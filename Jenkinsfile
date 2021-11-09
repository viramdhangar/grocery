pipeline {
    agent any
    tools{
        maven 'MAVEN'
        jdk 'jdk'
    }
    stages {
        stage('Build') {
            steps {
                echo 'Hello World'
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: '12345', url: 'https://github.com/viramdhangar/grocery']]])
                bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        }
        stage("Publish to Nexus") {

            steps {

                script {

                    pom = readMavenPom file: "pom.xml";

                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");

                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"

                    artifactPath = filesByGlob[0].path;

                    artifactExists = fileExists artifactPath;

                    if(artifactExists) {

                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";

                        nexusArtifactUploader(
                            nexusVersion: 'nexus3',
                            
                            protocol: 'http',

                            nexusUrl: 'localhost:8081',

                            groupId: 'pom.com.bestbuy',

                            version: 'pom.0.0.1-SNAPSHOT',

                            repository: 'maven-central-repository',

                            credentialsId: 'NEXUS-CRED',

                            artifacts: [

                                [artifactId: 'pom.best-buy',

                                classifier: '',

                                file: artifactPath,

                                type: pom.packaging],

                                [artifactId: 'pom.best-buy',

                                classifier: '',

                                file: "pom.xml",

                                type: "pom"]

                            ]

                        );

                    } else {

                        error "*** File: ${artifactPath}, could not be found";

                    }

                }

            }

        }
    //    stage('SonarQube') {
    // //   def scannerHome = tool 'SonarScanner 4.0';
    //        steps{
    //            withSonarQubeEnv('sonarqube-9.1') { 
    //            // If you have configured more than one global server connection, you can specify its name
    //    //      sh "${scannerHome}/bin/sonar-scanner"
    //            bat "mvn sonar:sonar"
    //            }
    //        }
    //    }
    }
}