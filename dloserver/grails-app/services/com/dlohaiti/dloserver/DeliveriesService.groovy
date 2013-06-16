package com.dlohaiti.dloserver

class DeliveriesService {

  def grailsApplication

  def saveDelivery(params) {
    Date createdDate = params.date("createdDate", grailsApplication.config.dloserver.measurement.timeformat.toString())
    Delivery delivery = new Delivery(createdDate: createdDate, quantity: params.quantity, type: params.type)
    delivery.kiosk = Kiosk.findByName(params.kioskName)
    delivery.save(flush: true)
    return delivery
  }
}
