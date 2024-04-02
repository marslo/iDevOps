#!/usr/bin/env groovy

import hudson.slaves.*
import hudson.model.Node.Mode
import jenkins.model.Jenkins
import hudson.plugins.sshslaves.SSHLauncher

String newCredId = 'AGENT_SSH_CREDENTIAL'

Jenkins.instance.nodes.findAll { node ->
  ! jenkins.model.Jenkins.MasterComputer.isInstance(node) &&
  node?.launcher instanceof hudson.plugins.sshslaves.SSHLauncher
}.each { node ->
  println ">> ${node.name} update <<"
  ComputerLauncher launcher = node.launcher
  SSHLauncher newLauncher = new SSHLauncher( launcher.host,
                                             launcher.port,
                                             newCredId,
                                             launcher.jvmOptions,
                                             launcher.javaPath,
                                             launcher.prefixStartSlaveCmd,
                                             launcher.suffixStartSlaveCmd,
                                             launcher.launchTimeoutSeconds,
                                             launcher.maxNumRetries,
                                             launcher.retryWaitTime,
                                             launcher.sshHostKeyVerificationStrategy
                                           )
  DumbSlave agent = new DumbSlave( node.name, node.remoteFS, newLauncher )
  agent.nodeDescription   = node.nodeDescription
  agent.numExecutors      = node.numExecutors
  agent.labelString       = node.labelString
  agent.mode              = node.mode
  agent.retentionStrategy = node.retentionStrategy
  node.computer.doDoDelete()
  Thread.sleep( 5*1000 )
  Jenkins.instance.addNode( agent )
}

"DONE"
// vim:tabstop=2:softtabstop=2:shiftwidth=2:expandtab:filetype=Groovy
