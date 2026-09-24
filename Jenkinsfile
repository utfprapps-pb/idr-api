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

        MINIO_CRED = credentials('dainf_labs_minio_id')
        MINIO_URL="https://minio.app.pb.utfpr.edu.br"
        MINIO_BUCKET="idr-dev"
        MINIO_ACCESS_KEY="${MINIO_CRED_USR}"
        MINIO_SECRET_KEY="${MINIO_CRED_PSW}"
        MINIO_SECURE=true
        MINIO_PORT=443
    }

    stages {   
        stage('Docker Compose UP') {
            steps {
                sh 'docker compose up -d --build'
            }
        }
    }
}
