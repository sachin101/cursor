pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
  }

  environment {
    APP_NAME = "java-program"
    JAR_GLOB = "target/*.jar"
    DEPLOY_DIR = "/opt/java-program"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & test') {
      steps {
        // Requires Maven on the Jenkins agent (or configure a Docker agent).
        sh 'mvn -B -ntp clean test'
      }
    }

    stage('Package') {
      steps {
        sh 'mvn -B -ntp -DskipTests package'
        archiveArtifacts artifacts: "${JAR_GLOB}", fingerprint: true
      }
    }

    stage('Deploy (SSH)') {
      when {
        branch 'main'
      }
      steps {
        // Configure an SSH credential in Jenkins with ID 'prod-ssh' (type: "SSH Username with private key")
        // and set your server/username below.
        withCredentials([sshUserPrivateKey(credentialsId: 'prod-ssh', keyFileVariable: 'SSH_KEY', usernameVariable: 'SSH_USER')]) {
          sh '''
            set -euo pipefail
            JAR="$(ls -1 target/*.jar | head -n 1)"
            echo "Deploying $JAR"
            ssh -i "$SSH_KEY" -o StrictHostKeyChecking=no "$SSH_USER@YOUR_SERVER_HOST" "mkdir -p '${DEPLOY_DIR}'"
            scp -i "$SSH_KEY" -o StrictHostKeyChecking=no "$JAR" "$SSH_USER@YOUR_SERVER_HOST:${DEPLOY_DIR}/${APP_NAME}.jar"
            ssh -i "$SSH_KEY" -o StrictHostKeyChecking=no "$SSH_USER@YOUR_SERVER_HOST" "sudo systemctl restart ${APP_NAME} && sudo systemctl --no-pager status ${APP_NAME} | tail -n 30"
          '''
        }
      }
    }
  }

  post {
    always {
      junit 'target/surefire-reports/*.xml'
    }
  }
}

