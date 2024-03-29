pipeline {
    agent any

    stages {

        stage('Code Coverage') {
            steps {
                withMaven {
                    sh 'mvn test'
                    sh 'mvn jacoco:report'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withMaven() {
                    sh "mvn clean verify sonar:sonar -Dsonar.projectKey=CATCultura -Dsonar.host.url=http://10.4.41.41:9000 -Dsonar.login=sqp_d295ab10e5d707f13b7066e17ce3624f6c0f0417"
                }
            }
          }


        stage('Build') {
            steps {
                withMaven {
                    sh "mvn validate"
                    sh "mvn compile"
                }
            }
        }

//         stage('Test') {
//             steps {
//                 withMaven {
//                     sh "mvn test"
//                     }
//             }
//         }

        stage('Deploy locally') {
            when { branch 'dev-main' }
            steps {

                withMaven {
                    sh 'mvn clean package'
                }
                sh 'sudo docker kill $(sudo docker ps -q -f ancestor=backend-staging)'
                sh 'sudo docker rmi backend-staging -f'
                sh 'sudo docker build -t backend-staging .'
                sh 'sudo docker run -d -p 8081:8081 backend-staging'
            }

        }
        stage('Deploy remotely') {
            when { branch 'master' }
                steps {

                    withMaven {
                        sh 'mvn clean package'
                    }
                    sh 'sudo docker build -t backend .'
                    sh 'sudo docker tag backend catculturacontainerhub.azurecr.io/backend'
                    sh 'sudo docker push catculturacontainerhub.azurecr.io/backend'

                }
        }

        stage('Notify') {
          steps {
            discordSend description: "Jenkins Pipeline Build", footer: "Results from a build", link: env.BUILD_URL, result: currentBuild.currentResult, title: JOB_NAME, webhookURL: "https://discord.com/api/webhooks/1029023402079572108/PSi21wQLj8EdmwAYw6DbyEsGuppRKibwV7r81QVq743lG5Z3_qZw2vNIr5jJ_sU_15RZ"
         }
        }

    }
}
