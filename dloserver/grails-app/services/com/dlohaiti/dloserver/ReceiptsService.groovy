package com.dlohaiti.dloserver

class ReceiptsService {

  def grailsApplication

  public saveSale(params) {
    Date timestamp = params.date("createdAt", grailsApplication.config.dloserver.measurement.timeformat.toString())
    Receipt receipt = Receipt.findByTimestamp(timestamp)
    if (receipt) {
      //TODO: return a 409 conflict and log a message instead of deleting
    }
    receipt = new Receipt(timestamp: timestamp)

    params.orderedProducts?.each { op ->
      ReceiptLineItem lineItem = new ReceiptLineItem()
      lineItem.sku = op.sku
      lineItem.quantity = op.quantity
      receipt.addToReceiptLineItems(lineItem)
    }
    receipt.kiosk = Kiosk.findByName(params.kioskId)
    receipt.save(flush: true)
    return receipt
  }

}
