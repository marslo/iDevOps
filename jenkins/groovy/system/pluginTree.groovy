import hudson.PluginWrapper

def getDependencyTree( String keyword, Integer benchmark = 2, Integer index = 0 ) {
  String prefix        = index ? '\t' + "|\t"*(index-1) + "|... " : ''
  PluginWrapper plugin = jenkins.model.Jenkins.instance.pluginManager.plugins.find { keyword == it.shortName }
  List dependencies    = plugin.collect { it.dependencies }.flatten() ?: []

  println prefix + "${plugin.shortName} ( ${plugin.version} )"

  if ( dependencies && benchmark != index ) {
    dependencies.collect{ it.shortName }.each { getDependencyTree (it, benchmark, index+1) }
  }
}

// getDependencyTree( 'jsch', 100 )
getDependencyTree( 'jaxb', 100 )

"DONE"
