//
// Cleanup workspace on all builders for given jenkins job.
//
// This script resides on on the jenkins master.
// It is called from a postbuild groovy action and
// requires the THE_JOB_NAME build string paramater.
//
import hudson.model.*

def jobName
def actionList = manager.build.getActions(ParametersAction)
if (actionList.size() != 0) {
  def pA = actionList.get(0)
  jobName = pA.createVariableResolver(manager.build).resolve("THE_JOB_NAME")
}

if (jobName) {
    def job = Hudson.instance.getItem(jobName)
    if (!job) {
        manager.listener.logger.println("Job name " + jobName + " not found. Skipping workspace cleanup.")
    } else if (job.isBuilding()) {
        manager.listener.logger.println("Skipping job " + jobName + ", currently building")
    } else {
        manager.listener.logger.println("Cleanup " + jobName + " workspaces")
        customWorkspace = job.getCustomWorkspace()
        manager.listener.logger.println("    Custom workspace = " + customWorkspace)
        for (node in Hudson.getInstance().getNodes()) {
            workspacePath = node.getWorkspaceFor(job)
            if (workspacePath == null) {
                manager.listener.logger.println("    Could not get workspace path for node " + node.getDisplayName())
            } else {
                if (customWorkspace != null) {
                    workspacePath = node.getRootPath().child(customWorkspace)
                }
                pathAsString = workspacePath.getRemote()
                if (workspacePath.exists()) {
                    manager.listener.logger.println("    Deleting from " + node.getDisplayName() + ":" + pathAsString)
                    workspacePath.deleteRecursive()
                }
           }
        }
    }
} else {
    manager.listener.logger.println("ERROR: the job name is empty. Skipping")
}
