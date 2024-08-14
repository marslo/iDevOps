#!/usr/bin/env groovy

/**
 * - usage:
 *   - list necessary plugins
 *     - `$ ssh <JENKINS_URL> groovy = < get-plugins.groovy`
 *   - list necessary plugins without version
 *     - `$ ssh <JENKINS_URL> groovy = nameOnly < get-plugins.groovy`
 *   - list all plugins
 *     - `$ ssh <JENKINS_URL> groovy = all < get-plugins.groovy`
 *   - list all plugins wihtout version
 *     - `$ ssh <JENKINS_URL> groovy = all nameOnly < get-plugins.groovy`
 * - check [jenkins-cli](https://www.jenkins.io/doc/book/managing/cli/) for more information
**/

import jenkins.model.Jenkins

Boolean showAll               = false
Boolean nameOnly              = false
List<String> necessaryPlugins = [
  'ansicolor',
  'artifactory',
  'badge',
  'blueocean',
  'blueocean-autofavorite',
  'blueocean-jira',
  'build-timeout',
  'buildtriggerbadge',
  'cloudbees-disk-usage-simple',
  'cloudbees-folder',
  'cobertura',
  'code-coverage-api',
  'config-file-provider',
  'configuration-as-code',
  'copyartifact',
  'coverage',
  'credentials',
  'credentials-binding',
  'custom-checkbox-parameter',
  'customizable-header',
  'docker-commons',
  'docker-workflow',
  'dynamic_extended_choice_parameter',
  'email-ext',
  'envinject',
  'extended-choice-parameter',
  'extended-read-permission',
  'file-leak-detector',
  'file-parameters',
  'gerrit-code-review',
  'gerrit-trigger',
  'git',
  'git-client',
  'git-forensics',
  'groovy',
  'groovy-postbuild',
  'hashicorp-vault-pipeline',
  'hashicorp-vault-plugin',
  'htmlpublisher',
  'instance-identity',
  'jira',
  'jira-steps',
  'job-dsl',
  'jobConfigHistory',
  'junit',
  'klocwork',
  'kubernetes',
  'kubernetes-cli',
  'kubernetes-client-api',
  'kubernetes-credentials',
  'ldap',
  'lockable-resources',
  'logstash',
  'material-theme',
  'matrix-auth',
  'monitoring',
  'nested-view',
  'parameterized-scheduler',
  'parameterized-trigger',
  'people-view',
  'permissive-script-security',
  'pipeline-build-step',
  'pipeline-groovy-lib',
  'pipeline-input-step',
  'pipeline-rest-api',
  'pipeline-stage-step',
  'pipeline-stage-view',
  'pipeline-timeline',
  'pipeline-utility-steps',
  'plugin-usage-plugin',
  'PrioritySorter',
  'promoted-builds',
  'rebuild',
  'sectioned-view',
  'simple-theme-plugin',
  'slack',
  'snakeyaml-api',
  'sse-gateway',
  'ssh-agent',
  'ssh-credentials',
  'ssh-slaves',
  'sshd',
  'timestamper',
  'uno-choice',
  'validating-string-parameter',
  'versioncolumn',
  'warnings-ng',
  'workflow-aggregator',
  'workflow-api',
  'workflow-basic-steps',
  'workflow-cps',
  'workflow-durable-task-step',
  'workflow-job',
  'workflow-multibranch',
  'workflow-scm-step',
  'workflow-step-api',
  'workflow-support',
  'ws-cleanup',
  'yet-another-build-visualizer'
]

if ( args.contains('all')      ) { showAll  = true }
if ( args.contains('nameOnly') ) { nameOnly = true }
println Jenkins.instance
               .pluginManager
               .plugins
               .findAll { showAll ? it : necessaryPlugins.contains(it.getShortName()) }
               .sort(false) { a, b -> a.getShortName().toLowerCase() <=> b.getShortName().toLowerCase() }
               .collect { plugin -> "${plugin.getShortName()}" + ( nameOnly ? '' : ":${plugin.getVersion()}" ) }
               .join('\n')

// vim:tabstop=2:softtabstop=2:shiftwidth=2:expandtab:filetype=Groovy
