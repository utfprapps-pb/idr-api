pipeline {
    agent any

    environment {
        SPRING_PROFILES_ACTIVE="prod"
        POSTGRESQL_CRED = credentials('postgres-id')
        DB_JDBC_USER = "${POSTGRESQL_CRED_USR}"
        DB_JDBC_PASSWORD = "${POSTGRESQL_CRED_PSW}"
        SERVER_PORT=8500
        DATABASE_URL="jdbc:postgresql://postgresql:5432/idr"
        DATABASE_USERNAME="${POSTGRESQL_CRED_USR}"
        DATABASE_PASSWORD="${POSTGRESQL_CRED_PSW}"
    }

    stages {   
        stage('Docker Compose UP') {
            steps {
                sh 'docker compose up -d --build'
            }
        }
    }
}
