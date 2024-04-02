println jenkins.model.Jenkins.instance
       .pluginManager
       .plugins
       .sort(false) { a, b ->
                        a.getShortName().toLowerCase() <=> b.getShortName().toLowerCase()
                    }
       .collect { plugin ->
         "    - ${plugin.shortName}:${plugin.version}"
       }
       .join('\n')

"DONE"
