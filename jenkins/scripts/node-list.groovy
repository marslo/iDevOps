def nodes = []
jenkins.model.Jenkins.instance.computers.each { c ->
  if (c.node.labelString.contains('windows')) {
    nodes.add(c.node.selfLabel.name)
  }
}
println nodes
