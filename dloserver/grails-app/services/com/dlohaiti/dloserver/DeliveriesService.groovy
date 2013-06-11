package com.dlohaiti.dloserver

class DeliveriesService {

  def grailsApplication

  def saveDelivery(params) {
    Date timestamp = params.date("createdAt", grailsApplication.config.dloserver.measurement.timeformat.toString())
    Delivery delivery = new Delivery(timestamp: timestamp, quantity: params.quantity, type: params.type)
    delivery.kiosk = Kiosk.findByName(params.kioskName)
    delivery.save(flush: true)
    return delivery
  }
}
