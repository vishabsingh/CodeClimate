def publishHTMLReports(reportName) {
// Code Cover Plugin,Code Coverage API,HTML Publisher
    publishHTML([allowMissing         : true,
                 alwaysLinkToLastBuild: true,
                 keepAll              : true,
                 reportDir            : 'target/view',
                 reportFiles          : 'index.html',
                 reportName           : reportName])

}
def getEnvironment() {
    return  'DEVELOP\n' +
            'QA\n' +
            'UAT\n' +
            'PRD'
}
def getTagVersionOfDocker(){
    return ""
}

pipeline{
	agent any
	 parameters {
            choice(choices: "$environment", description: '', name: 'ENVIRONMENT')
            string(defaultValue: "$tagVersionOfDocker", description: 'Enter Tag Version', name: 'TAG_VERSION')
        }
	environment {
       DOCKER = credentials('dockerhub')
       NEXUS_VERSION = "nexus3"
       NEXUS_PROTOCOL = "http"
       NEXUS_URL = "localhost:8081"
       NEXUS_REPOSITORY = "vn-repository"
       NEXUS_CREDENTIAL_ID = "nexus-user"
       NEXUS_PRIVATE_REPO_URL ="10.131.155.57"
	   NEXUS_REPO_PORT = "8123"
    }
	stages{
			stage('CheckOut'){
			    steps{
			     	script{
							def scmVars = checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'GitLabCredentials', url: 'https://gitlab.com/recklessrajput23/reactivespringwithmongodb']]])
							echo "${scmVars}"
							echo "${scmVars.GIT_COMMIT}"
							env.GIT_COMMIT = scmVars.GIT_COMMIT
							def hash = "${env.GIT_COMMIT}"
							def newHash = hash.substring(0,7);
                          	echo "HASH :  ${newHash}"
                          	env.OWN_GIT_HASH = "${newHash}";
			     	}
			    }
	
			}
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
	                publishHTMLReports('reports')
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
			stage('Quality Analysis') {
	             steps {
	                  withSonarQubeEnv('Sonar-Qube') {
	                    bat 'mvn sonar:sonar'
	                  }
	            }
	        }
			stage('Build Docker Image'){
				steps{
					echo 'Building Docker Image ...'
					 bat "mvn -DskipTests=true dockerfile:build"
				}
				post {
	                failure {
	                    echo 'Docker Build Image  has  failed. See logs for details.'
	                     script{
	                        currentBuild.result = 'FAILURE'
	                    }
	                    sendMail('Build Docker Image Failed ')
	                }
	            }
			}
			stage("Docker Push"){
		        steps{
		             withDockerRegistry([ credentialsId: "dockerhub", url: "" ]) {
			         	echo  "******* Tag Version  : ${params.TAG_VERSION}   Build Number  : ${BUILD_NUMBER}  OWN GIT HASH :  ${env.OWN_GIT_HASH}  ******* "
			         	bat "docker tag vishabsingh/reactivemongodb vishabsingh/reactivemongodb:${BUILD_NUMBER}-${env.OWN_GIT_HASH}"
			         	bat "docker push vishabsingh/reactivemongodb:${BUILD_NUMBER}-${env.OWN_GIT_HASH}"
			         	//bat "docker tag vishabsingh/reactivemongodb vishabsingh/reactivemongodb:${BUILD_NUMBER}"
			         	//bat "docker push vishabsingh/reactivemongodb:${BUILD_NUMBER}"
	       				 //bat "docker tag vishabsingh/reactivemongodb vishabsingh/reactivemongodb:${params.TAG_VERSION}"
						 //bat "docker push vishabsingh/reactivemongodb:${params.TAG_VERSION}"
		        		}
		        }
		        post{
		            failure {
						echo 'Docker Push has  failed. See logs for details.'
						script{
	                        currentBuild.result = 'FAILURE'
	                    }
	                    sendMail('Docker Push In Repository  Failed ')
					}
		        }

		   }
			//https://www.youtube.com/watch?v=arAXOnvOSyY
			stage('Push Docker Image to Nexus '){
				steps{
					withDockerRegistry(credentialsId: "${env.NEXUS_CREDENTIAL_ID}", url: "${env.NEXUS_PROTOCOL}://${env.NEXUS_PRIVATE_REPO_URL}:${env.NEXUS_REPO_PORT}") {
						 bat "docker tag vishabsingh/reactivemongodb:${BUILD_NUMBER}-${env.OWN_GIT_HASH}  ${env.NEXUS_PRIVATE_REPO_URL}:${env.NEXUS_REPO_PORT}/reactivemongodb:${BUILD_NUMBER}-${env.OWN_GIT_HASH}"
					     bat "docker push ${env.NEXUS_PRIVATE_REPO_URL}:${env.NEXUS_REPO_PORT}/reactivemongodb:${BUILD_NUMBER}-${env.OWN_GIT_HASH}"
					     
					     //bat "docker tag vishabsingh/reactivemongodb:${BUILD_NUMBER} ${env.NEXUS_PRIVATE_REPO_URL}:${env.NEXUS_REPO_PORT}/reactivemongodb:${BUILD_NUMBER}"
					     //bat "docker push ${env.NEXUS_PRIVATE_REPO_URL}:${env.NEXUS_REPO_PORT}/reactivemongodb:${BUILD_NUMBER}"
					}
					
					//withCredentials([usernamePassword(credentialsId: 'nexus-user', passwordVariable: 'PASS', usernameVariable: 'USER')]) {}
				}
				
				 post{
	                failure {
						echo 'Push Docker Image  to Nexus has failed !!!!'
						script{
	                        currentBuild.result = 'FAILURE'
	                    }
	                    sendMail('Push Docker Image To Nexus Repository  has Failed ')
					}
	           }
			}
		//		
    	  stage('Publish To Nexus'){
            steps{
              script{
              		pom = readMavenPom file: "pom.xml";
              		filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
              		 artifactPath = filesByGlob[0].path;
              		 artifactExists = fileExists artifactPath;
	                    if(artifactExists) {
	                    		echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
	                    		nexusArtifactUploader artifacts: 
		                        [
		                        	[artifactId: pom.artifactId, classifier: '', file: artifactPath, type: pom.packaging]
		                        ], 
		                        credentialsId: NEXUS_CREDENTIAL_ID, 
		                        groupId: pom.groupId, 
		                        nexusUrl: NEXUS_URL, 
		                        nexusVersion: NEXUS_VERSION, 
		                        protocol: NEXUS_PROTOCOL, 
		                        repository: NEXUS_REPOSITORY, 
		                        //version: pom.version
		                        //version: "${params.TAG_VERSION}"
		                        //version: "${BUILD_NUMBER}"
		                        version: "${pom.version}-${BUILD_NUMBER}-${env.OWN_GIT_HASH}"
		                  echo '********* Done Publish to NEXUS OSS ************** ' 
		              	}else {
                        	error "*** File: ${artifactPath}, could not be found";
                    	}
                }
   
            }
           post{
                failure {
					echo 'Push to Nexus has failed. See logs for details.'
					script{
                        currentBuild.result = 'FAILURE'
                    }
                    sendMail('Publish To Nexus Repository Failed ')
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
