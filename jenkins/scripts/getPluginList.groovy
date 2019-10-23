#!/usr/bin/env groovy
// inspired from https://stackoverflow.com/a/27242903/2940319

Jenkins.instance.pluginManager.plugins.each{ plugin ->
  println ("${plugin.getShortName()}:${plugin.getVersion()}")
}

// vim: ft=Jenkinsfile ts=2 sts=2 sw=2 et
