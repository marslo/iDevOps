#!/usr/bin/env groovy
// cron: * * * * *

import org.jenkinsci.plugins.scriptsecurity.scripts.*
ScriptApproval scriptApproval = ScriptApproval.get()
scriptApproval.pendingScripts.each {
  println "Approving: ${it.hash}"
  scriptApproval.approveScript(it.hash)
}
