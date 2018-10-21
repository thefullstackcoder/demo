def label = "worker-${UUID.randomUUID().toString()}"
podTemplate(label: label, containers: [
  containerTemplate(name: 'gradle', image: 'registry.kubecluster2.local:5000/jenkins-gradle2', command: 'cat', ttyEnabled: true, alwaysPullImage: true),
  containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.8.8', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:latest', command: 'cat', ttyEnabled: true)
],
volumes: [
  hostPathVolume(mountPath: '/home/gradle/.gradle', hostPath: '/tmp/jenkins/.gradle'),
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]) {
  node(label) {
    def myRepo = checkout scm
    def gitCommit = myRepo.GIT_COMMIT
    def gitBranch = myRepo.GIT_BRANCH
    def shortGitCommit = "${gitCommit[0..10]}"
    def previousGitCommit = sh(script: "git rev-parse ${gitCommit}~", returnStdout: true)
    def registry = "registry.kubedemo.local:5000"
  
    stage('Build') {
      container('gradle') {
        sh "gradle build --exclude-task integrationTest"
      }
    }
    
    stage('Test') {
      container('gradle') {
        try{
            sh "gradle test --exclude-task integrationTest"
         }finally{
            publishHTML (target: [
                  allowMissing: false,
                  alwaysLinkToLastBuild: false,
                  keepAll: true,
                  reportDir: 'build/reports/test',
                  reportFiles: 'index.html',
                  reportName: "Unit Test Report"
                ])
                junit "build/test-results/test/*.xml"
                jacoco execPattern: 'build/jacoco/**.exec'
         }
      }
    }
    
    stage('Code Quality Check'){
      container('gradle') {
        withSonarQubeEnv {
            sh 'gradle sonarqube -Dsonar.host.url=http://sonar-sonarqube.sonarqube.svc.cluster.local:9000 -Dsonar.verbose=true --stacktrace'
        }
      }
    }
    
    stage('Create Docker images') {
      container('docker') {        
          sh "docker build -t ${registry}/demo:${gitCommit} ."
          sh "docker push ${registry}/demo:${gitCommit}"
      }
    }  
    
    
        
    stage('Run Tests'){
        parallel (
          "Performance Test": {
             stage('Performance Test'){
                  container('helm'){
                    sh "helm upgrade demo-perf helm/. --namespace demo-perf --set image.tag=${gitCommit} --install"
                  }//container helm
                  
                  container('gradle'){
                    sh "gradle gatlingRun --exclude-task integrationTest"
                    gatlingArchive()
                  }//container gradle
                }//stage performance tests
          },      
          "Integration Tests": {
              stage('Integration Tests'){
                container('helm'){
                  sh "helm upgrade demo-int helm/. --namespace demo-int --set image.tag=${gitCommit} --install"
                } //container helm
              
                container('gradle'){
                    echo 'Testing..'
                    try{
                        sh "gradle integrationTest"
                     }finally{
                        publishHTML (target: [
                              allowMissing: false,
                              alwaysLinkToLastBuild: false,
                              keepAll: true,
                              reportDir: 'build/reports/integrationTest',
                              reportFiles: 'index.html',
                              reportName: "Integration Test Report"
                            ])
                            junit "build/test-results/integrationTest/*.xml"
                     }//try
                }//container gradle
            } //stage ingeration tests
          }
        )//parallel
    }//run tests
    
    
    stage('Run helm') {
      container('helm') {
        sh "helm upgrade demo helm/. --namespace demo --set image.tag=${gitCommit} --install"
      }//container helm
    }//stage helm    
    
  }//node
}//pod template
