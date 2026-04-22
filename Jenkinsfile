pipeline {
  // Jenkins is running in Docker. This pipeline uses the Docker daemon to:
  // - build/test using a Maven image (no Maven install needed)
  // - build an app image
  // - run the app as a long-lived container on port 8081
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
  }

  environment {
    APP_NAME = "java-program"
    JAR_GLOB = "target/*.jar"
    APP_PORT = "8081"
    APP_IMAGE = "java-program"
    DOCKER_MAVEN_IMAGE = "maven:3.9.9-eclipse-temurin-21"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & test') {
      steps {
        sh '''
          set -euo pipefail
          docker version
          docker run --rm \
            -v "$PWD":/workspace -w /workspace \
            -v "$HOME/.m2":/root/.m2 \
            "${DOCKER_MAVEN_IMAGE}" \
            mvn -B -ntp clean test
        '''
      }
    }

    stage('Package') {
      steps {
        sh '''
          set -euo pipefail
          docker run --rm \
            -v "$PWD":/workspace -w /workspace \
            -v "$HOME/.m2":/root/.m2 \
            "${DOCKER_MAVEN_IMAGE}" \
            mvn -B -ntp -DskipTests package
        '''
        archiveArtifacts artifacts: "${JAR_GLOB}", fingerprint: true
      }
    }

    stage('Build Docker image') {
      when { branch 'main' }
      steps {
        sh '''
          set -euo pipefail
          docker build -t "${APP_IMAGE}:${GIT_COMMIT}" -t "${APP_IMAGE}:latest" .
        '''
      }
    }

    stage('Deploy (Docker on Jenkins host)') {
      when { branch 'main' }
      steps {
        sh '''
          set -euo pipefail

          # Replace running container (if any)
          docker rm -f "${APP_NAME}" >/dev/null 2>&1 || true

          docker run -d --restart unless-stopped \
            --name "${APP_NAME}" \
            -e SERVER_PORT="${APP_PORT}" \
            -p "${APP_PORT}:${APP_PORT}" \
            "${APP_IMAGE}:latest"
        '''
      }
    }

    stage('Verify (health check)') {
      when { branch 'main' }
      steps {
        sh '''
          set -euo pipefail
          # Run curl in a container using the host network (Linux Docker host)
          docker run --rm --network host curlimages/curl:8.10.1 \
            -fsS "http://localhost:${APP_PORT}/actuator/health"
        '''
      }
    }
  }

  post {
    always {
      junit 'target/surefire-reports/*.xml'
    }
  }
}

