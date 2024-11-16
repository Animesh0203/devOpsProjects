pipeline {
    agent any

    tools{
        jdk 'jdk17'   // Ensure JDK17 is available for Maven
        maven 'maven3' // Ensure Maven3 is available
    }

    stages {
        stage('Code Checkout') {
            steps {
                git branch: 'main', changelog: false, poll: false, url: 'https://github.com/AbderrahmaneOd/Spring-Boot-Jenkins-CI-CD'
            }
        }
        
        stage('OWASP Dependency Check') {
            steps {
                dependencyCheck additionalArguments: '--scan ./ --format HTML ', odcInstallation: 'db-check'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh ''' 
                    mvn sonar:sonar \
                    -Dsonar.host.url=http://localhost:9000/ \
                    -Dsonar.login=squ_9bd7c664e4941bd4e7670a88ed93d68af40b42a3 
                '''
            }
        }

        stage('Clean & Package') {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }

        stage("Docker Build & Push") {
            steps {
                script {
                    // Define Docker image name and tags
                    def imageName = "spring-boot-prof-management"
                    def buildTag = "${imageName}:${BUILD_NUMBER}"
                    def latestTag = "${imageName}:latest"  // Tag for latest build
                    
                    // Build Docker image using provided Dockerfile
                    sh """
                        docker build -t ${imageName} -f Dockerfile .  // Use Dockerfile provided in your repo
                    """
                    
                    // Tag the Docker image
                    sh "docker tag ${imageName} abdeod/${buildTag}"
                    sh "docker tag ${imageName} abdeod/${latestTag}"  // Tag the image with 'latest'
                    
                    // Push both build and latest tags to Docker Hub
                    sh "docker push abdeod/${buildTag}"
                    sh "docker push abdeod/${latestTag}"  // Push the 'latest' tag
                    
                    // Set build tag as environment variable for subsequent stages
                    env.BUILD_TAG = buildTag
                }
            }
        }

        stage('Vulnerability Scanning') {
            steps {
                // Run vulnerability scan on the Docker image using Trivy
                sh "trivy image abdeod/${BUILD_TAG}"
            }
        }

        stage("Staging") {
            steps {
                // Run the application in staging using Docker Compose
                sh 'docker-compose up -d'
            }
        }
    }
}
