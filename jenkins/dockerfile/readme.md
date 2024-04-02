<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [build devops jenkins image](#build-devops-jenkins-image)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


### build devops jenkins image
```bash
$ revsion="$(git rev-parse --short HEAD)"
$ tag='2.426.3-lts-jdk11'
$ docker build --no-cache \
               --force-rm \
               --build-arg TAG="${tag}" \
               -t artifactory.domain.com/docker/devops/jenkins:"${tag}-${revsion}" \
               -f devops-jenkins .

# publish
$ docker push artifactory.domain.com/docker/devops/jenkins:"${tag}-${revsion}"
```

- or
  ```bash
  $ cd dockerfile/jenkins
  $ bash build.sh -t '2.426.3-lts-jdk11' [ --rm ]
  ```

