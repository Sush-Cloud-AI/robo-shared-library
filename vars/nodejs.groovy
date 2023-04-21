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

    stage('build'){
            steps{
                 sh "echo This a build stage"

        }
    }
}
}
}