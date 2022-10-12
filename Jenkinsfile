pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                withMaven {
                    sh "mvn validate"
                    sh "mvn compile"
                }
            }
        }

        stage('Test') {
            steps {
                withMaven {
                    sh "mvn test"
                    }
            }
        }

        stage('Deploy') {
            steps {
                sh 'sudo docker kill $(sudo docker ps -q)'
                sh 'sudo docker rmi backend -f'
                withMaven {
                    sh 'mvn clean package'
                }
                sh 'sudo docker build -t backend .'
                sh 'sudo docker run -d -p 8081:8081 backend'
            }
        }

    }
}