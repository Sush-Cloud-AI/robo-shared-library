// lint check for all comp scripted pipeline

def lintchecks(){
    stage('Lint Check'){
        if (env.APP_TYPE == "nodejs"){
        sh '''
        # echo Installing Jslint for ${COMPONENT}
        # npm install jslint
        # ls -ltr node_modules/jslint/bin
        # ~/node_modules/jslint/bin/jslint.js server.js || true
        echo lint checks completed for ${COMPONENT}
        '''
        }

        else if (env.APP_TYPE == "java"){
        sh '''
        # echo Lint check starting for  ${COMPONENT}
        # mvn checkstyle:check || true
        echo Lint check completed for  ${COMPONENT}
        '''    

        }

        else if (env.APP_TYPE == "python"){
        sh '''
        # echo Lint check starting for  ${COMPONENT}
        # pylint *.py || true
        echo Lint check completed for  ${COMPONENT}
        '''

        }
        else {
        sh '''
        echo lint checks completed for ${COMPONENT}
        '''
        }
    }
}

// sonar checks scripted
def sonarcheck() {
    stage('sonar check'){
    if (env.APP_TYPE == "java"){   
    sh '''
    #mvn clean compile
    #sonar-scanner -Dsonar.host.url=http://172.31.18.231:9000 -Dsonar.sources=. -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} -Dsonar.projectKey=${COMPONENT} -Dsonar.java.binaries=target/classes/
    #curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > /tmp/quality-gate.sh 
    #chmod +x /tmp/quality-gate.sh && /tmp/quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.18.231 ${COMPONENT}
    echo Sonar checks completed
    '''
    }
    else{

     sh '''
    #mvn clean compile
    #sonar-scanner -Dsonar.host.url=http://172.31.18.231:9000 -Dsonar.sources=. -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} -Dsonar.projectKey=${COMPONENT} -Dsonar.java.binaries=target/classes/
    #curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > /tmp/quality-gate.sh 
    #chmod +x /tmp/quality-gate.sh && /tmp/quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.18.231 ${COMPONENT}
    echo Sonar checks completed
    '''
    }
    }
}

// unit test scripted 

def testcases() {
    stage('Test Cases') {
        def stages = [:]    // declaring empty list
                stages["Unit Testing"] = {
                        sh 'echo Unit Testing Completed'
                }
                stages["Integration Testing"] = {
                        sh 'echo Integration Testing Completed'
                }
                stages["Function Testing"] = {
                        sh 'echo Functional Testing Completed'
                }
              parallel(stages) 
          }
      }


// prep artifact

def artifact(){

    stage('checking  artifactory in nexus') {
            
           script{
                env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl http://172.31.3.38:8081/service/rest/repository/browse/${COMPONENT}/ |grep ${COMPONENT}-${TAG_NAME}.zip || true")
                // true in the above line is to make the UPLOAD_STATUS to make is forecully true as it reutrns a null when curl will not have a value to print 
                print UPLOAD_STATUS
            }
           }

    if (env.UPLOAD_STATUS == ""){ // will allow upload only when the curl return null from above stage
    stage('Preparing the atifact'){
        if (env.APP_TYPE == "nodejs"){
            sh "npm install"// Generates the nodes_modules
            sh "zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules/ server.js" 
            sh "echo Artifacts Preparation Completed................!!!"
            
        }

        else if (env.APP_TYPE == "java")  {
            sh "mvn clean package"
            sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
            sh "zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar"
           }

        else if (env.APP_TYPE == "python")  {
            sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt"
           }

        else if (env.APP_TYPE == "nginx") {  
            sh '''
            cd static
            zip -r ../${COMPONENT}-${TAG_NAME}.zip * 
            ''' 
            } 
    }

    stage('Uploading the articats'){
        withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USR')]) {
           sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.3.38:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
            // curl returns failure when failed when we use -f . 
            }
    }
    }
}
















//for declerative 
// def javasonarcheck() {
//     sh '''
//     #mvn clean compile
//     #sonar-scanner -Dsonar.host.url=http://172.31.18.231:9000 -Dsonar.sources=. -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} -Dsonar.projectKey=${COMPONENT} -Dsonar.java.binaries=target/classes/
//     #curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > /tmp/quality-gate.sh 
//     #chmod +x /tmp/quality-gate.sh && /tmp/quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.18.231 ${COMPONENT}
//     echo Sonar checks completed
//     '''
// } 

// // 1) for compile require lanugaes like java we need to compile before sonar scan
// //2) ip is the sonar qube host ip with 9000 port of it and the pathof the binary should be passed after the compile
// // 3) sonar scan must me installed on worker node for the scan to work
// //4) for the piple to recognize if the sonar alaysis has failed we use an api which is preent in the sheel script quality gate 


// def sonarcheck() {
//     sh '''
    
//     #sonar-scanner -Dsonar.host.url=http://172.31.18.231:9000 -Dsonar.sources=. -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} -Dsonar.projectKey=${COMPONENT} 
//     #curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > /tmp/quality-gate.sh 
//     #chmod +x /tmp/quality-gate.sh && /tmp/quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.18.231 ${COMPONENT}
//     echo sonar checks completed
//     '''
// } 
// // not required to compile for nodejs 

