List<List<String>> plugins = [[ 'DISPLAY NAME', 'SHORT NAME', 'VERSION' ]]
plugins += jenkins.model.Jenkins.instance
                  .pluginManager
                  .plugins
                  .sort(false) { a, b ->
                                   a.getShortName().toLowerCase() <=> b.getShortName().toLowerCase()
                               }
                  .collect { plugin ->
                    [ plugin.displayName, plugin.shortName, plugin.version ]
                  }

List<Integer> width = ( 1..(plugins.collect{ it.size() }.sort().last()) ).collect{ n ->
  plugins.collect{ it.get(n-1).length() }.sort().last()
}

println plugins.collect { plugin ->
  ( 1..plugin.size() ).collect { n ->
    Integer padSize = width.get(n-1)
    plugin.get(n-1).padRight(padSize)
  }.join( ' | ' )
}.join('\n')
