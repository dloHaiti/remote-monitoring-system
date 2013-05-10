package com.dlohaiti.dloserver

import au.com.bytecode.opencsv.CSVReader

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
            measurement.timestamp = timestamp
            reading.addToMeasurements(measurement)
        }

        reading.kiosk = Kiosk.findByName(params.kiosk)
        reading.save(flush: true)
        return reading
    }

    private static BigDecimal parseValue(String value) {
        switch (value.toUpperCase()) {
            case "OK": return 1
            case "NOT_OK": return 0
            default: return value.toBigDecimal()
        }
    }

    public void importIncomingFiles() {
        log.debug("Processing new incoming files")

        def incomingFiles = incomingService.incomingFiles

        if (incomingFiles?.size()) {
            log.info("Found ${incomingFiles?.size()} new files to be imported")
        }

        incomingFiles.each {
            if (importFile(it)) {
                incomingService.markAsProcessed(it)
            } else {
                incomingService.markAsFailed(it)
            }
        }

        log.debug("Finished processing ${incomingFiles.size()} files")
    }

    private boolean importFile(String filename) {
        log.info("Started importing file '$filename'")
        CSVReader reader = new CSVReader(new FileReader(filename))
        Reading reading = new Reading(timestamp: new Date())

        String[] nextLine
        while ((nextLine = reader.readNext()) != null) {
            boolean validLine = (nextLine.length == 3)
            Measurement measurement
            if (validLine) {
                measurement = parseMeasurement(nextLine)
                reading.addToMeasurements(measurement)
                reading.kiosk = measurement.parameter?.sensor?.kiosk
                validLine = measurement.validate()
            }
            if (!validLine) {
                log.error("File '$filename' will be rejected because it contains an invalid line: \"${nextLine}\"")
                if (measurement?.hasErrors()) {
                    log.debug(measurement.errors)
                }
                return false
            }
        }

        def result = reading.save(flush: true)
        if (!result) {
            log.error("Could not import file '$filename' with ${reading.measurements?.size()} measurements")
            log.debug(reading.errors)
            return false
        }
        log.info("Finished importing file '$filename' with ${reading.measurements?.size()} measurements")
        return true
    }

    private Measurement parseMeasurement(String[] nextLine) {
        def sensorId = nextLine[0]
        def timestamp = nextLine[1]
        def value = nextLine[2]

        def sensor = Sensor.findBySensorIdIlike(sensorId)
        def measurement = new Measurement()
        measurement.parameter = sensor?.measurementType
        measurement.location = sensor?.location
        measurement.value = parseValue(value)
        measurement.timestamp = Date.parse(grailsApplication.config.dloserver.measurement.timeformat.toString(), timestamp)
        return measurement
    }
}
