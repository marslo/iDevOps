import jenkins.*
import hudson.*
import jenkins.model.*
import hudson.model.*

final long CURRENT_TIME   = System.currentTimeMillis()
final int BENCH_MARK      = 1*24*60*60*1000

Map<String, Map<String, String>> results = [:]
int sum = 0

jenkins.model.Jenkins.instance.getAllItems( Job.class ).each { project ->
  if ( project.getBuilds().byTimestamp(CURRENT_TIME - BENCH_MARK, CURRENT_TIME).size() > 0 ) {
    results."${project.fullName}" = [ SUCCESS:0, UNSTABLE:0, FAILURE:0, ABORTED:0, INPROGRESS:0, NOT_BUILT:0 ]
    def build = project.getLastBuild()

    while ( build && (CURRENT_TIME - build.startTimeInMillis) <= BENCH_MARK ) {
      if ( build.isBuilding() ) {
        results."${project.fullName}".INPROGRESS = results."${project.fullName}".INPROGRESS + 1
      } else {
        results."${project.fullName}"."${build.result}" = results."${project.fullName}"."${build.result}" + 1
      } // if job is building, then results."${project.fullName}"."${build.result}" will be null
      build = build.getPreviousBuild()
    } // traverse in the whole traverse builds
  } // if there's builds within 24 hours
}

Map<String, String> data = results.collectEntries { name, status ->
  [ "${name}": "${status.values().sum()}" ]
}

println "~~> total: ${data.values().collect { it.toInteger() }.sum() }"
// to show build numbers only
// println '~> ' + data.collect { "${it.key} : ${it.value}" }.join('\n~> ')

// to show percentage of each pipeline/job
results.each{ name, status ->
  sum = status.values().sum()
  println "\n~~> ${name}: ${sum} : "
  status.each{ r, c ->
    if ( c ) println "\t${r.padRight(11)}: ${c.toString().padRight(5)}: percentage: " +
                     ( sum ? "${c * 100 / sum}%" : '0%' )
  }
}

"DONE"
