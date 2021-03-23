package com.prj

def build(pipelineVars) {
    echo "Running ${pipelineVars.buildArgs}"
    // do build inside docker container instead. bind to it to get artifact output
    dir("${pipelineVars.buildArgs.reactjs.name}") {
        sh 'npm install'
        sh 'npm run-script build'
    }

    echo "Common parameters: ${pipelineVars.commonBuildJson}"
    echo "Build parameters: ${pipelineVars.buildJson}"

    echo "Common parameters parsed: ${pipelineVars.parameters.common}"
    echo "Build parameters parsed: ${pipelineVars.parameters.additional}"
}

def test(pipelineVars) {
    echo "Testing ${pipelineVars.buildArgs}"
    dir("${pipelineVars.buildArgs.reactjs.name}") {
        sh 'npm run-script test'
    }
}

return this