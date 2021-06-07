#!/usr/bin/env groovy
// inspired from https://stackoverflow.com/a/40279671/2940319

import hudson.model.Computer.ListPossibleNames
label = 'mytester'
slaves = Hudson.instance.slaves.findAll { it.getLabelString().split() contains "${label}" }
slaves.each {
  println "slave '${it.name}' has these IPs: " +  it.getChannel().call(new ListPossibleNames())
}

// vim: ft=Jenkinsfile ts=2 sts=2 sw=2 et
