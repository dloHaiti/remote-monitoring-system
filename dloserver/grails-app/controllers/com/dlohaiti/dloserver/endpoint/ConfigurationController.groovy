package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.*
import grails.converters.JSON

class ConfigurationController {
    def configurationService

  def index() {

    List<Product> products = Product.findAllByActive(true)
    List<Parameter> parameters =  request.kiosk.getParameters()
    List<Promotion> promotions = request.kiosk.getPromotions()
    List<DeliveryAgent> deliveryAgents = DeliveryAgent.findAllByKioskAndActive(request.kiosk, true)
    List<ProductCategory> productCategories=ProductCategory.all
    List<SalesChannel> salesChannels=request.kiosk.getSalesChannels()
    List<CustomerAccount> accounts = CustomerAccount.findAllByKiosk(request.kiosk)
    List<ProductMrp> productMrps = ProductMrp.findAllByKiosk(request.kiosk)
    List<Sponsor> sponsors = request.kiosk.getSponsors()
    List<Rebate> rebates = request.kiosk.getRebates()

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
            productMrps: productMrps,
            sponsors: sponsors,
            rebates: rebates,
            delivery: [
              configuration: DeliveryConfiguration.first(), //there should only be one, always grab the first one
              agents: deliveryAgents
            ],
            configuration: [
                    unitOfMeasure: configurationService.getUnitOfMeasure(),
                    currency: configurationService.getCurrencyCode(),
                    locale: configurationService.getLocale(),
                    dateformat: configurationService.getDateFormat(),
                    paymentType: configurationService.getPaymentType()
            ]
        ] as JSON)
  }
}
