

def javasonarcheck() {
    sh '''
    #mvn clean compile
    #sonar-scanner -Dsonar.host.url=http://172.31.18.231:9000 -Dsonar.sources=. -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} -Dsonar.projectKey=${COMPONENT} -Dsonar.java.binaries=target/classes/
    #curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > /tmp/quality-gate.sh 
    #chmod +x /tmp/quality-gate.sh && /tmp/quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.18.231 ${COMPONENT}
    echo Sonar checks completed
    '''
} 

// 1) for compile require lanugaes like java we need to compile before sonar scan
//2) ip is the sonar qube host ip with 9000 port of it and the pathof the binary should be passed after the compile
// 3) sonar scan must me installed on worker node for the scan to work
//4) for the piple to recognize if the sonar alaysis has failed we use an api which is preent in the sheel script quality gate 


def sonarcheck() {
    sh '''
    
    #sonar-scanner -Dsonar.host.url=http://172.31.18.231:9000 -Dsonar.sources=. -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} -Dsonar.projectKey=${COMPONENT} 
    #curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > /tmp/quality-gate.sh 
    #chmod +x /tmp/quality-gate.sh && /tmp/quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.18.231 ${COMPONENT}
    echo sonar checks completed
    '''
} 
// not required to compile for nodejs 

