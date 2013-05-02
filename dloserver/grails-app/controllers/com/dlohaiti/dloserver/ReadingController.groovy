package com.dlohaiti.dloserver

import grails.converters.JSON

class ReadingController {

    def save() {
        log.debug "Received $params"
        def timestamp = params.date("timestamp", "yyyy-MM-dd hh:mm:ss z")
        Reading reading = Reading.findByTimestamp(timestamp)
        if (!reading) {
            reading = new Reading()
        }

        try {
            reading.timestamp = timestamp

            params.measurements?.each {
                Measurement measurement = new Measurement(it)
                reading.addToMeasurements(measurement)
            }

            if (reading.save()) {
                render(status: 201, text: [msg: "OK"] as JSON)
            } else {
                // TODO Better formatting of error msgs
                log.debug(reading.errors)
                render(status: 422, text: [msg: reading.errors] as JSON)
            }
        } catch (Exception e) {
            log.error("Error saving Reading [$reading.timestamp]: ", e)
            // FIXME Not working, it's throwing and 500 error
            render(status: 503, text: [msg: e.message] as JSON)
        }
    }
}
