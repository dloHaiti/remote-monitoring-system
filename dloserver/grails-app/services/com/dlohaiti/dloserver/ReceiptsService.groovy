package com.dlohaiti.dloserver

class ReceiptsService {

  def grailsApplication

  public saveReceipt(params) {
    Date createdDate = params.date("createdDate", grailsApplication.config.dloserver.measurement.timeformat.toString())
    String currencyCode = params.total.currencyCode
    Receipt receipt = new Receipt([createdDate: createdDate, totalGallons: params.totalGallons, total: params.total.amount, currencyCode: currencyCode])

    params.lineItems?.each { item ->
      Product product = Product.findBySku(item.sku)
      def lineItem = new ReceiptLineItem(
          sku: item.sku,
          quantity: item.quantity,
          type: item.type,
          price: item.price.amount,
          currencyCode: currencyCode,
          gallons: product.gallons * item.quantity
      )
      receipt.addToReceiptLineItems(lineItem)
    }
    receipt.kiosk = Kiosk.findByName(params.kioskId)
    receipt.save(flush: true)
    return receipt
  }

}
