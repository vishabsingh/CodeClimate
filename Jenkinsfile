pipeline{
	agent any
	tools {
		maven 'M2_HOME'
		//jdk 'jdk_1.8.0_151'
	}
	environment {
		//CC_TEST_REPORTER_ID = 001b684be444f3976494ee57ecf10030bfff021eafaad236b8ecc1c9ee5ef993
		CC_TEST_REPORTER_ID = credentials('CC_TEST_REPORTER_ID')
		//JACOCO_SOURCE_PATH = "src/main/java"

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
		stage('Upload Test Coverage For Code Climate '){
		     when {
                  branch 'codeclimate'
             }
			steps{
				sh "curl -L https://codeclimate.com/downloads/test-reporter/test-reporter-latest-linux-amd64 > ./cc-test-reporter"
				sh "chmod +x ./cc-test-reporter"
				//sh "./cc-test-reporter format-coverage  -d -t jacoco --add-prefix /src/main/java/ target/site/jacoco/jacoco.xml"
				sh './cc-test-reporter format-coverage target/site/jacoco/jacoco.xml --input-type jacoco'
				sh "./cc-test-reporter upload-coverage -r ${CC_TEST_REPORTER_ID}"
			}
		}
	}
}

