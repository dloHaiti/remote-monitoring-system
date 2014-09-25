package com.dlohaiti.dloserver.endpoint

import grails.converters.JSON
import com.dlohaiti.dloserver.Sponsor

class SponsorsController {
    def sponsorsService

    def save() {
        log.debug "Received $params"
        log.debug "Received " + params.controller

        params.kiosk = request.kiosk
        Sponsor sponsor
        try{
            sponsor = sponsorsService.saveAccount(params)
            if (sponsor.hasErrors()) {
                log.debug(sponsor.errors)
                render(status: 422,
                        contentType: 'application/json',
                        text: [errors: sponsor.errors.fieldErrors.collect({e -> "${e.field.toUpperCase()}_${e.code.toUpperCase()}"})] as JSON
                )
            } else {
                render(
                        status: 201,
                        contentType: 'application/json',
                        text: [errors: []] as JSON
                )
            }
        }catch (Exception e) {
            def createdDate = params.date('createdDate', grailsApplication.config.dloserver.measurement.timeformat.toString())
            log.error("Error saving customer account [${createdDate}]: ", e)
            render(
                    status: 500,
                    contentType: 'application/json',
                    text: [errors: ['SERVER_ERROR']] as JSON
            )
        }

    }
}
