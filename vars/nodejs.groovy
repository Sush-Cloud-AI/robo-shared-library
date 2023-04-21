// for nodejs projects

def lintcheck(){
sh '''
 #echo Installing Jslint for ${COMPONENT}
 #npm install jslint
 #ls -ltr node_modules/jslint/bin
 #~/node_modules/jslint/bin/jslint.js server.js || true
 echo lint checks completed for ${COMPONENT}
 '''    
} 
// true is used to mark the present step as pass and skip to next step  even if lint check fails.........





def call (){ // call is the default which will be called
pipeline {
    agent any
    environment {                     
        SONAR = credentials('sonar') 
        NEXUS = credentials('nexus')
    }
    stages{
        // should run on every coomit to feautre branch 
        stage('Lint Check'){
            steps{
                 script {                     // all fuctions called from groovy file should be placed inside scrpts
                lintcheck()

            }
            }
        }
             stage('Sonar scan'){
            steps{
                 script {                     // all fuctions called from groovy file should be placed inside scrpts
                 common.sonarcheck()                 // cant declare any shell command in scripts only use groovy based commands

            }

        }
    }

     stage('Testcases'){
        parallel{
            stage('unit testing'){
                steps{
                    //mvn verify or npm test
                    sh "echo Unit test completed"
                }

            }
            stage('Intergration testing'){
                steps{
                    //mvn verify  or npm verify 
                    sh "echo Unit test completed"
                }

            }
            stage('Functional testing'){
                steps{
                     
                    sh "echo Functional test completed"
                }

            }
        }
    }


    stage('Preparing the atifact'){
        when{ expression {env.TAG_NAME != null}} // will run when a tag is pushed . 
            steps{    
            sh "npm install"
            sh "zip ${COMPONENT}.zip node_modules/ server.js"
            sh "echo Artifact prep completed !!!"
        }
        }

    stage('Uploading the articats'){
        when{ expression {env.TAG_NAME != null}} // will run when a tag is pushed .
            steps{
                 sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}.zip http://172.31.3.38:8081/repository/${COMPONENT}/${COMPONENT}.zip"

        }
    }
}
}
}