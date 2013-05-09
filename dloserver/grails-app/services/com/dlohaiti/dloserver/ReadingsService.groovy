package com.dlohaiti.dloserver

class ReadingsService {

    def incomingService

    public void importIncomingFiles() {
        log.info("Importing new incoming files")

        def incomingFiles = incomingService.incomingFiles

        incomingFiles.each {
            def result = importFile(it)
            log.info("Finished importing file $it: ${result?'SUCCESS':'ERROR'}")
            if (result) {
                incomingService.markAsProcessed(it)
            } else {
                incomingService.markAsFailed(it)
            }
        }

        log.info("Finished importing ${incomingFiles.size()} files")
    }

    private boolean importFile(String filename) {
        return true;  // TODO
    }
}
