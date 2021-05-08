String agentName = 'marslo-test'
Jenkins.instance.computers.findAll { computer ->
  agentName == computer.name
}.each { computer ->
  println """
    ${computer.getClass()}
    ~~> ${computer.displayName} :
                  online? : ${computer.online}
              connectTime : ${computer.connectTime}
       offlineCauseReason : ${computer.offlineCauseReason}
                 executor : ${computer.numExecutors}
                    class : ${computer.class.superclass?.simpleName}
               properties : ${computer.getSystemProperties()}
                     logs : ${computer.log}
  """
}

/**
 * Result
 *     class hudson.slaves.SlaveComputer
 *     ~~> marslo-test :
 *                   online? : false
 *               connectTime : 1620462807200
 *        offlineCauseReason : This agent is offline because Jenkins failed to launch the agent process on it.
 *                  executor : 1
 *                     class : Computer
 *                properties : [N/A:N/A]
 *
 *
 * Result: [hudson.slaves.SlaveComputer@5cf6c127]
 *
**/
