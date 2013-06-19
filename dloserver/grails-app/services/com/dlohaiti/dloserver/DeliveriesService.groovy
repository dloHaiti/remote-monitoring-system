package com.dlohaiti.dloserver

class DeliveriesService {

  def grailsApplication

  def saveDelivery(params) {
    Date createdDate = params.date("createdDate", grailsApplication.config.dloserver.measurement.timeformat.toString())
    DeliveryAgent agent = DeliveryAgent.findByName(params.agentName)
    if(agent == null) {
      throw new MissingDeliveryAgentException()
    }
    Delivery delivery = new Delivery(createdDate: createdDate, quantity: params.quantity, type: params.type, deliveryAgent: agent, kiosk: params.kiosk)
    delivery.save(flush: true)
    return delivery
  }
}
