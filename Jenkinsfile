void setBuildStatus(String message, String state) {
    step([
        $class: 'GitHubCommitStatusSetter',
        repoSource: [$class: "ManuallyEnteredRepositorySource", url: env.GIT_URL],
        contextSource: [
            $class: 'ManuallyEnteredCommitContextSource',
            context: 'ci/jenkins/linting-and-unit-tests'
        ],
        statusResultSource: [
            $class: 'ConditionalStatusResultSource',
            results: [[$class: "AnyBuildResult", message: message, state: state]]
        ]
    ])
}

pipeline {
    agent {
	label 'k8s-maven'
    }

    stages {
        stage('Checkout') {
            steps {
                setBuildStatus("Build in progress", "PENDING")
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
        }

        stage('Package') {
            steps {
                echo 'Packaging the application...'
                sh 'mvn package -DskipTests'
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
        }
        success {
            echo 'Pipeline succeeded! ✓'
            setBuildStatus("Build succeeded", "SUCCESS");
        }
        failure {
            echo 'Pipeline failed! ✗'
            setBuildStatus("Build failed", "FAILURE");
        }
    }
}
