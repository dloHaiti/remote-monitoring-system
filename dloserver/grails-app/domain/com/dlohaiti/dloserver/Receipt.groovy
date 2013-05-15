package com.dlohaiti.dloserver

class Receipt {
  Date timestamp
  Kiosk kiosk

  static hasMany = [receiptLineItems: ReceiptLineItem]
}
