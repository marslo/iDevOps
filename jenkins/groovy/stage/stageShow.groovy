import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*
import org.jenkinsci.plugins.workflow.job.*
import org.jenkinsci.plugins.workflow.flow.*
import io.jenkins.blueocean.rest.impl.pipeline.*
import org.jenkinsci.plugins.workflow.cps.*
import org.jenkinsci.plugins.workflow.graph.FlowNode;

String JOB_NAME = 'marslo/pre'
Integer BUILD_NUMBER = 8

WorkflowRun run = Jenkins.instance
                   .getItemByFullName( JOB_NAME )
                   .getBuildByNumber( BUILD_NUMBER )
PipelineNodeGraphVisitor visitor = new PipelineNodeGraphVisitor(run)
List<FlowNodeWrapper> flowNodes = visitor.getPipelineNodes()
println flowNodes.size()

flowNodes.each {
  println """
    ${it.getDisplayName()} :
                          getRun() : ${it.getRun()}
                         getResult : ${it.status.getResult()}
                          getState : ${it.status.getState()}
                           getType : ${it.getType()}
                             getId : ${it.getId()}
                          isActive : ${it.node.active}
                         searchUrl : ${it.node.getSearchUrl()}
                            getUrl : ${Jenkins.instance.getRootUrl() + it.node.getUrl()}
                         iconColor : ${it.node.getIconColor()}
  """
  println '--------------'
}
