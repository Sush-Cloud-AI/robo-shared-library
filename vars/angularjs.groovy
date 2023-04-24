def call(){
    node{
        git branch: 'main', url: "https://github.com/Sush-Cloud-AI/${COMPONENT}"
        env.APP_TYP = "nginx"
        common.lintchecks()
        common.sonarcheck()
        common.testcases()
        if(env.TAG_NAME != null){ //will run when a tag is pushed .
        common.artifact() 
        }
    }
}

