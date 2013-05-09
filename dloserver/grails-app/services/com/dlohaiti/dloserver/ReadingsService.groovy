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
        CSVReader reader = new CSVReader(new FileReader(filename))
        Reading reading = new Reading(timestamp: new Date())

        String [] nextLine
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine.length == 3) {
                Measurement measurement = parseMeasurement(nextLine)
                reading.timestamp = measurement.timestamp
                reading.addToMeasurements(measurement)
                reading.kiosk = measurement.parameter?.sensor?.kiosk
            }
        }

        reading.save(flush: true)
        if (reading.hasErrors()) {
            log.debug(reading.errors)
            return false
        }
        return true
    }

    private Measurement parseMeasurement(String[] nextLine) {
        def sensorId = nextLine[0]
        def timestamp = nextLine[1]
        def value = nextLine[2]

        def sensor = Sensor.findBySensorIdIlike(sensorId)
        def measurement = new Measurement()
        measurement.parameter = sensor.measurementType
        measurement.value = parseValue(value)
        measurement.location = sensor.location
        measurement.timestamp = Date.parse(grailsApplication.config.dloserver.measurement.timeformat.toString(), timestamp)
        return measurement
    }
}
