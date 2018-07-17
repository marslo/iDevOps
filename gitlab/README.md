# Upgrade
## Upgrade to 11.x

Change `git_data_dirs({"default" => "[PATH_HERE]"})` to `git_data_dirs({"default" => { "path" => "[PATH_HERE]"}})`

E.g.:
```
$ sudo grep git_data_dirs /etc/gitlab/gitlab.rb
# git_data_dirs({"default" => "/var/opt/gitlab/git-data"})
# git_data_dirs({"default" => "/data/gitlab/git-data"})
git_data_dirs({"default" => { "path" => "/data/gitlab/git-data"}})

```

# Q&A
- [Error while upgrade to 11.0.0 (500 Error)](https://gitlab.com/gitlab-org/omnibus-gitlab/issues/3610)


# Reference
- [Storing Git data in an alternative directory](https://docs.gitlab.com/omnibus/settings/configuration.html#storing-git-data-in-an-alternative-directory)
