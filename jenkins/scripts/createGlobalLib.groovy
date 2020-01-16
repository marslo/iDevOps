#!groovy

// inspired from https://plugins.jenkins.io/workflow-cps-global-lib
// imports
import hudson.scm.SCM
import jenkins.model.Jenkins
import jenkins.plugins.git.GitSCMSource
import org.jenkinsci.plugins.workflow.libs.*
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever

// parameters
def globalLibrariesParameters = [
  branch:               "master",
  credentialId:         "global-shared-library-key",
  implicit:             false,
  name:                 "Your Global Shared Library name here",
  repository:           "git@bitbucket.org:your-company/your-repo.git"
]

// define global library
GitSCMSource gitSCMSource = new GitSCMSource(
  "global-shared-library",
  globalLibrariesParameters.repository,
  globalLibrariesParameters.credentialId,
  "*",
  "",
  false
)

// define retriever
SCMSourceRetriever sCMSourceRetriever = new SCMSourceRetriever(gitSCMSource)

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

// get Jenkins Global Libraries
def globalLibraries = jenkins.getDescriptor("org.jenkinsci.plugins.workflow.libs.GlobalLibraries")

// define new library configuration
LibraryConfiguration libraryConfiguration = new LibraryConfiguration(globalLibrariesParameters.name, sCMSourceRetriever)
libraryConfiguration.setDefaultVersion(globalLibrariesParameters.branch)
libraryConfiguration.setImplicit(globalLibrariesParameters.implicit)

// set new Jenkins Global Library
globalLibraries.get().setLibraries([libraryConfiguration])

// save current Jenkins state to disk
jenkins.save()
