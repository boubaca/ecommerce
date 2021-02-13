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
           }
     }
 }
}