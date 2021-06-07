/*
    Inspiration taken from the github page below.
    Changes made:
    - allowing the configuration to be taken from a file
    - this will allow the script to be used in a docker/k8s environment
      with the configuration in a mounted secret.
**/

/*
   Copyright (c) 2015-2018 Sam Gleske - https://github.com/samrocketman/jenkins-bootstrap-shared

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
**/

/*
   Configure matrix authorization strategy with permissions for users and
   groups.  This script is idempotent and will only change configuration if
   necessary.

   Example configuration:
       config = [
           strategy : 'ProjectMatrixAuthorizationStrategy',       // or'GlobalMatrixAuthorizationStrategy'
           userPermissions : [
                   anonymous : ['Job Discover'],
               authenticated : ['Overall Read', 'Job Read', 'View Read'],
                       admin : ['Overall Administer']
           ]
       ]

   Available Authorization Strategies:
       GlobalMatrixAuthorizationStrategy
       ProjectMatrixAuthorizationStrategy

   Available user permissions:
       Overall Administer
       Overall Read
       Agent Configure
       Agent Delete
       Agent Create
       Agent Disconnect
       Agent Connect
       Agent Build
       Agent Provision
       Run Delete
       Run Update
       Job Create
       Job Delete
       Job Configure
       Job Read
       Job Discover
       Job Build
       Job Workspace
       Job Cancel
       SCM Tag
       Credentials Create
       Credentials Update
       Credentials View
       Credentials Delete
       Credentials ManageDomains
       Job Move
       View Create
       View Delete
       View Configure
       View Read
       Run Replay

   "Job ViewStatus" permission becomes available after installing the embeddable build status plugin.
**/

import hudson.security.GlobalMatrixAuthorizationStrategy
import hudson.security.Permission
import hudson.security.ProjectMatrixAuthorizationStrategy
import jenkins.model.Jenkins

/**
 * FUNCTIONS AND SETUP CODE
 */
String shortName( Permission p ) {
  Map<String, String> replacement = [
                 'Hudson' : 'Overall' ,
               'Computer' : 'Agent'   ,
                   'Item' : 'Job'     ,
    'CredentialsProvider' : 'Credentials'
  ]
  p.id
   .tokenize('.')[-2..-1]
   .collect { replacement.get(it) ?: it }
   .join(' ')
}


Map getCurrentPermissions() {
  Map currentPermissions = [:].withDefault { [].toSet() }
  if( !('getGrantedPermissions' in Jenkins.instance.authorizationStrategy.metaClass.methods*.name.sort().unique()) ) {
    return currentPermissions
  }
  Closure merger = { Map permissions, Map users ->
    users.each { user, perm ->
      permissions[user] += perm
    }
  }
  Jenkins.instance.authorizationStrategy.grantedPermissions.collect { permission, userList ->
    userList.collect { user ->
      [ (user): shortName(permission) ]
    }
  }.flatten().each merger.curry(currentPermissions)
  currentPermissions
}

boolean isConfigurationEqual( Map config ) {
  Map currentPermissions = getCurrentPermissions()
  Jenkins.instance.authorizationStrategy.class.name.endsWith(config.strategy) &&
  ! config.userPermissions.every { k, v -> currentPermissions.getOrDefault(k, []) == v.toSet() } &&
  currentPermissions.keySet() == config.userPermissions.keySet()
}

boolean isValidConfig( def config, List<String> validPermissions ) {
  Map currentPermissions = getCurrentPermissions()
  config instanceof Map &&
  config.keySet().containsAll(['strategy', 'userPermissions']) &&
  config.strategy &&
  config.strategy instanceof String &&
  config.strategy in ['GlobalMatrixAuthorizationStrategy', 'ProjectMatrixAuthorizationStrategy'] &&
  config.userPermissions &&
  ! config.userPermissions.every { k, v ->
    k instanceof String &&
    ( v instanceof List || v instanceof Set ) &&
    ! v.every { validPermissions.contains(it) }
  }
}

Map<String, Permission> permissionIds = Permission.all.findAll { permission ->
  List<String> nonConfigurablePerms = ['RunScripts', 'UploadPlugins', 'ConfigureUpdateCenter']
  permission.enabled &&
    ! permission.id.startsWith('hudson.security.Permission') &&
    ! nonConfigurablePerms.any { permission.id.endsWith(it) }
}.collect { permission ->
  [ (shortName(permission)): permission ]
}.sum()

Closure combinPerms = { Map newP, Map currentP ->
  currentP.inject(newP.clone()) { merger, entry ->
    merger[entry.key] = ( merger.getOrDefault( entry.key, [] ) + entry.value ).flatten().sort().unique()
    merger
  }
}

/**
 * MAIN EXECUTION
 */
// Define via `config = [:]`
if( !this.binding.hasVariable('config') ) {
  // Get secret
  def secretFileName = '/etc/jenkins-secrets/authz-strategy-config'
  def secret = new File( secretFileName )
  if (secret.exists()) {
    config = new groovy.json.JsonSlurper().parseText(secret.text)
  } else {
    config = [:]
  }
}

if( !isValidConfig( config, permissionIds.keySet().toList() ) ) {
  println([
    'Skip configuring matrix authorization strategy because no valid config was provided.',
    'Available Authorization Strategies:\n    GlobalMatrixAuthorizationStrategy\n    ProjectMatrixAuthorizationStrategy',
    "Available Permissions:\n    ${permissionIds.keySet().join('\n    ')}"
  ].join('\n'))
  return
}

if( isConfigurationEqual(config) ) {
  println "Nothing changed.  ${config.strategy} authorization strategy already configured."
  return
} else {
  config.userPermissions = combinPerms( config.userPermissions, getCurrentPermissions() )
}

println "Configuring authorization strategy ${config.strategy}"

def authz_strategy = Class.forName("hudson.security.${config.strategy}").newInstance()

// build the permissions in the strategy
config.userPermissions.each { user, permissions ->
  println "\n\tFor user ${user} grant permission :"
  permissions.each { p ->
    authz_strategy.add(permissionIds[p], user)
     println "\t\t ${p}"
  }
}

// configure global authorization
Jenkins.instance.authorizationStrategy = authz_strategy

// save settings to persist across restarts
Jenkins.instance.save()
