//===========================================================================
//    FileName: built-in-stages.groovy
//      Author: marslo.jiao@gmail.com
//     Created: 2019-05-05 00:07:55
//  LastChange: 2019-06-03 16:02:48
//===========================================================================

strA = "this is string A"

def runStageA(String bPath) {
  stage('scenario A') {
    echo 'a'
    sh "touch a.groovy"
    println bPath
  }
}

def runStageB() {
  stage('scenario B') {
    echo 'b'
    println strA
    echo "${strA}"
  }
}

def runStageC() {
  stage('scenario C') {
    def strC = "this is string C"
    echo 'c'
    sh "ls -altrh ${WORKSPACE}"
    sh "echo ${strC}"
    println strC
  }
}

def runPrecommitStages(String basePath){
  runStageA(basePath)
  runStageB()
  runStageC()
}

return this
