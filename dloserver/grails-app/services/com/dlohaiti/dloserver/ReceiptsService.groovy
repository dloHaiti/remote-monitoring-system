package com.dlohaiti.dloserver

class ReceiptsService {

  def grailsApplication

  public saveReceipt(params) {
    Date createdDate = params.date("createdDate", grailsApplication.config.dloserver.measurement.timeformat.toString())
    Receipt receipt = new Receipt(createdDate: createdDate)
    String currencyCode = "HTG"


    params.orderedProducts?.each { op ->
      def lineItem = new ReceiptLineItem([sku: op.sku, quantity: op.quantity, type: "PRODUCT", price: op.price.amount, currencyCode: currencyCode])
      receipt.addToReceiptLineItems(lineItem)
    }
    params.appliedPromotions?.each { ap ->
      def lineItem = new ReceiptLineItem([sku: ap.sku, quantity: ap.quantity, type: "PROMOTION", price: ap.amount, currencyCode: currencyCode])
      receipt.addToReceiptLineItems(lineItem)
    }
    receipt.kiosk = Kiosk.findByName(params.kioskId)
    receipt.save(flush: true)
    return receipt
  }

}
