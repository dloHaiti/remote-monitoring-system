package com.dlohaiti.dloserver

class ReceiptsService {

  def grailsApplication

  public saveReceipt(params) {
    Date createdDate = params.date("createdDate", grailsApplication.config.dloserver.measurement.timeformat.toString())
    Receipt receipt = new Receipt(createdDate: createdDate)
    String currencyCode = "HTG"

    params.lineItems?.each { item ->
      def lineItem = new ReceiptLineItem([sku: item.sku, quantity: item.quantity, type: item.type, price: item.price.amount, currencyCode: currencyCode])
      receipt.addToReceiptLineItems(lineItem)
    }
    receipt.kiosk = Kiosk.findByName(params.kioskId)
    receipt.save(flush: true)
    return receipt
  }

}
