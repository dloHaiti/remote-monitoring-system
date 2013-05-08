package com.dlohaiti.dloserver

import static java.util.Collections.emptyList

class IncomingService {

    def grailsApplication

    private getIncomingFolder() {
        new File(grailsApplication.config?.dloserver?.readings?.incoming?.toString())
    }

    public List<String> getIncomingFiles() {
        def result = []

        incomingFolder.eachFile { file ->
            result << file.absolutePath
        }

        return result
    }

    public String getFileContent(String filename) {
        return new File(filename).text
    }
}
