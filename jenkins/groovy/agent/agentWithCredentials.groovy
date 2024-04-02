#!/usr/bin/env groovy
// =============================================================================
//      FileName : agentWithCredentials.groovy
//        Author : marslo.jiao@gmail.com
//       Created : 2024-02-16 19:48:48
//    LastChange : 2024-02-16 19:49:00
// =============================================================================

List<String> title = [ 'AGENT NAME', 'NODE CREDENTIAL', 'COMPUTER CREDENTIIAL' ]
List<List<String>> agentCredentials = jenkins.model.Jenkins.instance.computers.findAll { computer ->
  ! jenkins.model.Jenkins.MasterComputer.isInstance(computer) &&
  computer?.launcher instanceof hudson.plugins.sshslaves.SSHLauncher
}.collect { computer ->
  [ computer.name, computer.node.launcher?.credentialsId?.toString() ?: '', computer.launcher?.credentialsId?.toString() ?: '' ]
}

agentCredentials.add( 0, title )
agentCredentials.add( 0, agentCredentials.transpose().collect { column -> column.collect{ it.size() }.max() } )

agentCredentials = agentCredentials.withIndex().collect { raw, idx ->
  if ( idx ) raw.withIndex().collect { x, y -> x.toString().padRight(agentCredentials[0][y]) }
}.findAll()

String showTable ( List l ) {
  l.collect{ '| ' +  it.join(' | ' ) + ' |' }.join('\n')
}

println showTable( [ agentCredentials.head(), agentCredentials.head().collect { '-'*it.size() } ] )
println showTable( agentCredentials.tail() )

// vim:tabstop=2:softtabstop=2:shiftwidth=2:expandtab:filetype=Groovy
