import org.csanchez.jenkins.plugins.kubernetes.KubernetesComputer
import io.fabric8.kubernetes.api.model.Container
import io.fabric8.kubernetes.api.model.EnvVar
import io.fabric8.kubernetes.api.model.VolumeMount

String sep    = ' ' * 16
String subsep = ' ' * 20

jenkins.model.Jenkins.instance.computers.findAll{ it instanceof KubernetesComputer && it.isOnline()}.each { computer ->
  println """
         name : ${computer.getDisplayName()}
       images : ${computer.getContainers().collect{ it.image }.join(', ')}
           os : ${computer.getOSDescription()}
  isJnlpAgent : ${computer.isJnlpAgent()}
         jobs : ${computer.allExecutors.collect { it.currentWorkUnit?.work?.runId ?: '' }.join(', ') ?: ''}
          env : ${computer.containers.collect{ it.getEnv() }.flatten().collect{ "${it.name} : ${it.value}" }.join( '\n' + sep )}
    resources : limits :
                    ${computer.containers.collect{ it.getResources().getLimits() }?.first().collect{ "${it.key} : ${it.value}"}.join( '\n' + subsep ) ?: ''}
                requests :
                    ${computer.containers.collect{ it.getResources().getRequests() }?.first().collect{ "${it.key} : ${it.value}"}.join( '\n' + subsep ) ?: ''}
       volume : ${computer.containers.collect { it.getVolumeMounts() }.flatten().collect{ "${it.name} : ${it.mountPath}" }.join( '\n' + sep )}
     commands : ${computer.containers.collect{ it.getCommand() }.join(', ')}
         agrs : ${computer.containers.collect{ it.getArgs() }.join(', ')}
   workingDir : ${computer.containers.collect{ it.getWorkingDir() }.join()}
      message : ${computer.containers.collect{ it.getTerminationMessagePath()}.join()}
  isLaunching : ${computer.isLaunching()}
     isOnline : ${computer.isOnline()}
  """
}
