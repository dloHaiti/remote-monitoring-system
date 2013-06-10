package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.Parameter
import com.dlohaiti.dloserver.Product
import grails.converters.JSON

class ConfigurationController {

  def index() {
    List<Product> products = Product.all
    List<Parameter> parameters = Parameter.all
//    List<Promotion> promotions = Promotion.all

    render(status: 200, text: [
        products: products,
//        promotions: promotions,
        parameters: parameters
    ] as JSON)
  }
}
