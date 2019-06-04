pipeline{
	agent any
	environment {
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
						publishHTML([reportDir: '/target/site/jacoco', reportFiles: 'index.html', reportName: 'JaCoCo Code Coverage'])
						publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'target/site/junitReports/', reportFiles: 'surefire-report.html', reportName: 'SureFire-Report', reportTitles: ''])
	                }
	            }
			}
			stage('Codeclimate'){
				steps{
					echo 'okkkkk!!!'
				}
			}
			stage('Quality Analysis') {
	             steps {
	                  withSonarQubeEnv('Sonar-Qube') {
	                    bat 'mvn sonar:sonar'
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
