package com.dlohaiti.dloserver

class ReceiptsService {

  def grailsApplication

  public saveReceipt(params) {
    Date createdDate = params.date("createdDate", grailsApplication.config.dloserver.measurement.timeformat.toString())
    Receipt receipt = Receipt.findByTimestamp(createdDate)
    if (receipt) {
      //TODO: return a 409 conflict and log a message instead of deleting
    }
    receipt = new Receipt(createdDate: createdDate)

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
