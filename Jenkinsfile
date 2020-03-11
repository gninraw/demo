pipeline {

    agent any

    tools {
        maven 'Maven 3.6.3'
        jdk 'jdk 8'
    }

    stages {
        stage('Clone repository') {
            steps {
                slackSend(color: '#00b159', message: "Build Started - '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
                git(credentialsId: 'github-ssh', url: 'git@github.com:rubyon/demo.git', branch: "${env.BRANCH_NAME}", changelog: true, poll: true)
            }
        }

        stage('Build & Push image') {
            parallel {
                stage('master') {
                    when {
                        branch 'master'
                    }
                    steps {
                        sh 'mvn clean package docker:build'
                    }
                }
                stage('dev') {
                    when {
                        branch 'dev'
                    }
                    steps {
                        sh 'sed    "s#demo#demo-dev#g" ./docker-compose.yml'
                        sh 'sed -i "s#demo#demo-dev#g" ./docker-compose.yml'
                        sh 'sed    "s#-dev:latest#:dev#g" ./docker-compose.yml'
                        sh 'sed -i "s#-dev:latest#:dev#g" ./docker-compose.yml'
                        sh 'sed    "s#demo-dev.rubyon.co.kr#demo-test.rubyon.co.kr#g" ./docker-compose.yml'
                        sh 'sed -i "s#demo-dev.rubyon.co.kr#demo-test.rubyon.co.kr#g" ./docker-compose.yml'
                        sh 'sed    "s#demo-db#demo-dev-db#g" ./src/main/resources/application.properties'
                        sh 'sed -i "s#demo-db#demo-dev-db#g" ./src/main/resources/application.properties'                        
                        sh 'sed    "s#latest</image#dev</image#g" ./pom.xml'
                        sh 'sed -i "s#latest</image#dev</image#g" ./pom.xml'
                        sh 'mvn clean package docker:build'
                    }
                }
            }
        }
        stage('Run') {
            steps {
                sh 'docker-compose down'
                sh 'docker-compose up -d'
            }
        }

        stage('Complete') {
            steps {
                script {
                    try {
                        sh 'docker image prune -f'
                    } catch (e) {}
                }
                deleteDir()
            }
        }

    }

    post {
        success {
            slackSend(color: '#439FE0', message: "Build Completed - '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            slackSend(color: '#FF0000', message: "Build Failed - '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
    }

    environment {
        IMAGE_NAME = 'hub.rubyon.co.kr/demo'
    }

}