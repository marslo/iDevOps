import org.jenkinsci.plugins.workflow.job.WorkflowJob

String branch = 'develop'
// String branch = 'refs/heads/develop'

jenkins.model.Jenkins.instance.getAllItems(WorkflowJob.class).findAll{
  it.definition instanceof org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
}.findAll {
  ! ( it.definition?.scm instanceof hudson.scm.NullSCM ) &&
  ! it.definition?.scm?.branches?.any{ it.getName().contains(branch) }
}.each {
  println it.fullName.toString().padRight(50) + ' : ' +
          it.definition?.scm?.branches?.collect{ it.getName() }?.join(', ')
}

"DONE"
