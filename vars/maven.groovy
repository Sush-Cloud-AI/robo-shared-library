// for java based services // shipping //


def lintcheck(){
sh '''
 echo Lint check starting for  ${COMPONENT}
 mvn checkstyle:check || true
 echo Lint check completed for  ${COMPONENT}
 '''    
} 
// true is used to mark the present step as pass and skip to next step  even if lint check fails.........
// mvb checkstyle: check is the tool for lint check for java services

def sonarcheck() {
    sh '''
    mvn clean compile
    sonar-scanner -Dsonar.host.url=http://172.31.18.231:9000 -Dsonar.sources=. -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} -Dsonar.projectKey=${COMPONENT} -Dsonar.java.binaries=target/classes/
    curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > /tmp/quality-gate.sh 
    chmod +x /tmp/quality-gate.sh && /tmp/quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.18.231 ${COMPONENT}
    '''
} 
// 1) for compile require lanugaes like java we need to compile before sonar scan
//2) ip is the sonar qube host ip with 9000 port of it and the pathof the binary should be passed after the compile
// 3) sonar scan must me installed on worker node for the scan to work
//4) for the piple to recognize if the sonar alaysis has failed we use an api which is preent in the sheel script quality gate 



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
                 sonarcheck()

            }

        }
    }
}
}
}


    