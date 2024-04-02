import org.jenkinsci.plugins.workflow.job.WorkflowJob

jenkins.model.Jenkins.instance.getAllItems(WorkflowJob.class).findAll{
  it.definition instanceof org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
}.each {
  println it.fullName.toString().padRight(30) + ' ~> ' + it?.definition?.getScriptPath()
}
