package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.Delivery
import grails.converters.JSON

class DeliveriesController {

  def deliveriesService

  def save() {
    log.debug "Received $params"

    Delivery delivery

    try {
      params.kioskName = request.kioskName
      delivery = deliveriesService.saveDelivery(params)

      if (delivery.hasErrors()) {
        // TODO Better formatting of error msgs
        log.debug(delivery.errors)
        render(status: 422, text: [msg: delivery.errors] as JSON)
      } else {
        render(status: 201, text: [msg: "OK"] as JSON)
      }
    } catch (Exception e) {
      log.error("Error saving Receipt [${params.date('timestamp', 'yyyy-MM-dd hh:mm:ss z')}]: ", e)
      render(status: 503, text: [msg: e.message] as JSON)
    }
  }
}
