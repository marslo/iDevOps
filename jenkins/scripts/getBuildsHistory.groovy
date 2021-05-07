// get single build
def job = Jenkins.instance.getItemByFullName('<group>/<name>')
println "All builds: "
job.getBuilds().each{
  println """
              id : ${it.getId()}
    build number : ${it.getNumber()}
     start since : ${it.getTimestampString()}
     xs:dateTime : ${it.getTimestampString2()}
        duration : ${it.getDurationString()}
  """
}



// get single build within 24 hours
def numberOfHoursBack = 1*24
def job = Jenkins.instance.getItemByFullName('<group>/<jobName>')
def history = job.getBuilds().byTimestamp(System.currentTimeMillis()-numberOfHoursBack*60*60*1000, System.currentTimeMillis())
println """
  history.size(): ${history.size()}
  history: ${history}
"""



// get history in a group
String jobPattern = '<group>/'
def numberOfHoursBack = 1*24

Jenkins.instance.getAllItems(Job.class).findAll { Job job ->
  job.fullName.contains(jobPattern)
}.collect { Job job ->
  println "~~~> job.fullName: ${job.fullName}"
  def history = job.getBuilds().byTimestamp(System.currentTimeMillis()-numberOfHoursBack*60*60*1000, System.currentTimeMillis())
    println """
      ${job.fullName}: ${history.size()}
      history: ${history}
    """
}
println "DONE"
