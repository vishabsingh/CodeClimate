pipeline{
	agent any
	tools {
		maven 'M2_HOME'
		//jdk 'jdk_1.8.0_151'
	}
	environment {
		CC_TEST_REPORTER_ID = credentials('CC_TEST_REPORTER_ID')
		JACOCO_SOURCE_PATH = "src/main/java"

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
			post {
                failure {
                    script{
                        currentBuild.result = 'FAILURE'
                    }
                    sendMail('Test Build Failed ')
                }

                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/TEST-*.xml'
                    jacoco classPattern: '**/target/classes', execPattern: '**/target/**.exec'
                    publishHTMLReports('target/site/junitReports/','surefire-report.html','Surefire-Report');
                    publishHTMLReports('target/site/jacoco/','index.html','Jacoco-Coverage-Report');

                }
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
			post {
                failure {
                    script{
                        currentBuild.result = 'FAILURE'
                    }
                    sendMail('Upload Coverage for Code Climate Failed !!!!! ')
                }
            }
		}
	}
	post {
        success{
            sendMail('All Stages Build SucessFully')
        }
    }
}
def publishHTMLReports(reportDirectory,reportFileName,reportName) {
	publishHTML([allowMissing         : false,
				 alwaysLinkToLastBuild: false,
				 keepAll              : false,
				 reportDir            : reportDirectory,
				 reportName           : reportName,
				 reportFiles          : reportFileName
	])

}
def sendMail(mail_subject ){
      emailext attachLog: false,
      	body: '${JELLY_SCRIPT,template="jenkins-jelly-template"}',
      	recipientProviders: [developers(), requestor()],
      	mimeType: 'text/html' ,
      	subject: mail_subject,
      	to: 'vishab.singh@tavant.com'
}


