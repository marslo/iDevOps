if (manager.build.result.isBetterOrEqualTo(hudson.model.Result.SUCCESS)) {
  url = "http://pww.artifactory.cdi.philips.com"
  tag = "CDI Artifactory Server"
  description = "<a href='${url}' target='_blank'>${tag}</a>"
  manager.build.setDescription(description)
}
