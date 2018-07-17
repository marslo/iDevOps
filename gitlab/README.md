<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Command:](#command)
    - [Check Healthy](#check-healthy)
    - [Check Logs](#check-logs)
    - [Reconfigure](#reconfigure)
- [Upgrade](#upgrade)
    - [Upgrade to 11.x](#upgrade-to-11x)
- [Reference](#reference)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Command:

## Check Healthy
```
$ sudo gitlab-rake gitlab:check SANITIZE=true --trace
$ sudo gitlab-rake gitlab:check --trace
$ sudo gitlab-rake db:migrate:status --trace
```

## Check Logs
```
$ sudo gitlab-ctl tail
```

## Reconfigure
```
$ sudo gitlab-ctl reconfigure
```

# Upgrade
## Upgrade to 11.x

- Error:
    ```
    Recipe Compile Error in /opt/gitlab/embedded/cookbooks/cache/cookbooks/gitlab/recipes/config.rb
    ```

- [Solution](https://gitlab.com/gitlab-org/omnibus-gitlab/issues/3610):

1. Change `git_data_dirs({"default" => "[PATH_HERE]"})` to `git_data_dirs({"default" => { "path" => "[PATH_HERE]"}})`
2. Execute `$ sudo gitlab-ctl reconfigure`

E.g.:
```
$ sudo grep -n git_data_dirs /etc/gitlab/gitlab.rb
$ sudo grep -n git_data_dirs /etc/gitlab/gitlab.rb
261:# git_data_dirs({"default" => "/var/opt/gitlab/git-data"})
264:# git_data_dirs({"default" => "/data/gitlab/git-data"})
265:git_data_dirs({"default" => { "path" => "/data/gitlab/git-data"}})

OR

$ sudo cd /etc/gitlab
$ sudo mv gitlab.rb gitlab.rb.old
$ sudo touch gitlab.rb
$ sudo gitlab-ctl reconfigure
```

# Reference
- [Configuration Options](https://docs.gitlab.com/omnibus/settings/configuration.html)
- [Storing Git data in an alternative directory](https://docs.gitlab.com/omnibus/settings/configuration.html#storing-git-data-in-an-alternative-directory)
