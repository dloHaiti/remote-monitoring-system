package com.dlohaiti.dloserver

class ImportCsvJob {

    static triggers = {
        simple repeatInterval: 60000l, startDelay: 60000l
    }

    def readingsService

    def execute() {
        readingsService.importIncomingFiles()
    }
}
