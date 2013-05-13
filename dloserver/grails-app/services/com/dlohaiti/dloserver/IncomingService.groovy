package com.dlohaiti.dloserver

class IncomingService {

    def grailsApplication

    static transactional = false

    public List<String> getIncomingFiles() {
        def result = []

        incomingFolder.eachFile { file ->
            if (file.isFile()) {
                result << file.absolutePath
            }
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

        if (result) {
            log.debug("Moved file $filename to 'processed' folder")
        } else {
            log.error("Could NOT move file $filename to 'processed' folder")
        }
        return result
    }

    public Boolean markAsFailed(String filename) {
        def file = new File(filename)
        def result = file.renameTo(new File(failedFolder, file.getName()))

        if (result) {
            log.debug("Moved file $filename to 'failed' folder")
        } else {
            log.error("Could NOT move file $filename to 'failed' folder")
        }
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
