pipeline {
 agent { docker { image 'maven:3.3.3' } }
 stages {
     stage('build') {
         steps {
             echo 'Building...'
             sh 'mvn clean'
         }
     }
     stage('Test') {
          steps {
              echo 'Testing..'
              sh 'mvn package'
          }
     }
     stage('Deploy') {
           steps{
               echo 'Deploying....'
               sh 'docker build -t springio/auth-course .'
           }
     }
     stage('deploy to docker'){
          steps{
               echo 'running in docker ..'
               sh 'docker run -d -p 8082:8082 springio/auth-course'
          }
     }
 }
}