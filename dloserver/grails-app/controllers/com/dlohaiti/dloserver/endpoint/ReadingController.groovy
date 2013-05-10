package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.Reading
import grails.converters.JSON

class ReadingController {

    def readingsService

    def save() {
        log.debug "Received $params"

        Reading reading

        try {
            reading = readingsService.saveReading(params)

            if (reading.hasErrors()) {
                // TODO Better formatting of error msgs
                log.debug(reading.errors)
                render(status: 422, text: [msg: reading.errors] as JSON)
            } else {
                render(status: 201, text: [msg: "OK"] as JSON)
            }
        } catch (Exception e) {
            log.error("Error saving Reading [${params.date('timestamp', 'yyyy-MM-dd hh:mm:ss z')}]: ", e)
            render(status: 503, text: [msg: e.message] as JSON)
        }
    }
}
