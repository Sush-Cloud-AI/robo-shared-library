// for nodejs projects
// scripted pipeline


def call(){
    node{
        git branch: 'main', url: "https://github.com/Sush-Cloud-AI/${COMPONENT}"
        env.APP_TYP = "nodejs"
        common.lintchecks()
        common.sonarcheck()
        common.testcases()
        if(env.TAG_NAME != null){ //will run when a tag is pushed .
            common.artifact() 
        }
    }
}





















// To make the code dry we have to use scripted piplines instead for declerative pipe lines ....
// def lintcheck(){
// sh '''
//  #echo Installing Jslint for ${COMPONENT}
//  #npm install jslint
//  #ls -ltr node_modules/jslint/bin
//  #~/node_modules/jslint/bin/jslint.js server.js || true
//  echo lint checks completed for ${COMPONENT}
//  '''    
// } 
// // true is used to mark the present step as pass and skip to next step  even if lint check fails.........





// def call (){ // call is the default which will be called
// pipeline {
//     agent any
//     environment {                     
//         SONAR = credentials('sonar') 
//         NEXUS = credentials('nexus')
//     }
//     stages{
//         // should run on every coomit to feautre branch 
//         stage('Lint Check'){
//             steps{
//                  script {                     // all fuctions called from groovy file should be placed inside scrpts
//                 lintcheck()

//             }
//             }
//         }
//              stage('Sonar scan'){
//             steps{
//                  script {                     // all fuctions called from groovy file should be placed inside scrpts
//                  common.sonarcheck()                 // cant declare any shell command in scripts only use groovy based commands

//             }

//         }
//     }

//      stage('Testcases'){
//         parallel{
//             stage('unit testing'){
//                 steps{
//                     //mvn verify or npm test
//                     sh "echo Unit test completed"
//                 }

//             }
//             stage('Intergration testing'){
//                 steps{
//                     //mvn verify  or npm verify 
//                     sh "echo Unit test completed"
//                 }

//             }
//             stage('Functional testing'){
//                 steps{
                     
//                     sh "echo Functional test completed"
//                 }

//             }
//         }
//     }
// // checking the precence of the atifact on nexus / preparing the atifact and upload should happen only wehn the artifcat is not present in nexus already 

//         stage('checking  artifactory in nexus') {
//            when{ expression {env.TAG_NAME != null}} 
//            steps{
//             script{
//                 env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl http://172.31.3.38:8081/service/rest/repository/browse/${COMPONENT}/ |grep ${COMPONENT}-${TAG_NAME}.zip || true")
//                 // true in the above line is to make the UPLOAD_STATUS to make is forecully true as it reutrns a null when curl will not have a value to print 
//                 print UPLOAD_STATUS
//             }
//            }

//         }


//     stage('Preparing the atifact'){
//         when{ 
//             expression {env.TAG_NAME != null} // will run when a tag is pushed . 
//             expression {env.UPLOAD_STATUS == ""}} // will allow upload only when the curl return null from above stage
//             steps{    
//             sh "npm install"
//             sh "zip ${COMPONENT}-${TAG_NAME}.zip node_modules/ server.js"
//             sh "echo Artifact prep completed !!!"
//         }
//         }

//     stage('Uploading the articats'){
//         when{ expression {env.TAG_NAME != null} // will run when a tag is pushed .
//             expression {env.UPLOAD_STATUS == ""}}
//             steps{
//                  sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.3.38:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
//                  // curl returns failure when failed when we use -f . 

//         }
//     }
// }
// }
// }


//declerative vs scripted


// Jenkinsfile (Declarative Pipeline)
// pipeline {
//     agent any 
//     stages {
//         stage('Build') { 
//             steps {
//                 // 
//             }
//         }
//         stage('Test') { 
//             steps {
//                 // 
//             }
//         }
//         stage('Deploy') { 
//             steps {
//                 // 
//             }
//         }
//     }
// }

// Jenkinsfile (Scripted Pipeline)
// node {  
//     stage('Build') { 
//         // 
//     }
//     stage('Test') { 
//         // 
//     }
//     stage('Deploy') { 
//         // 
//     }
// }