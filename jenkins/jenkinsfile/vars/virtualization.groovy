// ===========================================================================
//     FileName: virtualization.groovy
//       Author: marslo.jiao@gmail.com
//      Created: 2019-06-19 13:43:50
//   LastChange: 2019-06-21 15:13:20
// ===========================================================================

def wpCompilerPod(String podLabel, String imageLink code) {
  podTemplate(
    label: podLabel,
    cloud: 'DevOps Kubernetes',
    yaml: """
      apiVersion: v1
      kind: Pod
      metadata:
        labels:
          label: wp
      spec:
        hostNetwork: true
        nodeSelector:
          project: wp
        containers:
        - name: jnlp
          image: ${imageLink}
          workingDir: /home/devops
          tty: true
          volumeMounts:
          - name: docker-sock
            mountPath: /var/run/docker.sock
          - name: docker
            mountPath: /usr/bin/docker
          - name: armcc-lic
            mountPath: /opt/armcc/armcc.lic
        volumes:
        - name: armcc-lic
          persistentVolumeClaim:
            claimName: armcc-pvc
        - name: docker-sock
          mountPath: /var/run/docker.sock
        - name: docker
          mountPath: /usr/bin/docker
    """
  ) {
    code()
  }
}
