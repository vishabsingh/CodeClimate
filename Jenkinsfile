pipeline{
	agent any
	tools {
		maven 'M2_HOME'
		//jdk 'jdk_1.8.0_151'
	}
	environment {
		CC_TEST_REPORTER_ID = credentials('CC_TEST_REPORTER_ID')
		CODACY_PROJECT_TOKEN = credentials('CODACY_PROJECT_TOKEN');
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
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
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
		stage('Upload Test Coverage For Codacy '){
             when {
                  branch 'codacy'
             }
            steps{
               //sh "curl -Ls -o codacy-coverage-reporter https://dl.bintray.com/codacy/Binaries/curl -Ls https://api.bintray.com/packages/codacy/Binaries/codacy-coverage-reporter/versions/_latest | jq -r .name/codacy-coverage-reporter-linux "
                //sh "curl -Ls -o codacy-coverage-reporter-assembly.jar curl -Ls https://api.github.com/repos/codacy/codacy-coverage-reporter/releases/latest | jq -r '.assets | map({content_type, browser_download_url} | select(.content_type | contains('java-archive'))) | .[0].browser_download_url')"
                //sh "curl -L https://dl.bintray.com/codacy/Binaries/curl -L https://api.bintray.com/packages/codacy/Binaries/codacy-coverage-reporter/versions/_latest | jq -r .name/codacy-coverage-reporter-linux  > ./codacy-coverage-reporter"
                //sh "chmod +x codacy-coverage-reporter"
                //sh "./codacy-coverage-reporter report -l Java -r target/site/jacoco/jacoco.xml"
                sh "curl -Ls -o codacy-coverage-reporter-assembly.jar $(curl -Ls https://api.github.com/repos/codacy/codacy-coverage-reporter/releases/latest | jq -r '.assets | map({content_type, browser_download_url} | select(.content_type | contains("java-archive"))) | .[0].browser_download_url')"
                sh '''
                        LATEST_VERSION="\$(curl -Ls https://api.bintray.com/packages/codacy/Binaries/codacy-coverage-reporter/versions/_latest | jq -r .name)"
                        curl -Ls -o codacy-coverage-reporter-assembly.jar "https://dl.bintray.com/codacy/Binaries/\${LATEST_VERSION}/codacy-coverage-reporter-assembly.jar"
                '''
                //sh "java -jar codacy-coverage-reporter-assembly.jar report -l Java -r jacoco.xml"
                sh "chmod +x codacy-coverage-reporter"
                sh "./codacy-coverage-reporter report -l Java -r target/site/jacoco/jacoco.xml"


            }
            post {
                failure {
                    script{
                        currentBuild.result = 'FAILURE'
                    }
                    sendMail('Upload Coverage for Codacy !!!!! ')
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


