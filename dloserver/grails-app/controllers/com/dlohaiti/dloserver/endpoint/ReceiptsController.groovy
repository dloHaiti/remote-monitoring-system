package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.Receipt
import grails.converters.JSON

class ReceiptsController {

  def receiptsService

  def save() {
    log.debug "Received $params"

    Receipt sale

    try {
      sale = receiptsService.saveSale(params)

      if (sale.hasErrors()) {
        // TODO Better formatting of error msgs
        log.debug(sale.errors)
        render(status: 422, text: [msg: sale.errors] as JSON)
      } else {
        render(status: 201, text: [msg: "OK"] as JSON)
      }
    } catch (Exception e) {
      log.error("Error saving Receipt [${params.date('timestamp', 'yyyy-MM-dd hh:mm:ss z')}]: ", e)
      render(status: 503, text: [msg: e.message] as JSON)
    }
  }
}
