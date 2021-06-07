#!/usr/bin/env groovy

import hudson.model.*
import jenkins.model.*
import hudson.slaves.*
import hudson.slaves.EnvironmentVariablesNodeProperty.Entry
import hudson.plugins.sshslaves.verifiers.*
import groovy.transform.Field

@Field final String DEFAULT_WORKSPACE     = '/home/jenkins'
@Field final String AGENT_SSH_CREDENTIAL  = 'AGENT_SSH_CREDENTIAL'
@Field final int AGENT_SSH_PORT           = 22
@Field final int MAX_NUM_RETRIES          = 5
@Field final int RETRY_WAIT_TIME          = 30
@Field final int LAUNCH_TIMEOUT_SECONDS   = 30
@Field final int AGENT_CONNECTION_TIMEOUT = 5*60*1000
@Field final int AGENT_CONNECTION_SLEEP   = 30

/**
 * verify agent with given condition
 *
 * @param predicate  the predicate for all nodes in Jenkins
 * @return           {@code Boolean}. {@code true} for matches the predicate
 *
 * @see              <a href="https://javadoc.jenkins-ci.org/jenkins/model/Jenkins.html">jenkins.model.Jenkins.instance</a>
**/
def verifyAgent( Closure predicate ) {
  jenkins.model.Jenkins.instance.getNodes().any(predicate)
}

/**
 * Check whether if the agent exists or not
 *
 * @param name       the agent node name
 * @return           {@code Boolean} type {@code true} or {@code false}
 *
 * @see              {@link #verifyAgent(Closure)}
**/
def isAgentExists( String name ) {
  verifyAgent { name == it.computer?.name }
}

/**
 * Check whether if the agent online or not
 *
 * @param name       the agent node name
 * @return           {@code Boolean} type {@code true} or {@code false}. If agent isn't exists, return {@code false} by default
 *
 * @see              {@link #verifyAgent(Closure)}
**/
def isAgentOnline( String name ) {
  verifyAgent { name == it.computer?.name && it.computer.online }
}

/**
 * waiting for given agent connected within timeslot
 *
 * @param name       the agent node name
 * @param timeout    check connection within given timeout. default is 5 mins
 * @param waiting    the check frequency. default is check every 30 secs
 * @return           {@code Boolean} type {@code true} or {@code false}. If agent isn't exists, return {@code false} by default
 *
 * @see              <a href="https://javadoc.jenkins.io/hudson/slaves/DumbSlave.html">hudson.slaves.DumbSlave</a>
**/
def waitForAgentConnect( String name,
                         int timeout = AGENT_CONNECTION_TIMEOUT,
                         int waiting = AGENT_CONNECTION_SLEEP
) {
  DumbSlave agent   = jenkins.model.Jenkins.instance.getNode( name )
  Boolean connected = false
  long start        = System.currentTimeMillis()

  while ( !connected ) {
    if ( (System.currentTimeMillis() - start) > timeout ) {
      println """
      "agent log for ${name} : "
      ........................................
        ${agent.toComputer().getLog().toString()}
      ........................................
      """
      error( "Error: ${name} failed to establish a channel connection within ${timeout/1000} secs.")
    }

    println "... waiting for agent ${name} online. " +
            "retry in ${waiting} secs. " +
            "there are ${(int)Math.ceil(timeout/1000/waiting) - (int)Math.floor( (System.currentTimeMillis() - start)/1000/waiting )} more retries left ..."
    Thread.sleep( waiting*1000 )
    connected = ( agent.computer.getChannel() != null )
  }

  return connected
}


/**
 * get agent log
 *
 * @param name       the agent node name
 *
 * @see              <a href="https://javadoc.jenkins.io/hudson/slaves/DumbSlave.html">hudson.slaves.DumbSlave</a>
**/
def getAgentLog( String name ) {
  DumbSlave agent = jenkins.model.Jenkins.instance.getNode( name )
  return agent.toComputer().getLog().toString()
}

/**
 * Create agent via SSH way, and waiting for connection established if necessary
 *
 * @param name                the agent node name
 * @param ip                  the ip address of agent
 * @param trigger             the user id who trigger the jenkins job
 * @param description         the agent description
 * @param waitForConnect      whether or not waiting for agent connected. default is {@code true}
 * @return                    {@code Boolean} type {@code true} or {@code false} for agent created or agent created and connected
 *
 * @see                       <a href="https://javadoc.jenkins.io/hudson/slaves/DumbSlave.html">hudson.slaves.DumbSlave</a>
 * @see                       <a href="https://javadoc.jenkins.io/plugin/ssh-slaves/hudson/plugins/sshslaves/SSHLauncher.html">hudson.plugins.sshslaves.SSHLauncher</a>
 * @see                       <a href="https://javadoc.jenkins.io/plugin/ssh-slaves/hudson/plugins/sshslaves/verifiers/NonVerifyingKeyVerificationStrategy.html">hudson.plugins.sshslaves.verifiers.NonVerifyingKeyVerificationStrategy</a>
**/
def createAgentWithSSH( String name,
                        String ip,
                        String trigger,
                        String description = '',
                        Boolean waitForConnect = true
) {
  DumbSlave agent = new DumbSlave(
                      name ,
                      DEFAULT_WORKSPACE ,
                      new hudson.plugins.sshslaves.SSHLauncher(
                        ip                               ,        // Host
                        AGENT_SSH_PORT                   ,        // Port
                        AGENT_SSH_CREDENTIAL             ,        // Credentials
                        (String)null                     ,        // JVM Options
                        (String)null                     ,        // JavaPath
                        (String)null                     ,        // Prefix Start Agent Command
                        (String)null                     ,        // Suffix Start Agent Command
                        (Integer) LAUNCH_TIMEOUT_SECONDS ,        // Connection Timeout in Seconds
                        (Integer) MAX_NUM_RETRIES        ,        // Maximum Number of Retries
                        (Integer) RETRY_WAIT_TIME        ,        // The number of seconds to wait between retries
                        new NonVerifyingKeyVerificationStrategy() // Host Key Verification Strategy
                      )
  )
  agent.nodeDescription = ( description ? "${description} | " : "${name} : ${ip} | " ) +
                          "created automatically by @${trigger} via Jenkins Job: ${env.BUILD_URL} "
  agent.numExecutors = 1
  agent.labelString = ""
  agent.mode = Node.Mode.NORMAL
  agent.retentionStrategy = new RetentionStrategy.Always()

  println ( "~~> INFO: initialize the agent ${name} :" )
  Jenkins.instance.addNode( agent )
  return waitForConnect ? waitForAgentConnect(name) : isAgentExists(name)
}

// vim: ts=2 sts=2 sw=2 et ft=Groovy
