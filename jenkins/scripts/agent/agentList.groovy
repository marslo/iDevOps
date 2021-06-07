#!/usr/bin/env groovy
// inspired from

def nodes = []
jenkins.model.Jenkins.instance.computers.each { c ->
  if (c.node.labelString.contains('windows')) {
    nodes.add(c.node.selfLabel.name)
  }
}
println nodes

// vim: ft=Jenkinsfile ts=2 sts=2 sw=2 et
