package com.dlohaiti.dloserver.endpoint

import grails.converters.JSON
import com.dlohaiti.dloserver.PaymentHistory

class PaymentHistoryController {
    def paymentHistoryService

    def save() {
        log.debug "Received $params"

        params.kiosk = request.kiosk
        PaymentHistory paymentHistory
        try{
            paymentHistory = paymentHistoryService.saveHistory(params)
            if (paymentHistory.hasErrors()) {
                log.debug(paymentHistory.errors)
                render(status: 422,
                        contentType: 'application/json',
                        text: [errors: paymentHistory.errors.fieldErrors.collect({e -> "${e.field.toUpperCase()}_${e.code.toUpperCase()}"})] as JSON
                )
            } else {
                render(
                        status: 201,
                        contentType: 'application/json',
                        text: [errors: []] as JSON
                )
            }
        }catch (Exception e) {
            log.error("Error saving payment History: ", e)
            render(
                    status: 500,
                    contentType: 'application/json',
                    text: [errors: ['SERVER_ERROR']] as JSON
            )
        }
    }
}
