package com.dlohaiti.dloserver

import grails.converters.JSON

class HealthcheckController {

    def index() {
        render ([msg: "OK"] as JSON)
    }
}
