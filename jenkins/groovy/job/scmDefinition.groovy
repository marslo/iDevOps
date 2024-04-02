import org.jenkinsci.plugins.workflow.job.WorkflowJob

// jenkins.model.Jenkins.instance.getAllItems(WorkflowJob.class).findAll{
//   it.definition instanceof org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
// }.each {
//   println it.fullName.toString().padRight(30) +
//           (it.definition?.scm?.branches?.join() ?: '').padRight(30) +
//           it.definition?.scm?.repositories?.collect{ it.getURIs() }?.flatten()?.join()
// }

jenkins.model.Jenkins.instance.getAllItems(WorkflowJob.class).findAll{
  it.definition instanceof org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
}.each {
  println it.fullName.toString().padRight(40) +
          ( it.definition?.scm?.branches?.join() ?: '' ).padRight(30) +
          it.definition?.scm?.userRemoteConfigs.collect { it.credentialsId }.join().padRight(30) +
          it.definition?.scm?.repositories?.collect{ it.getURIs() }?.flatten()?.join()
}

"DONE"
