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

        // TODO handle database error (status: 503)
        if (reading.save()) {
            render (status: 201, text: [msg: "OK"] as JSON)
        } else {
            // TODO Better formatting of error msgs
            render (status: 422, text: [msg: reading.errors] as JSON)
        }
    }
}
