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
    '''

} 




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


    