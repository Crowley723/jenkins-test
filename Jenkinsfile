pipeline {
    agent any

    tools {
        // Make sure you have Maven configured in Jenkins
        // Go to: Manage Jenkins -> Global Tool Configuration -> Maven
        maven 'Maven 3.9.5' // Update this to match your Maven installation name in Jenkins
    }

    environment {
        // Optional: set Java home if needed
        JAVA_HOME = tool 'JDK 21' // Update if you've configured a specific JDK in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // Jenkins automatically checks out the code if using SCM
                echo 'Checking out code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
            post {
                always {
                    // Publish test results
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging the application...'
                sh 'mvn package -DskipTests'
            }
        }

        stage('Archive') {
            steps {
                echo 'Archiving artifacts...'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded! ✓'
        }
        failure {
            echo 'Pipeline failed! ✗'
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}