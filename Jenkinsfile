void setBuildStatus(String message, String state) {
    step([
        $class: 'GitHubCommitStatusSetter',
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
                //setBuildStatus("Unit testing in progress", "PENDING")
                //publishChecks conclusion: 'NEUTRAL', name: 'ci/jenkins/linting-and-unit-testing', status: 'IN_PROGRESS', summary: 'Unit tests starting...', title: 'Unit Tests'
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
                echo 'Running unit tests...'
                sh 'mvn test -Dtest="*Test,*UnitTest" -Dtest="!*IntegrationTest"'
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
            //setBuildStatus("Unit tests succeeded", "SUCCESS");
            //publishChecks name: 'ci/jenkins/linting-and-unit-testing', title: 'Unit Tests', conclusion: SUCCESS, summary: 'All unit tests passed successfully', detailsURL: env.BUILD_URL
        }
        failure {
            echo 'Pipeline failed! ✗'
            //setBuildStatus("Unit tests failed", "FAILURE");
            //publishChecks name: 'ci/jenkins/linting-and-unit-testing', title: 'Unit Tests', conclusion: FAILURE summary: 'Unit tests failed - check logs for details', detailsURL: env.BUILD_URL
        }
    }
}
