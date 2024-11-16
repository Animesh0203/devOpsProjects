pipeline {
    agent any
    
    environment {
        // Set your Docker image for Maven and Java
        DOCKER_IMAGE = 'maven:3.8.1-openjdk-11'  // A Docker image that includes Maven and OpenJDK 11
    }
    
    stages {
        stage('Clone Repository') {
            steps {
                // Clone your GitHub repository
                git url: 'https://github.com/Animesh0203/devOpsProjects.git', branch: 'main'  // Replace with your repository URL
            }
        }
        
        stage('Build & Test with Docker') {
            steps {
                script {
                    // Use Docker to run Maven clean and test
                    sh """
                        docker run --rm -v //c/ProgramData/Jenkins/.jenkins/workspace/Docker:/usr/src/mymaven -w /usr/src/mymaven $DOCKER_IMAGE mvn clean test
                    """
                }
            }
        }
        
        stage('Post-Build Actions') {
            steps {
                // Archive test results if needed (adjust path if necessary)
                junit '**/target/test-*.xml'  // Adjust for your test report location
            }
        }
    }
    
    post {
        always {
            // This will run after every build, regardless of success or failure
            echo 'Cleaning up resources'
        }
        success {
            // This will run only if the build succeeds
            echo 'Build and tests passed!'
        }
        failure {
            // This will run if the build fails
            echo 'Build failed. Check the logs!'
        }
    }
}
