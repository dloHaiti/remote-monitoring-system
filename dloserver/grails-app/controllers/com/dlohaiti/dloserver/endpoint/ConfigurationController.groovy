package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.*
import grails.converters.JSON

class ConfigurationController {

  def index() {
    List<Product> products = Product.findAllByActive(true)
    List<Parameter> parameters = Parameter.all
    List<Promotion> promotions = Promotion.all
    List<DeliveryAgent> deliveryAgents = DeliveryAgent.findAllByKioskAndActive(request.kiosk, true)


    render(
        status: 200,
        contentType: 'application/json',
        encoding: 'UTF-8',
        text: [
            products: products,
            promotions: promotions,
            parameters: parameters,
            delivery: [
              configuration: DeliveryConfiguration.first(), //there should only be one, always grab the first one
              agents: deliveryAgents
            ]
        ] as JSON)
  }
}
