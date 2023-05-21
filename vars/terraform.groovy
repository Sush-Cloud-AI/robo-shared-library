def call(){
properties([
        parameters([
        [$class: 'ChoiceParameterDefinition',
           choices: 'dev\nprod\n',
           name: 'ENV',
           description: "Select the ENV"
        ],
        ]),
    ])


    node{
        git branch: 'main', url: "https://github.com/Sush-Cloud-AI/${REPONAME}"
        stage('Terraform init') {
            sh "terrafile -f env-${ENV}/Terrafile"
            sh "terraform init -backend-config=env-${ENV}/backend-${ENV}.tfvars"
            }

        stage('Terraform plan') {
            sh "terraform plan -var-file=env-${ENV}/${ENV}.tfvars"
            }


        stage('Terraform apply') {
            sh "terraform apply -var-file=env-${ENV}/${ENV}.tfvars -auto-approve"
            }




    }
}