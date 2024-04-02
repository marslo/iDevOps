// via DSL: ssh <domain> list-plugins | sed 's/  \+/_/g' | awk -F'_' '{print $1":"$3}' | sort -u
println jenkins.model.Jenkins
                     .instance
                     .pluginManager
                     .plugins
                     .toList()
                     .sort { it.getShortName() }
                     .collect { "${it.shortName}:${it.version}" }
                     .join('\n')
