#!/usr/bin/env groovy

import hudson.model.*
import jenkins.model.*
import hudson.slaves.*
import groovy.transform.Field

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
 * completed remove the regular agent from Jenkins.
 * <p>
 * if agent is tied to project (in use), then skip the removal.
 * {@code CloudComputer} or {@code CloudSlave} or {@code MasterComputer} are not "regular" agent
 *
 * @param name       the agent node name
 * @return           {@code Boolean} type {@code true} or {@code false} for removal result. only succeed removal will be {@code true}
 *
 * @see              <a href="https://javadoc.jenkins-ci.org/jenkins/model/Jenkins.html">jenkins.model.Jenkins.instance</a>
 * @see              <a href="https://javadoc.jenkins.io/hudson/slaves/DumbSlave.html">hudson.slaves.DumbSlave</a>
**/
def removeAgent( String name ) {
  Boolean deleted = false
  DumbSlave agent = jenkins.model.Jenkins.instance.getNode( name )
  Computer computer = agent.toComputer()

  if ( agent
       &&
       ! AbstractCloudComputer.isInstance( computer )
       &&
       ! AbstractCloudSlave.isInstance( computer )
       &&
       ! ( computer instanceof jenkins.model.Jenkins.MasterComputer )
  ) {
    Boolean online = computer.isOnline()
    Boolean busy   = computer.countBusy() != 0

    if ( !busy ) {
      println """
        ${"${online ? ' offline and' : ''} remove agent ${name} :"}
           display name : ${agent.getDisplayName()}
            description : ${agent.getNodeDescription()}
               executor : ${agent.getNumExecutors()}
              node mode : ${agent.getMode()}
                online? : ${online}
                  busy? : ${busy}
         offline cause? : ${computer.getOfflineCause()}
      """
      if ( online ) {
        computer.setTemporarilyOffline( true,
                                        new hudson.slaves.OfflineCause.ByCLI('offline due to agent will be removed automatically')
        )
        Thread.sleep( 5*1000 )
      }
      computer.doDoDelete()
      deleted = ! isAgentExists( name )
      println ( "~~> INFO: agent ${name} ${deleted ? 'has been successfully removed' : 'failed been removed'} from ${env.JENKINS_URL}computer")
    } else {
      String worker = computer.allExecutors.collect { it.currentWorkUnit?.work?.runId ?: '' }.join(', ')
      println ( "WARN: the agent ${name} cannot be removed due to tied to project(s) ${worker}" )
    }
  } else {
    println ( 'WARN: cloud agent or Jenkins master cannot be removed!' )
  }

  return deleted
}

// vim: ts=2 sts=2 sw=2 et ft=Groovy
