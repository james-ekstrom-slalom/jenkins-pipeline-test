package com.prj

def build(pipelineVars) {
    echo "Running ${pipelineVars.buildArgs}"
    dir("${pipelineVars.buildArgs.reactjs.name}") {
        sh 'npm install'
        sh 'npm run-script build'
    }
}

def test(pipelineVars) {
    echo "Testing ${pipelineVars.buildArgs}"
    dir("${pipelineVars.buildArgs.reactjs.name}") {
        sh 'npm run-script test'
    }
}

return this