package com.dlohaiti.dloserver

class Receipt {
  Date createdDate
  Kiosk kiosk

  static hasMany = [receiptLineItems: ReceiptLineItem]
}
