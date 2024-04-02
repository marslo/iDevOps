import org.jenkinsci.plugins.workflow.job.WorkflowJob

String branch = 'develop'
// String branch = 'refs/heads/develop'

jenkins.model.Jenkins.instance.getAllItems(WorkflowJob.class).findAll{
  it.definition instanceof org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
}.findAll {
  it.definition?.scm instanceof hudson.scm.NullSCM
}.each {
  println it.fullName.toString()
}

"DONE"
