#!/usr/bin/env groovy
// inspired from https://wiki.jenkins.io/display/JENKINS/Jenkins+Script+Console#JenkinsScriptConsole-RunningScriptConsoleonthemaster

import hudson.util.RemotingDiagnostics
import jenkins.model.Jenkins

String agent_name = 'your agent name'
//groovy script you want executed on an agent
groovy_script = '''
println System.getenv("PATH")
println "uname -a".execute().text
'''.trim()

String result
Jenkins.instance.slaves.find { agent ->
  agent.name == agent_name
}.with { agent ->
  result = RemotingDiagnostics.executeGroovy( groovy_script, agent.channel )
}
println result

// Result
// /usr/local/bin:/usr/bin:/bin:/usr/games
// Linux CI-WP-CD-RPI001 4.9.80-v7+ #1098 SMP Fri Mar 9 19:11:42 GMT 2018 armv7l GNU/Linux

// vim: ft=Jenkinsfile ts=2 sts=2 sw=2 et
