package com.dlohaiti.dloserver

import grails.converters.JSON

class ReadingController {

    def show() {
        if (params.id && Reading.exists(params.id)) {
            Reading reading = Reading.findByDateCreated(params.id)
            render reading as JSON
        }
        else {
            List<Reading> all = Reading.list()
            render all as JSON
        }
    }

    def update() {
        Reading reading = new Reading(params.reading)

        if (reading.save()) {
            render reading as JSON
        } else {
            render reading.errors
        }
    }
}
