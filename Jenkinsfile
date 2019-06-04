pipeline{
	agent any
	tools {
		maven 'MVN_HOME'
	}
	environment {
		//CC_TEST_REPORTER_ID = 001b684be444f3976494ee57ecf10030bfff021eafaad236b8ecc1c9ee5ef993
		CC_TEST_REPORTER_ID = credentials('CC_TEST_REPORTER_ID')

    }
	stages{
		stage('Build'){
			steps{
				sh "mvn clean package -DskipTests=true"

			}
		}
		stage('Test'){
			steps {
				sh 'mvn test surefire-report:report'
			}

		}
		stage('Codeclimate'){
			steps{
				sh "curl -L https://codeclimate.com/downloads/test-reporter/test-reporter-latest-linux-amd64 > ./cc-test-reporter"
				sh "chmod +x ./cc-test-reporter"
				sh "./cc-test-reporter before-build --debug"
			}
		}
	}
}

