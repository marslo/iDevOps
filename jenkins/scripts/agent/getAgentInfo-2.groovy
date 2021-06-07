import java.util.*
import java.text.SimpleDateFormat

SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
Calendar calendar = Calendar.getInstance()
String agentName  = 'marslo-test'

Jenkins.instance.computers.findAll { computer ->
  agentName == computer.name
}.each { computer ->
  calendar.setTimeInMillis(computer.connectTime)
  String moreinfo = computer.online
                      ? "properties : ${computer.getSystemProperties().collect { k, v -> "$k=$v" }.join('\n\t\t\t>>> ')}"
                      : "      logs : ${computer.getLog()}"

  println """
    ~~> ${computer.displayName} :
                    class : ${computer.getClass()}
                    class : ${computer.class.superclass?.simpleName}
                  online? : ${computer.online}
              description : ${computer.description}
              connectTime : ${simpleDateFormat.format(calendar.getTime())}
       offlineCauseReason : ${computer.offlineCauseReason}
                 executor : ${computer.numExecutors}
               ${moreinfo}
  """
}



/**

 * Result
 *     ~~> marslo-test :
 *                     class : class hudson.slaves.SlaveComputer
 *                     class : Computer
 *                   online? : false
 *               description : marslo test agent offline
 *               connectTime : 2021-05-08 06:42:08
 *        offlineCauseReason : This agent is offline because Jenkins failed to launch the agent process on it.
 *                  executor : 1
 *                      logs : SSHLauncher{host='1.2.3.4', port=22, credentialsId='DevOpsSSHCredential', jvmOptions='', javaPath='', prefixStartSlaveCmd='', suffixStartSlaveCmd='', launchTimeoutSeconds=30, maxNumRetries=5, retryWaitTime=30, sshHostKeyVerificationStrategy=hudson.plugins.sshslaves.verifiers.NonVerifyingKeyVerificationStrategy, tcpNoDelay=true, trackCredentials=true}
 * [05/08/21 06:42:08] [SSH] Opening SSH connection to 1.2.3.4:22.
 * connect timed out
 * ...
 *
**/
