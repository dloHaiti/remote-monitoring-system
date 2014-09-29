package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.MissingProductException
import com.dlohaiti.dloserver.Receipt
import grails.converters.JSON

class ReceiptsController {

    def receiptsService

    def save() {
        log.debug "Received $params"

        Receipt receipt

        try {
            params.kiosk = request.kiosk
            receipt = receiptsService.saveReceipt(params)

            if (receipt.hasErrors()) {
                def errors = receipt.errors.getFieldErrors().collect({ e -> "${e.field.toUpperCase()}_${e.code.toUpperCase()}" })
                log.error("Error when saving Receipt: " + errors)
                render(
                        status: 422,
                        contentType: 'application/json',
                        text: [errors: errors] as JSON)
            } else {
                render(
                        status: 201,
                        contentType: 'application/json',
                        text: [errors: []] as JSON)
            }
        } catch (MissingProductException e) {
            log.error("Could not find referenced product")
            render(
                    status: 422,
                    contentType: 'application/json',
                    text: [errors: ['PRODUCT_MISSING']] as JSON
            )
        } catch (Exception e) {
            def date = params.date('createdDate', grailsApplication.config.dloserver.measurement.timeformat.toString())
            log.error("Could not save Delivery on [${date}] from Kiosk [${params.kiosk.name}]. Exception: [$e]")
            render(
                    status: 500,
                    contentType: 'application/json',
                    text: [errors: ['SERVER_ERROR']] as JSON)
        }
    }
}
