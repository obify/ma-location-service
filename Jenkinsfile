pipeline {
    agent any
    stages {
        stage('git repo and clean') {
            steps {
                bat "rmdir  /s /q ma-location-service"
                bat "git clone https://github.com/obify/ma-location-service.git"
            }
        }
        stage('test') {
            steps {
                bat "mvn test -f ma-location-service"
            }
        }
        stage('SonarQube Analysis') {
                    steps {
                        script{
                            withSonarQubeEnv('SonarQubeServer'){
                                bat "mvn clean install"
                                bat "mvn sonar:sonar"
                            }
                            timeout(time: 1, unit: 'HOURS'){
                            def qg = waitForQualityGate()
                                if(qg.status != 'OK'){
                                    error "Pipeline aborted quality gate failure: ${qg.status}"
                                }
                            }
                            bat "mvn clean install"
                        }
                    }
        }
        stage('package') {
            steps {
                bat "mvn package -f ma-location-service"
            }
        }
    }
}
