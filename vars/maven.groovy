// for java based services // shipping //

// scripted pipeline

def call(){
    node{
        common.lintchecks()
        common.sonarcheck()
        common.testcases()
    }
}

























// // Declearative pipeline 
// def lintcheck(){
// sh '''
//  #echo Lint check starting for  ${COMPONENT}
//  #mvn checkstyle:check || true
//  echo Lint check completed for  ${COMPONENT}
//  '''    
// } 
// // true is used to mark the present step as pass and skip to next step  even if lint check fails.........
// // mvb checkstyle: check is the tool for lint check for java services

// // sonar check placed inside coommon



// def call (){ // call is the default which will be called
// pipeline {
//     agent any
//     environment {                     
//         SONAR = credentials('sonar') 
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
//                  common.javasonarcheck()

//             }

//         }
//     }

//     stage('Testcases'){
//         parallel{
//             stage('unit testing'){
//                 steps{
//                     //mvn verify or npm test
//                     sh "echo Unit test completed "
//                 }

//             }
//             stage('Intergration testing'){
//                 steps{
//                     //mvn verify  or npm verify 
//                     sh "echo Unit test completed "
//                 }

//             }
//             stage('Functional testing'){
//                 steps{
                     
//                     sh "echo Functional test completed"
//                 }

//             }
//         }
//     }

//     stage('build'){
//             steps{
//                  sh "echo This a build stage"

//         }
//     }
// }
// }
// }


    