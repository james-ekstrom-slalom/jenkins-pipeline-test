package com.prj

import org.yaml.snakeyaml.Yaml

def runPipeline(build) {
    def reactJs = new com.prj.ReactjsBuild()
    def pipelineVars = [:]

    // TODO Get common build properties (roles, buckets, etc) from convention based ssm or vault parameter stores
    // Prevent passing around a huge collection of parameters

    // TODO set based on branch name
    pipelineVars.isFeature = false
    pipelineVars.isMaster = true

    node {
        stage('PreBuild') {
            checkout scm

            pipelineVars.buildArgs = readYaml file: "build.yaml"

            // Get which variables we need from the build target??
            // Then look them up in vault or ssm?
            echo "Setting common build json"
            pipelineVars.commonBuildJson = sh (
                script: "aws ssm get-parameter --name \"/service/test-build/james-build-test\" --region us-west-2 | python -c \"import sys, json; print(json.load(sys.stdin)['Parameter']['Value'])\"",
                returnStdout: true
            ).trim()

            echo "Setting build json"
            pipelineVars.buildJson = sh (
                script: "aws ssm get-parameter --name \"${pipelineVars.buildArgs.parameterPath}\" --region us-west-2 | python -c \"import sys, json; print(json.load(sys.stdin)['Parameter']['Value'])\"",
                returnStdout: true
            ).trim()

            pipelineVars.parameters.common = readJSON text: pipelineVars.commonBuildJson
            pipelineVars.parameters.additional = readJSON text: pipelineVars.buildJson


            // Can we benefit from containerized builds here??
            // A dockerfile will make it easier to build in general because the app developer defines the build env
            // Just need to pass the correct parameters to it.

            env.NODEJS_HOME = "${tool 'nodejs'}"
            // on linux / mac
            env.PATH="${env.NODEJS_HOME}/bin:${env.PATH}"
            sh 'npm --version'
        }

        stage('Build') {
            reactJs.build(pipelineVars)
        }

        stage('Test') {
            reactJs.test(pipelineVars)
        }

        stage('Package') {
            // Package the build artifacts and push them to a centralized location
        }
    }
}

return this


// def call(String name = 'human') {
//     echo "Hello, ${name}."
// }



// def preBuild() {
//     // gather credentials
// }

// def build() {
//     // build and compile code
//     reactJs.build(pipelineVars)
// }

// def test() {
//     // run unit tests and other tests
// }

// def deploy() {
//     // deploy to live server
//     // set up s3 bucket with cloudfront serving it.
//     // Deploy to s3, refresh cache on cloudfront ?? 
// }

// def postBuild() {
//     // cleanup and notifications
// }