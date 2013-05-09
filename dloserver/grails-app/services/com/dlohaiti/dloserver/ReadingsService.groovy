package com.dlohaiti.dloserver

class ReadingsService {

    def incomingService
    def grailsApplication

    public saveReading(params) {
        Date timestamp = params.date("timestamp", grailsApplication.config.dloserver.measurement.timeformat.toString())
        Reading reading = Reading.findByTimestamp(timestamp)
        if (reading) {
            reading.delete()
        }
        reading = new Reading(timestamp: timestamp)

        params.measurements?.each {
            Measurement measurement = new Measurement()
            measurement.location = Location.findByNameIlike(it.location)
            measurement.parameter = MeasurementType.findByNameIlike(it.parameter)
            measurement.value = parseValue(it.value)
            reading.addToMeasurements(measurement)
        }

        reading.save(flush: true)
        return reading
    }

    private BigDecimal parseValue(String value) {
        switch (value.toUpperCase()) {
            case "OK": return 1
            case "NOT_OK": return 0
            default: return value.toBigDecimal()
        }
    }

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
