import jenkins.*
import jenkins.model.*

println jenkins.model.Jenkins.instance.getNodes().findAll {
  [ 'AbstractCloudSlave', 'AbstractCloudComputer' ].contains( it.class.superclass?.simpleName )
}.size()
