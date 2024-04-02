#!/usr/bin/env groovy

import hudson.plugins.git.GitSCM
import hudson.plugins.git.UserRemoteConfig
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
import com.cloudbees.plugins.credentials.common.StandardCredentials
import com.cloudbees.plugins.credentials.CredentialsProvider

String credId    = 'GIT_SSH_CREDENTIAL'
String orgCredId = ''
String newUrl    = ''
String orgUrl    = ''

if ( CredentialsProvider.lookupCredentials( StandardCredentials.class, jenkins.model.Jenkins.instance)
                        .any { credId == it.id }
) {

  Jenkins.instance.getAllItems( WorkflowJob.class ).findAll {
    it.definition instanceof org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition &&
    ! it.definition?.scm?.userRemoteConfigs.collect { it.credentialsId }.contains( credId )
  }.each { job ->

    GitSCM orgScm          = job.definition?.scm
    Boolean orgLightweight = job.definition?.lightweight

    List<UserRemoteConfig> newUserRemoteConfigs = orgScm.userRemoteConfigs.collect {
      orgUrl    = it.url
      newUrl    = 'ssh://' + it.url.split('ssh://').last().split('@').last()
      orgCredId = it.credentialsId
      new UserRemoteConfig( newUrl, it.name, it.refspec, credId )
    }
    GitSCM newScm = new GitSCM( newUserRemoteConfigs, orgScm.branches, orgScm.doGenerateSubmoduleConfigurations,
                                orgScm.submoduleCfg, orgScm.browser, orgScm.gitTool, orgScm.extensions
                              )
    CpsScmFlowDefinition flowDefinition = new CpsScmFlowDefinition( newScm, job.definition.scriptPath )

    job.definition = flowDefinition
    job.definition.lightweight = orgLightweight
    job.save()
    println ">> " + job.fullName + " DONE :" +
            "\n\t - orgScm: ${orgCredId.padRight(30)}: ${orgUrl}" +
            "\n\t - newScm: ${credId.padRight(30)}: ${newUrl}"
  }

} else {
  println "${credId} CANNOT be found !!"
}

"DONE"
