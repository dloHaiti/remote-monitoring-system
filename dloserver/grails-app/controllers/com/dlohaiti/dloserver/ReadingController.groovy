package com.dlohaiti.dloserver

import grails.converters.JSON

class ReadingController {

    def save() {
        Reading reading = new Reading()
        reading.timestamp = params.date("reading.timestamp", "yyyy-MM-dd hh:mm:ss z")

        int numMeasurements = 0
        def measurementJson = params["reading[0]"]
        while (measurementJson) {
            Measurement measurement = new Measurement()
            measurement.parameter = measurementJson.parameter
            measurement.location = measurementJson.location
            measurement.value = measurementJson.value
            reading.addToMeasurements(measurement)
            numMeasurements++
            measurementJson = params["reading[$numMeasurements]"]
        }

        JSON.use("deep")
        if (reading.save()) {
            render ([msg: "OK"] as JSON)
        } else {
            render (status: 201, test: [msg: reading.errors] as JSON)
        }
    }
}
