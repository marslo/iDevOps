#!/usr/bin/env bash

## usage:
## bash build.sh [ -t '2.426.3-lts-jdk11' ] [ --rm ]

declare revsion
declare image
declare validTags
declare remoteClr='false'
declare tag='2.426.3-lts-jdk11'

while [[ $# -gt 0 ]]; do
  case "$1" in
    -t | --tag ) tag="$2"         ; shift 2 ;;
          --rm ) remoteClr='true' ; shift   ;;
             * ) break                      ;;
  esac
done


validTags=$(while read -r _i; do
              curl -sSgk "https://registry.hub.docker.com/v2/repositories/jenkins/jenkins/tags?&page=${_i}&page_size=100" |
                   jq -r '.results[] | select( .name | contains("-lts-") ) | .name';
            done < <(seq 10)
           )
# shellcheck disable=SC2001
if ! grep --fixed-strings "${tag}" <<< "${validTags}" >/dev/null; then
  echo -e "\033[1;31mERROR: tag \`${tag}\` is not exists\033[0m\n.. valid tags are:\n$(sed 's/^/\t- /g' <<< "${validTags}") "
  exit 1
fi

# build
revsion="$(git rev-parse --short HEAD)"
revCount="$(git rev-list --count "${revsion}" --first-parent)"
version="v${revCount}-${revsion}"
image='artifactory.marvell.com/storage-ff-docker-local/devops/jenkins'
docker build --no-cache \
             --force-rm \
             --build-arg TAG="${tag}" \
             -t "${image}:${tag}-${version}" \
             -f devops-jenkins .

# clean remote image if necessary
[[ 'true' = "${remoteClr}" ]] &&
  command -v jf >/dev/null &&
  jf rt delete --quiet "${image}/${tag}-${version}"/**

# publish
docker push "${image}:${tag}-${version}"
echo -e ">> \033[33;3m${image}:${tag}-${version}\033[0m has been published."

# vim:tabstop=2:softtabstop=2:shiftwidth=2:expandtab:filetype=sh:foldmethod=marker
