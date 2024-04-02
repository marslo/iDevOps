println jenkins.model.Jenkins.instance.pluginManager.plugins
               .sort(false) { a, b ->
                                a.getShortName().toLowerCase() <=> b.getShortName().toLowerCase()
                            }
               .collect { plugin ->
                            "~~> ${plugin.shortName} : ${plugin.version} : ${plugin.displayName}" +
                            ( plugin.dependants   ? "\n\t+++ ${plugin.dependants.join('\n\t+++ ')}"   : '' )  +
                            ( plugin.dependencies ? "\n\t... ${plugin.dependencies.join('\n\t... ')}" : '' )
                        }
               .join('\n')
