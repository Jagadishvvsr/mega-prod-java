pipeline{
  agent any
  stages {
    stage('checkout'){
      steps{
        git url: 'https://github.com/Jagadishvvsr/mega-prod-java.git', branch: 'main'
      }
    }

    stage('clean'){
      steps{
        sh 'echo "cleaning the workspace for new build"'
        sh 'mvn clean'
      }
    }

    stage('compile'){
      steps{
        sh 'echo "building..."'
        sh 'mvn compile'
      }
    }

    stage('test'){
      steps{
        sh 'mvn test'
      }
      post{
        always{
          junit 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('package'){
      steps{
        sh 'mvn -DskipTests package'
      }
    }

    stage('integration test and code coverage'){
      steps{
        sh 'mvn -DskipTests -DskipITs=false verify'
        jacoco execPattern: 'target/site/jacoco/**' // Collect code coverage data
      }
      post{
        always{
          junit 'target/failsafe-reports/*.xml'
          archiveArtifacts artifacts: 'target/**.exec'
        }
      }
    }
  }

  post{
    success{
      archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true // Archive build artifacts
    }
  }
}
