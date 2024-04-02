import hudson.model.Node.Mode
import hudson.slaves.*
import jenkins.model.Jenkins

String name = 'ENG-VEGA-TP-RPI098'

Jenkins.instance.computers.findAll { computer ->
  ! jenkins.model.Jenkins.MasterComputer.isInstance(computer) &&
  computer?.launcher instanceof hudson.plugins.sshslaves.SSHLauncher
}.each { agent ->

  ComputerLauncher launcher = agent.launcher

  println agent.name
  println agent.remoteFS
  println agent.numExecutors
  println '-'*10

  println launcher
  println launcher.host
  println launcher.port
  println launcher.credentialsId
  println launcher.jvmOptions
  println launcher.javaPath
  println launcher.prefixStartSlaveCmd
  println launcher.suffixStartSlaveCmd
  println launcher.launchTimeoutSeconds
  println launcher.maxNumRetries
  println launcher.retryWaitTime
  println launcher.sshHostKeyVerificationStrategy

}


import hudson.model.Node.Mode
import hudson.slaves.*
import jenkins.model.Jenkins

String name = 'ENG-VEGA-TP-RPI098'

Jenkins.instance.nodes.findAll { node ->
  ! jenkins.model.Jenkins.MasterComputer.isInstance(node) &&
  node?.launcher instanceof hudson.plugins.sshslaves.SSHLauncher
}.each { node ->

  ComputerLauncher launcher = node.launcher
  DumbSlave node = new DumbSlave(

  println node.name
  println node.remoteFS
  println node.nodeDescription
  println node.numExecutors
  println node.mode
  println node.retentionStrategy

  println '-'*10

  println launcher
  println launcher.host
  println launcher.port
  println launcher.credentialsId
  println launcher.jvmOptions
  println launcher.javaPath
  println launcher.prefixStartSlaveCmd
  println launcher.suffixStartSlaveCmd
  println launcher.launchTimeoutSeconds
  println launcher.maxNumRetries
  println launcher.retryWaitTime
  println launcher.sshHostKeyVerificationStrategy

}


import hudson.model.Node.Mode
import hudson.slaves.*
import jenkins.model.Jenkins

String name = 'ENG-VEGA-TP-RPI098'
String newCredId = 'GIT_SSH_CREDENTIAL'

Jenkins.instance.nodes.findAll { node ->
  ! jenkins.model.Jenkins.MasterComputer.isInstance(node) &&
  node?.launcher instanceof hudson.plugins.sshslaves.SSHLauncher
}.each { node ->

  ComputerLauncher launcher = node.launcher
  SSHLauncher newLauncher = new hudson.plugins.sshslaves.SSHLauncher( launcher.host,
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

  agent.save()

}



import jenkins.model.Jenkins
import hudson.plugins.sshslaves.SSHLauncher
import hudson.slaves.ComputerLauncher

String newCredId = 'GIT_SSH_CREDENTIAL'

Jenkins.instance.nodes.findAll { node ->
  ! jenkins.model.Jenkins.MasterComputer.isInstance(node) &&
  node?.launcher instanceof hudson.plugins.sshslaves.SSHLauncher &&
  'CI-LIONMS-HC-SUT005' == node.name
}.each { node ->

  println ">> ${node.name} update <<"
  ComputerLauncher launcher = node.launcher
  SSHLauncher newLauncher = new SSHLauncher( launcher.host, launcher.port, newCredId )
  node.setLauncher( newLauncher )
  node.save()

  if ( node.computer.isOnline() && node.computer.countBusy() == 0 ) {
    println ">> ${node.name} disconnect <<"
    String messasge = 'disconnect due to update credential'
    node.computer.setTemporarilyOffline(true, new hudson.slaves.OfflineCause.ByCLI( message ))
    node.computer.doChangeOfflineCause( message )
    println '\t.. computer.getOfflineCause: ' + node.computer.getOfflineCause();
  }
  sleep 10
  if ( ! node.computer.isOnline() ) {
    println ">> ${node.name} re-connect <<"
    node.toComputer().connect(true)
  }

}

"DONE"
