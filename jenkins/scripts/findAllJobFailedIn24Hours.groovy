import hudson.model.Job
import hudson.model.Result
import hudson.model.Run
import java.util.Calendar
import jenkins.model.Jenkins
import static groovy.json.JsonOutput.*

final Calendar rightNow  = Calendar.getInstance()
final long BENCH_MARK    = 1*24*60*60*1000
final String JOB_PATTERN = 'keyword'
Map res = [:]

Jenkins.instance.getAllItems(Job.class).findAll { Job job ->
  job.fullName.contains(JOB_PATTERN)
}.each { Job job ->
  res.(job.fullName) = job.builds.findAll { Run run ->
    !run.isBuilding() &&
    run.result == Result.FAILURE &&
    (rightNow.getTimeInMillis() - run.getStartTimeInMillis()) <= BENCH_MARK
  }.collectEntries { Run run ->
    [ run.id, run.getAbsoluteUrl() ]
  }
}

println prettyPrint( toJson(res.findAll{ !it.value.isEmpty() }) )
