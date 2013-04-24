package com.dlohaiti.dloserver

import grails.converters.JSON

class HealthcheckController {

    // TODO check connection with database
    def index() {
        render ([msg: "OK"] as JSON)
    }
}
