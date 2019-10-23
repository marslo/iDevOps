#!/usr/bin/env groovy
// inspired from https://gist.github.com/dnozay/e7afcf7a7dd8f73a4e05

// Licensed under MIT
// author : Damien Nozay

// list jobs and their last build.

jobs = Jenkins.instance.getAllItems()
jobs.each { j ->
  if (j instanceof com.cloudbees.hudson.plugins.folder.Folder) { return }
  println 'JOB: ' + j.fullName
  numbuilds = j.builds.size()
  if (numbuilds == 0) {
    println '  -> no build'
    return
  }
  lastbuild = j.builds[numbuilds - 1]
    println '  -> lastbuild: ' + lastbuild.displayName + ' = ' + lastbuild.result + ', time: ' + lastbuild.timestampString2
}

// returns blank
''

// vim: ft=Jenkinsfile ts=2 sts=2 sw=2 et
