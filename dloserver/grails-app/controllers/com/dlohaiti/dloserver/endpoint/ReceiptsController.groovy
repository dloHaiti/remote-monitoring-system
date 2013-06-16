package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.Receipt
import grails.converters.JSON

class ReceiptsController {

  def receiptsService

  def save() {
    log.debug "Received $params"

    Receipt receipt

    try {
      params.kioskName = request.kioskName
      receipt = receiptsService.saveReceipt(params)

      if (receipt.hasErrors()) {
        // TODO Better formatting of error msgs
        log.debug(receipt.errors)
        render(status: 422, text: [msg: receipt.errors] as JSON)
      } else {
        render(status: 201, text: [msg: "OK"] as JSON)
      }
    } catch (Exception e) {
      log.error("Error saving Receipt [${params.date('createdDate', 'yyyy-MM-dd hh:mm:ss z')}]: ", e)
      render(status: 503, text: [msg: e.message] as JSON)
    }
  }
}
