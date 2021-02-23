package com.prj

def build(pipelineVars) {
    echo "Running ${pipelineVars.buildArgs}"
    dir("${pipelineVars.buildArgs.reactjs.name}") {
        sh 'npm install'
        sh 'npm run-script build'
    }
}

def test() {

}

return this