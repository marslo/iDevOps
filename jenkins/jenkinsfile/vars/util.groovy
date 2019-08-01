#!/usr/bin/env groovy
// ============================================================================
//      FileName: util.groovy
//        Author: marslo.jiao@gmail.com
//       Created: 2019-08-01 11:41:02
//    LastChange: 2019-08-01 11:41:02
// ============================================================================

ef getTimestamp() {
  def timeStamp = sh (
    returnStdout: true,
    script: 'set +x; date +"%y%m%d%H%M"'
  ).trim()
  return timeStamp
}

def getUpStreamInfo(build) {
  Map upstreamInfo = [:]
  if ( 0 == build.size ) {
    upstreamInfo["project"]   = null
    upstreamInfo["fullDesc"]  = null
    upstreamInfo["buildNum"]  = null
  } else {
    Integer upIndex = build.modCount - 1
    upstreamInfo["shortDesc"] = build[upIndex].getBuildCauses()["shortDescription"][0]
    upstreamInfo["buildNum"]  = build[upIndex].getDisplayName()
    upstreamInfo["project"]   = build[upIndex].getFullDisplayName()
    upstreamInfo["fullUrl"]   = build[upIndex].getAbsoluteUrl()
    upstreamInfo["fullDesc"]  = getHTMLLink(build[upIndex].getAbsoluteUrl(), build[upIndex].getFullDisplayName())
  }
  return upstreamInfo
}

def showDesc(String descInfo, String version = null, String testscenario = null) {
  String descStr = "${descInfo}"
  Map upInfo = getUpStreamInfo(currentBuild.upstreamBuilds)

  if (env.NODE_NAME.length() < 30) {
    descStr += " \u25C2 ${env.NODE_NAME}"
  }
  if (upInfo.fullDesc) {
    descStr = upInfo["fullDesc"] + " \u25B8 " + descStr
  }
  if (version) {
    descStr += " \u00AB ${version} "
  }
  if (testscenario) {
    descStr += "\u2219 ${testscenario}"
  }
  currentBuild.description = descStr
}

def showError(String errInfo, String version = null, String testscenario = null) {
  String errStr
  Map upInfo = getUpStreamInfo(currentBuild.upstreamBuilds)
  if (upInfo.fullDesc) {
    errStr= upInfo["fullDesc"] + " \u25B8 ${errInfo} \u25C2 ${env.NODE_NAME}"
  } else {
    errStr = "${errInfo} \u25C2 ${env.NODE_NAME}"
  }
  if (version) {
    errStr += " \u25CF ${version} "
  }
  if (testscenario) {
    errStr += "\u25CA ${testscenario}"
  }
  currentBuild.description = errStr
  error (color.show('brred', errInfo))
}

def getHTMLLink(url, text) {
  return "<a href=\"${url}\" target=\"_blank\">${text}</a>"
}

def getBuildVersion(String patten) {
  String ver = null
  try {
    verFile = findFiles(glob: patten)[0]
    ver = readFile(verFile.path)
  } catch (ArrayIndexOutOfBoundsException e) {
    ver = 'unknow-build-version'
  }
  return ver
}

def getArtifacts(String artiName, String buildJobName, String buildNumber) {
  echo "== copy artifacts from ${buildJobName} build #${buildNumber}"
  copyArtifacts               filter : artiName              ,
                fingerprintArtifacts : true                  ,
                         projectName : buildJobName          ,
                            selector : specific(buildNumber)
}

def regroupArtifacts(String customer, String nand, String socname, String curVer, String target){
  ver = getBuildVersion("*build/_out*/**/file_version.txt")
  target = "${ver}/${target}"
  if (findFiles (glob: "**/build/_out*/**/${socname}*.suffix")) {
    def tarSt = sh (
      returnStatus: true,
      script: """
        set +x
        for dname in \$(dirname \$(find build -type f -iname "*${customer}*.suffix" -o -iname "*${nand}*.suffix") | uniq); do
          tname=\$(echo \${dname} | awk -F'/' '{print \$--NF}');
          mkdir -p "${target}/\${tname}/Intermediate"
          cp -r \${dname}/* ${target}/\${tname}/
          cp -r \${dname}/../Intermediate ${target}/\${tname}/
        done
      """
    )
    return tarSt
  }
}

def showJUnitReport(String xmlPath) {
  if (findFiles (glob: xmlPath)){
    step([
                 $class : "JUnitResultArchiver",
            testResults : xmlPath,
      allowEmptyResults : false
    ])
    return true
  } else {
    println color.show('brred', "ERROR: Could not find JUnit report at ${xmlPath}")
    return false
  }
}


def setupSSHKey(String dest) {
  def keySt = sh (
    returnStatus: true,
    script: """
      set +x
      cp ~/.ssh/jenkins@devops ${dest}/jenkins@devops
    """
  )
  return keySt
}

def getImageVersion(String dockerFile) {
  def f = readFile(dockerFile).split('\n')
  String ver = ''
  f.each { line ->
    if (line.contains('ARG VERSION=')) {
      ver = line.split('=')[-1]
    }
  }
  return ver
}

def uniqLabel(String label) {
  Date now = new Date()
  return label + '-' + now.format("HHmmss", TimeZone.getTimeZone('America/Los_Angeles'))
}

@NonCPS
def addEnvVarToCurBuild(String key, String value) {
  def curBuild = currentBuild.rawBuild
  def pa = new ParametersAction()
  def actions = curBuild.getActions(hudson.model.ParametersAction)
  if (actions) {
    pa = actions[0]
    curBuild.removeAction(actions[0])
  }
  try {
    def updatedAction = pa.merge( new ParametersAction([ new StringParameterValue(key, value) ]))
    curBuild.addAction(updatedAction)
  } catch(e) {
    if (actions) {
      curBuild.addAction(actions[0])
    }
    throw e
  }
}

@NonCPS
def addEnvVarToCurBuild(Map envVars) {
  def curBuild = currentBuild.rawBuild
  def pa = new ParametersAction()
  def actions = curBuild.getActions(hudson.model.ParametersAction)
  List<StringParameterValue> paramsList = new ArrayList<StringParameterValue>();
  if (actions) {
    pa = actions[0]
    curBuild.removeAction(actions[0])
  }
  try {
    envVars.each { entry ->
        paramsList << new StringParameterValue("${entry.key}", "${entry.value.toString()}")
    }
    def updatedAction = pa.merge(new ParametersAction(paramsList))
    curBuild.addAction(updatedAction)
  } catch(e) {
    if (actions) {
      curBuild.addAction(actions[0])
    }
    throw e
  }
}

// obsolete
def tarArtifacts(String socname, String curVer, String target){
  if (findFiles (glob: "**/build/_out*/**/${socname}*.suffix")) {
    def tarSt = sh (
      returnStatus: true,
      script: """
        set +x
        tar -C \$(dirname \$(find ${WORKSPACE} -type d -maxdepth 2 -name "${target}")) -czf "${target}-${curVer}".tar.gz ${target}
      """
    )
    return tarSt
  }
}

// obsolete
def prepareArtifacts(String basePath) {
  def prepare = sh (
    returnStdout: true,
    script: """
      set +x
      /bin/ls *.tar.gz | xargs -n1 tar -C ${basePath} -xzf
      mv ${basePath}/tsb_bics4 ${basePath}/tsb
      mv ${basePath}/micron_b27 ${basePath}/micron
      for type in tsb micron; do
        destfolder="${basePath}/\${type}/"
        for suffixbin in '*a*main*.suffix'    \
                      '*b*main*.suffix'      \
                      '*c*.suffix' \
                      '*d*main.suffix'   \
                      '*legacy*.suffix'           \
                      'file_version.txt';
        do
          find "\${destfolder}" -iname "\${suffixbin}" -exec cp {} "\${destfolder}" \\;
        done
        echo "\${destfolder}"
        ls -Altrh "\${destfolder}"
      done
    """
  ).trim()
  println prepare
}


// vim: ts=2 sts=2 sw=2 et ft=Groovy
