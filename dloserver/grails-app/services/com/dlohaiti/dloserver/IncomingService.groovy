package com.dlohaiti.dloserver

class IncomingService {

    def grailsApplication

    public List<String> getIncomingFiles() {
        def result = []

        incomingFolder.eachFile { file ->
            result << file.absolutePath
        }

        log.debug("Found ${result.size()} files in incoming folder")

        return result
    }

    public String getFileContent(String filename) {
        return new File(filename).text
    }

    public Boolean markAsProcessed(String filename) {
        def file = new File(filename)
        def result = file.renameTo(new File(processedFolder, file.getName()))

        log.debug("Moving file $filename to 'processed' folder: ${result?'success':'error'}")
        return result
    }

    public Boolean markAsFailed(String filename) {
        def file = new File(filename)
        def result = file.renameTo(new File(failedFolder, file.getName()))

        log.debug("Moving file $filename to 'failed' folder: ${result?'success':'error'}")
        return result
    }

    private getIncomingFolder() {
        new File(grailsApplication.config?.dloserver?.readings?.incoming?.toString())
    }

    private getProcessedFolder() {
        new File(grailsApplication.config?.dloserver?.readings?.processed?.toString())
    }

    private getFailedFolder() {
        new File(grailsApplication.config?.dloserver?.readings?.failed?.toString())
    }

}
