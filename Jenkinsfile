pipeline {
    agent any

    // 트리거 추가
    triggers {
        githubPush()
    }

    environment {
        REGISTRY = 'ghcr.io'
        IMAGE_NAME = 'kmkhm/my-java-image'
        GHCR_CREDENTIALS = credentials('ghcr-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "✅ 소스 코드 체크아웃 완료"
            }
        }

        stage('Build') {
            steps {
                sh '''
                    echo "🔨 Gradle 빌드 시작"
                    chmod +x ./gradlew
                    ./gradlew clean build
                    echo "✅ 빌드 완료"
                '''
            }
        }

        stage('Test') {
            steps {
                sh '''
                    echo "🧪 테스트 실행"
                    ./gradlew test
                    echo "✅ 테스트 완료"
                '''
            }
            post {
                always {
                    // testResultsPattern → testResults로 수정
                    junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    def imageTag = "${BUILD_NUMBER}"
                    def imageName = "${REGISTRY}/${IMAGE_NAME}:${imageTag}"

                    sh """
                        echo "🐳 Docker 이미지 빌드 시작"
                        docker build -t ${imageName} .
                        docker tag ${imageName} ${REGISTRY}/${IMAGE_NAME}:latest
                        echo "✅ Docker 이미지 빌드 완료: ${imageName}"
                    """
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    def imageTag = "${BUILD_NUMBER}"
                    def imageName = "${REGISTRY}/${IMAGE_NAME}:${imageTag}"

                    sh """
                        echo "📦 GHCR에 이미지 푸시 시작"
                        echo \$GHCR_CREDENTIALS_PSW | docker login ${REGISTRY} -u \$GHCR_CREDENTIALS_USR --password-stdin
                        docker push ${imageName}
                        docker push ${REGISTRY}/${IMAGE_NAME}:latest
                        echo "✅ 이미지 푸시 완료!"
                        echo "📋 배포 명령어:"
                        echo "   kubectl rollout restart deployment cicd-test-app -n backend"
                    """
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
          slackSend(
            channel: '#deployment',
            message: "빌드 성공! - ${env.JOB_NAME}",
            color: 'good'
          )
        }
        failure {
          slackSend(
            channel: '#deployment',
            message: "빌드 실패 - ${env.JOB_NAME}",
            color: 'danger'
          )
        }
    }
}