// for nodejs projects

def lintcheck(){
sh '''
 echo Installing Jslint for ${COMPONENT}
 npm install jslint
 ls -ltr node_modules/jslint/bin
 ~/node_modules/jslint/bin/jslint.js server.js || true
 '''    
} 
// true is used to mark the present step as pass and skip to next step  even if lint check fails.........

def sonarcheck() {
    sh '''
    mvn clean compile
    sonar-scanner -Dsonar.host.url=http://172.31.18.231:9000 -Dsonar.sources=. -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} -Dsonar.projectKey=${COMPONENT} 
    curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > /tmp/quality-gate.sh 
    chmod +x /tmp/quality-gate.sh && /tmp/quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.18.231 ${COMPONENT}
    '''
} 
// not required to compile for nodejs 




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
                 sonarcheck()                 // cant declare any shell command in scripts only use groovy based commands

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