pipeline {
    agent any

    stages {
        stage('Build Frontend') {
            steps {
                // Cambia al directorio del frontend
                dir('PrestaBanco-frontend') {
                    // Instala las dependencias y construye el proyecto
                    bat 'npm install' // Usa 'sh' si estás en un entorno Unix/Linux
                    bat 'npm run build'
                }
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                script {
                    // Construye la imagen Docker del frontend
                    bat 'docker build -t tinyantu/prestabanco-frontend:latest -f PrestaBanco-frontend/Dockerfile PrestaBanco-frontend'
                }
            }
        }

        stage('Build Backend') {
            steps {
                // Cambia al directorio del backend
                dir('PrestaBanco-backend') {
                    bat 'mvn clean package' // Asumiendo que usas Maven
                }
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                script {
                    bat 'docker build -t tinyantu/prestabanco-backend:latest -f PrestaBanco-backend/Dockerfile PrestaBanco-backend'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    bat 'docker-compose -f compose.yml up -d'
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}
