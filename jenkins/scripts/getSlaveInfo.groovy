#!/usr/bin/env groovy
// inspired from https://wiki.jenkins.io/display/JENKINS/Display+Information+About+Nodes

for (aSlave in hudson.model.Hudson.instance.slaves) {
  println('====================');
  println('Name: ' + aSlave.name);
  println('getLabelString: ' + aSlave.getLabelString());
  println('getNumExectutors: ' + aSlave.getNumExecutors());
  println('getRemoteFS: ' + aSlave.getRemoteFS());
  println('getMode: ' + aSlave.getMode());
  println('getRootPath: ' + aSlave.getRootPath());
  println('getDescriptor: ' + aSlave.getDescriptor());
  println('getComputer: ' + aSlave.getComputer());
  println('\tcomputer.isAcceptingTasks: ' + aSlave.getComputer().isAcceptingTasks());
  println('\tcomputer.isLaunchSupported: ' + aSlave.getComputer().isLaunchSupported());
  println('\tcomputer.getConnectTime: ' + aSlave.getComputer().getConnectTime());
  println('\tcomputer.getDemandStartMilliseconds: ' + aSlave.getComputer().getDemandStartMilliseconds());
  println('\tcomputer.isOffline: ' + aSlave.getComputer().isOffline());
  println('\tcomputer.countBusy: ' + aSlave.getComputer().countBusy());
  //if (aSlave.name == 'NAME OF NODE TO DELETE') {
  //  println('Shutting down node!!!!');
  //  aSlave.getComputer().setTemporarilyOffline(true,null);
  //  aSlave.getComputer().doDoDelete();
  //}
  println('\tcomputer.getLog: ' + aSlave.getComputer().getLog());
  println('\tcomputer.getBuilds: ' + aSlave.getComputer().getBuilds());
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
