#!/usr/bin/env groovy
// inspired from https://stackoverflow.com/a/27242903/2940319

import hudson.FilePath
import hudson.model.Node
import hudson.model.Slave
import jenkins.model.Jenkins
import groovy.time.*

Jenkins jenkins = Jenkins.instance
def jenkinsNodes =jenkins.nodes

while(1)
{
  for (Node node in jenkinsNodes) {
    sleep(1000)
    // Make sure slave is online
    if (!node.getComputer().isOffline())
    {
      //Make sure that the slave busy executor number is 0.
      if(node.getComputer().countBusy()==0) {
        println "'$node.nodeName' can take jobs !!!"
        return 0
      } else {
        println "$node.nodeName' is busy !!!"
      }
    } else {
      println "'$node.nodeName' is offline !!!"
    }
  }
  sleep(1000)
}

// vim: ft=Jenkinsfile ts=2 sts=2 sw=2 et
