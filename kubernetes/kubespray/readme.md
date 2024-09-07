<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [cluster bootstrap](#cluster-bootstrap)
- [grafana](#grafana)
- [kubernetes-dashboard](#kubernetes-dashboard)
  - [ingress](#ingress)
  - [RBAC](#rbac)
  - [grafana](#grafana-1)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## cluster bootstrap

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

## grafana

```bash
$ helm repo add grafana https://grafana.github.io/helm-charts
$ helm repo list
NAME                    URL
kubernetes-dashboard    https://kubernetes.github.io/dashboard/
ingress-nginx           https://kubernetes.github.io/ingress-nginx
grafana                 https://grafana.github.io/helm-charts

$ helm repo update
$ helm search repo grafana/grafana

$ helm install grafana grafana/grafana --namespace monitoring --create-namespace
```

## kubernetes-dashboard
### ingress
```bash
---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: kubernetes-dashboard
  namespace: kube-system
  annotations:
    kubernetes.io/ingress.class: "nginx"
    nginx.ingress.kubernetes.io/backend-protocol: "HTTPS"
    nginx.ingress.kubernetes.io/secure-backends: "true"
spec:
  ingressClassName: nginx
  tls:
  - hosts:
    - sms-k8s-dashboard.sample.com
    secretName: sample-tls
  rules:
    - host: sms-k8s-dashboard.sample.com
      http:
        paths:
        - path: /
          backend:
            service:
              # or kubernetes-dashboard-kong-proxy for latest version
              name: kubernetes-dashboard
              port:
                number: 443
          pathType: Prefix
```
### [RBAC](https://gist.github.com/s-lyn/3aba97628c922ddc4a9796ac31a6df2d)
- clusterrole
  ```bash
  $ cat << EOF | kubectl apply -f -
  apiVersion: rbac.authorization.k8s.io/v1
  kind: ClusterRole
  metadata:
    labels:
      k8s-app: kubernetes-dashboard
    name: kubernetes-dashboard
  rules:
  - apiGroups:
    - '*'
    resources:
    - '*'
    verbs:
    - '*'
  EOF
  ```

- clusterrolebinding
  ```bash
  $ cat << EOF | kubectl apply -f -
  apiVersion: rbac.authorization.k8s.io/v1
  kind: ClusterRoleBinding
  metadata:
    name: kubernetes-dashboard
  roleRef:
    apiGroup: rbac.authorization.k8s.io
    kind: ClusterRole
    name: kubernetes-dashboard
  subjects:
  - kind: ServiceAccount
    name: kubernetes-dashboard
    namespace: kube-system
  EOF
  ```

- serviceaccount
  ```bash
  $ cat << EOF | kubectl apply -f -
  apiVersion: v1
  kind: ServiceAccount
  metadata:
    labels:
      k8s-app: kubernetes-dashboard
    name: kubernetes-dashboard
    namespace: kube-system
  EOF
  ```

- generate token
  ```bash
  $ kubectl -n kube-system create token kubernetes-dashboard
  ey**********************WAA
  ```

### grafana

```bash
$ helm repo add grafana https://grafana.github.io/helm-charts
$ helm repo list
NAME                    URL
kubernetes-dashboard    https://kubernetes.github.io/dashboard/
ingress-nginx           https://kubernetes.github.io/ingress-nginx
grafana                 https://grafana.github.io/helm-charts

$ helm repo update
$ helm search repo grafana/grafana

$ helm install grafana grafana/grafana --namespace monitoring --create-namespace
```
