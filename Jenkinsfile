pipeline{
	agent any
	environment {
		//CC_TEST_REPORTER_ID = 001b684be444f3976494ee57ecf10030bfff021eafaad236b8ecc1c9ee5ef993
		DOCKER = credentials('dockerhub')
    }
	stages{
			stage('Build'){
				steps{
				    script {
	                    try {
	                        bat "mvn clean package -DskipTests=true"
	                    } catch (Exception exp) {
	                        currentBuild.result = 'FAILURE'
	                        throw new RuntimeException('Maven clean package Failed ....')
	                    }
	                }
				}
			}
			
			stage('Test'){
				steps {
	                bat 'mvn test surefire-report:report'
	            }
	            post {
	                failure {
	                    script{
	                        currentBuild.result = 'FAILURE'
	                    }
	                    sendMail('Test Build Failed ')
	                }
	                always {
	                    junit allowEmptyResults: true, testResults: '/target/surefire-reports/TEST-*.xml'
	                    jacoco classPattern: '**/target/classes', execPattern: '**/target/**.exec'
						publishHTMLReports('target/site/junitReports/','surefire-report.html','SureFire-Report');
						publishHTMLReports('target/site/jacoco/','index.html','JaCoCoCodeCoverage');
					}
	            }
			}
			stage('Codeclimate'){
				steps{
					echo 'okkkkk!!!'
					script{
						def response = bat'curl -L https://codeclimate.com/downloads/test-reporter/test-reporter-latest-linux-amd64'
						echo 'RESPONSE ${response}'
					}
				}
			}
			stage("SonarQube Anayalsis") {
				steps {
					withSonarQubeEnv('Sonar6.7') {
						bat 'mvn sonar:sonar'
					}
					script{
						timeout(time: 10, unit: 'MINUTES') {
							def qg = waitForQualityGate abortPipeline: true
							if (qg.status != 'OK') {
								error "Pipeline aborted due to quality gate failure: ${qg.status}"
							}
						}
					}
				}
				post {
					failure {
						echo 'SonarQube Quality Gate Failed !!!!!!!'
						script {
							currentBuild.result = 'FAILURE'
						}
						sendMail('Sonar Quality Gate Failed !')
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
def sendMail(mail_subject ){
      emailext attachLog: false,
      	body: '${JELLY_SCRIPT,template="jenkins-html-custom"}', 
      	recipientProviders: [developers(), requestor()], 
      	mimeType: 'text/html' ,
      	subject: mail_subject, 
      	to: 'vishab.singh@tavant.com'
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
