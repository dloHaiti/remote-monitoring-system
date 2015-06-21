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
    List<CustomerAccount> accounts = CustomerAccount.findAllByKioskAndActive(request.kiosk, true)
    List<ProductMrp> productMrps = ProductMrp.findAllByKiosk(request.kiosk)
    List<Sponsor> sponsors = Sponsor.findAllByKiosk(request.kiosk)
    List<Rebate> rebates = request.kiosk.getRebates()
    List<CustomerType> customerTypes = CustomerType.all

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
            customerTypes: customerTypes,
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
                    paymentModes: configurationService.getPaymentModes(),
                    paymentTypes: configurationService.getPaymentTypes(),
                    deliveryTimes: configurationService.getDeliveryTimes()
            ]
        ] as JSON)
  }
}
