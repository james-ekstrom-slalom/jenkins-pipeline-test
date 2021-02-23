package com.prj

import org.yaml.snakeyaml.Yaml

def runPipeline(build) {
    def reactJs = new com.prj.ReactjsBuild()
    def pipelineVars = [:]

    // TODO set based on branch name
    pipelineVars.isFeature = false
    pipelineVars.isMaster = true


    node {
        stage('PreBuild') {
            checkout scm

            pipelineVars.buildArgs = readYaml file: "build.yaml"

            env.NODEJS_HOME = "${tool 'nodejs'}"
            // on linux / mac
            env.PATH="${env.NODEJS_HOME}/bin:${env.PATH}"
            sh 'npm --version'
        }

        stage('Build') {
            reactJs.build(pipelineVars)
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