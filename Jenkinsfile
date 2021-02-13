pipeline {
 agent { docker { image 'maven:3.3.3' } }
 stages {
     stage('build') {
         steps {
             echo 'Building...'
             sh 'mvn clean install'
         }
     }
     stage('Docker Build') {
           agent any
           def dockerHome = tool 'mydocker'
           env.PATH = "${dockerHome}/bin:${env.PATH}"
           steps {
             sh 'docker build -t barry/auth-course:latest .'
           }
     }
     stage('Docker Push') {
           agent any
           steps {
               sh 'docker push barry/auth-course:latest'
         }
     }
 }
}