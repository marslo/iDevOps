//===========================================================================
//    FileName: builtInStage.groovy
//      Author: marslo.jiao@gmail.com
//     Created: 2019-05-05 00:07:55
//  LastChange: 2021-07-27 16:28:36
//===========================================================================

String str = "this is string A"

def stageA( String bPath ) {
  stage('scenario A') {
    echo 'a'
    sh "touch a.groovy"
    println bPath
  }
}

def stageB() {
  stage('scenario B') {
    echo 'b'
    println str
    echo "${str}"
  }
}

def stageC() {
  stage('scenario C') {
    def strC = "this is string C"
    echo 'c'
    sh "ls -altrh ${WORKSPACE}"
    sh "echo ${strC}"
    println strC
  }
}

def runAllStages( String basePath ) {
  stageA( basePath )
  stageB()
  stageC()
}

return this
