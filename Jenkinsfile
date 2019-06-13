pipeline{
	agent any
	tools {
		maven "Maven-3.3.9"
        jdk "JAVA_HOME"
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
             when {maven "Maven-3.3.9"
        jdk "JAVA_HOME"
                  branch 'codacy'
             }
            steps{
                sh '''
                    curl -Ls -o codacy-coverage-reporter "$(curl -Ls https://api.github.com/repos/codacy/codacy-coverage-reporter/releases/latest |
                    jq -r '.assets |
                    map({name, browser_download_url} |
                    select(.name | contains("codacy-coverage-reporter-linux"))) |
                    .[0].browser_download_url')"
                 '''
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
        stage('SonarQube Analysis'){
            steps{
                withSonarQubeEnv('Sonar6.7') {
                    sh  "mvn sonar:sonar -Dsonar.projectName=Codeclimate -Dsonar.projectKey=Codeclimate"
                }
                 script{
                        timeout(time: 20, unit: 'MINUTES') {
                            def qg = waitForQualityGate abortPipeline: true
                            if (qg.status != 'OK') {
                                error "Pipeline aborted due to quality gate failure: ${qg.status}"
                            }
                        }
                    }
            }
            post {
                failure {
                    script {currentBuild.result = 'FAILURE'}
                    sendMail('Quality Gate Failed !!!!')
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


