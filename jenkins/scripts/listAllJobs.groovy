#!/usr/bin/env groovy
// inspired from https://support.cloudbees.com/hc/en-us/articles/226941767-Groovy-to-list-all-jobs

jenkins.model.Jenkins.instance.getAllItems(jenkins.model.ParameterizedJobMixIn.ParameterizedJob.class).each {
  if(it.isDisabled()){
    println it.fullName;
  }
}

// vim: ft=Jenkinsfile ts=2 sts=2 sw=2 et
