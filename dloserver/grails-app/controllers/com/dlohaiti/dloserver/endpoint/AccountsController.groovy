package com.dlohaiti.dloserver.endpoint

import grails.converters.JSON
import com.dlohaiti.dloserver.CustomerAccount;

class AccountsController {

    def accountsService

    def save() {
        params.kiosk = request.kiosk
        log.debug "Received $params"
        log.debug "Received " + params.controller

        render(

                status: 201,
                contentType: 'application/json',
                text: [errors: [],params: params] as JSON
        )
    }
}
