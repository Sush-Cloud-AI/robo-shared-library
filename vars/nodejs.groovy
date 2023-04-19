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






def call (){ // call is the default which will be called
pipeline {
    agent any
    stages{
        // should run on every coomit to feautre branch 
        stage('Lint Check'){
            steps{
                 script {                     // all fuctions called from groovy file should be placed inside scrpts
                lintcheck()

            }
             

        }
    }
}
}
}