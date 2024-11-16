pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'spring-boot-app:latest'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from your repository
                git 'https://github.com/Animesh0203/devOpsProjects.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image using your Dockerfile
                    sh 'docker build -t ${DOCKER_IMAGE} .'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Run the tests in the Docker container
                    sh '''
                    docker run --rm ${DOCKER_IMAGE} mvn test
                    '''
                }
            }
        }

        stage('Run Application') {
            steps {
                script {
                    // Run the Spring Boot application in the Docker container
                    sh 'docker run -d -p 8080:8080 ${DOCKER_IMAGE}'
                }
            }
        }

        stage('Cleanup') {
            steps {
                script {
                    // Stop and remove any containers created by the 'Run Application' stage
                    sh 'docker ps -q --filter "ancestor=${DOCKER_IMAGE}" | xargs docker stop || true'
                    sh 'docker ps -a -q --filter "ancestor=${DOCKER_IMAGE}" | xargs docker rm || true'
                    // Optionally remove the Docker image if you no longer need it
                    sh 'docker rmi ${DOCKER_IMAGE}'
                }
            }
        }
    }

    post {
        always {
            // Clean up Docker resources
            sh 'docker system prune -f'
        }

        success {
            echo 'Pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed!'
        }
    }
}
