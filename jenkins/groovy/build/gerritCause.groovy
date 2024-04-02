import hudson.util.RunList
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritCause

WorkflowJob job = jenkins.model.Jenkins.instance.getItemByFullName( 'vega/precommit' )
RunList builds  = job.getBuilds()

println builds.findAll { build -> build.getCause( GerritCause.class ) && true }
              .collect { build -> build.number }
