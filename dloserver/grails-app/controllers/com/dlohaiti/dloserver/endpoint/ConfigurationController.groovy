package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.*
import grails.converters.JSON

class ConfigurationController {

  def index() {
    List<Product> products = Product.findAllByActive(true)
    List<Parameter> parameters = Parameter.findAllByActiveAndManual(true, true)
    List<Promotion> promotions = Promotion.all
    List<DeliveryAgent> deliveryAgents = DeliveryAgent.findAllByKioskAndActive(request.kiosk, true)
    List<ProductCategory> productCategories=ProductCategory.all
    List<SalesChannel> salesChannels=SalesChannel.all
    List<CustomerAccount> accounts = CustomerAccount.findAllByKiosk(request.kiosk)


    render(
        status: 200,
        contentType: 'application/json',
        encoding: 'UTF-8',
        text: [
            products: products,
            promotions: promotions,
            parameters: parameters,
            salesChannels: salesChannels,
            productCategories: productCategories,
            customerAccounts: accounts,
            delivery: [
              configuration: DeliveryConfiguration.first(), //there should only be one, always grab the first one
              agents: deliveryAgents
            ]
        ] as JSON)
  }
}
