package com.prj

def build(pipelineVars) {
    echo "Running ${pipelineVars.buildArgs}"
    // do build inside docker container instead. bind to it to get artifact output
    dir("${pipelineVars.buildArgs.reactjs.name}") {
        sh 'npm install'
        sh 'npm run-script build'
    }

    echo "Common parameters: ${env.COMMONBUILDJSON}"
    echo "Build parameters: ${env.BUILDJSON}"
}

def test(pipelineVars) {
    echo "Testing ${pipelineVars.buildArgs}"
    dir("${pipelineVars.buildArgs.reactjs.name}") {
        sh 'npm run-script test'
    }
}

return this