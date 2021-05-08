#!/usr/bin/env groovy
// inspired from https://wiki.jenkins.io/display/JENKINS/Display+Information+About+Nodes

for (agent in hudson.model.Hudson.instance.slaves) {
  println('====================');
  println('Name: ' + agent.name);
  println('getLabelString: ' + agent.getLabelString());
  println('getNumExectutors: ' + agent.getNumExecutors());
  println('getRemoteFS: ' + agent.getRemoteFS());
  println('getMode: ' + agent.getMode());
  println('getRootPath: ' + agent.getRootPath());
  println('getDescriptor: ' + agent.getDescriptor());
  println('getComputer: ' + agent.getComputer());
  println('\tcomputer.isAcceptingTasks: ' + agent.getComputer().isAcceptingTasks());
  println('\tcomputer.isLaunchSupported: ' + agent.getComputer().isLaunchSupported());
  println('\tcomputer.getConnectTime: ' + agent.getComputer().getConnectTime());
  println('\tcomputer.getDemandStartMilliseconds: ' + agent.getComputer().getDemandStartMilliseconds());
  println('\tcomputer.isOffline: ' + agent.getComputer().isOffline());
  println('\tcomputer.countBusy: ' + agent.getComputer().countBusy());
  //if (agent.name == 'NAME OF NODE TO DELETE') {
  //  println('Shutting down node!!!!');
  //  agent.getComputer().setTemporarilyOffline(true,null);
  //  agent.getComputer().doDoDelete();
  //}
  println('\tcomputer.getLog: ' + agent.getComputer().getLog());
  println('\tcomputer.getBuilds: ' + agent.getComputer().getBuilds());
}


// result:
// ====================
// Name: vslave-021459-tf6x8-r1vm7
// getLabelString: vslave-021459
// getNumExectutors: 1
// getRemoteFS:
// getMode: EXCLUSIVE
// getRootPath: null
// getDescriptor: org.csanchez.jenkins.plugins.kubernetes.KubernetesSlave$DescriptorImpl@183fadb8
// getComputer: KubernetesComputer name: vslave-021459-tf6x8-r1vm7 slave: KubernetesSlave name: vslave-021459-tf6x8-r1vm7
  // computer.isAcceptingTasks: false
  // computer.isLaunchSupported: true
  // computer.getConnectTime: 1571823195580
  // computer.getDemandStartMilliseconds: 1571822100331
  // computer.isOffline: true
  // computer.countBusy: 0
  // computer.getBuilds: []
// ====================
// Name: vslave-022944-ggkmb-vcrtj
// getLabelString: vslave-022944
// getNumExectutors: 1
// getRemoteFS:
// getMode: EXCLUSIVE
// getRootPath: null
// getDescriptor: org.csanchez.jenkins.plugins.kubernetes.KubernetesSlave$DescriptorImpl@183fadb8
// getComputer: KubernetesComputer name: vslave-022944-ggkmb-vcrtj slave: KubernetesSlave name: vslave-022944-ggkmb-vcrtj
  // computer.isAcceptingTasks: false
  // computer.isLaunchSupported: true
  // computer.getConnectTime: 1571823245088
  // computer.getDemandStartMilliseconds: 1571822985397
  // computer.isOffline: true
  // computer.countBusy: 0
  // computer.getBuilds: []
// ====================

// vim: ft=Jenkinsfile ts=2 sts=2 sw=2 et
