#!/usr/bin/env groovy

jenkins.model.Jenkins.instance.getAllItems( hudson.model.Job.class ).findAll {
  it.hasProperty( 'typicalSCM' ) &&
  it.typicalSCM instanceof hudson.plugins.git.GitSCM
}.each { job ->
  println job.fullName.padRight(40) + ' : ' +
          ( job.typicalSCM.branches?.join() ?: '' ).padRight(40) +
          job.typicalSCM.userRemoteConfigs?.collect { it.credentialsId }.join().padRight(30) +
          job.typicalSCM.repositories?.collect{ it.getURIs() }?.flatten()?.join()
}

"DONE"

// vim:tabstop=2:softtabstop=2:shiftwidth=2:expandtab:filetype=Groovy
