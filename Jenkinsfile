pipeline {
    agent any

    tools {
        jdk 'jdk17'   // Ensure JDK17 is available for Maven
        maven 'maven3' // Ensure Maven3 is available
    }

    environment {
        DOCKER_IMAGE = "spring-boot-app"
        DOCKER_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Code Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Animesh0203/devOpsProjects'
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                dependencyCheck additionalArguments: '--scan ./ --format HTML', odcInstallation: 'db-check'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }

        stage('Sonarqube Analysis') {
            steps {
                sh ''' mvn sonar:sonar \
                    -Dsonar.host.url=http://localhost:9000/ \
                    -Dsonar.login=squ_9bd7c664e4941bd4e7670a88ed93d68af40b42a3 '''
            }
        }

        stage('Clean & Package') {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }

        stage('Vulnerability Scanning') {
            steps {
                sh "trivy image abdeod/${DOCKER_IMAGE}:${DOCKER_TAG}"
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    // Stop any running container with the same name
                    sh "docker stop ${DOCKER_IMAGE} || true"
                    sh "docker rm ${DOCKER_IMAGE} || true"

                    // Run the Docker container
                    sh "docker run -d --name ${DOCKER_IMAGE} -p 8080:8080 abdeod/${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }
    }

    post {
        always {
            // Clean up Docker images after build
            sh "docker rmi abdeod/${DOCKER_IMAGE}:${DOCKER_TAG} || true"
        }
    }
}
