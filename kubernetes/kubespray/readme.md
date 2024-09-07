## usage:

```bash
$ TAG_NAME='v2.26.0'

# clone kubespray
$ git clone --branch "${TAG_NAME}" https://github.com/kubernetes-sigs/kubespray.git kubespray-${TAG_NAME}

# pull docker image
$ docker pull quay.io/kubespray/kubespray:${TAG_NAME}

# copy inventory
$ cp -r inventory/sms-k8s ~/kubespary-${TAG_NAME}/inventory/

# run kubespray container
$ docker run --rm -it --mount type=bind,source="$(pwd)"/kubespray-${TAG_NAME}/inventory/sms-k8s,dst=/kubespray/inventory/sms-k8s \
         --mount type=bind,source="${HOME}"/.ssh/sms-k8s-apiservers,dst=/root/.ssh/sms-k8s-apiservers \
         quay.io/kubespray/kubespray:${TAG_NAME} bash

# deploy cluster
$ ansible-playbook -i inventory/sms-k8s/hosts.yaml \
                   --become --become-user=root \
                   --private-key /root/.ssh/sms-k8s-apiservers \
                   cluster.yml -v
```
