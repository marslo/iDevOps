import hudson.model.Job
import hudson.model.Run
import java.util.Calendar
import jenkins.model.Jenkins

final List<String> PROJECTS = [ 'vega' ]
final long BENCH_MARK       = 1*24*60*60*1000
final Calendar RIGHT_NOW    = Calendar.getInstance()

Jenkins.instance.getAllItems(Job.class).findAll { Job job ->
  PROJECTS.any { job.fullName.startsWith(it) }
}.collectEntries { Job job ->
  [
    ( job.fullName ) : job.builds.findAll { Run run ->
                         ( RIGHT_NOW.getTimeInMillis() - run.getStartTimeInMillis() ) <= BENCH_MARK
                       }.collectEntries { Run run ->
                         [
                           (run.id) : run.getCauses()?.collect { it.getClass().getSimpleName() }?.join(' -> ')
                         ]
                       }

  ]
}.collectEntries { k, v ->
  [
    (k) : v.groupBy( {it.value} )
           .collectEntries{ x, y -> [ (x) : y.keySet() ] }
  ]
}.each{ k, v ->
  println "~~> ${k} : ${v.values().flatten().size()}"
  println v.collect { "\t${it.key} : \n\t\t${it.value.join('\n\t\t')}" }.join('\n')
}

"DONE"
