package com.dlohaiti.dloserver

class Receipt {
  Date createdDate
  Kiosk kiosk
  Integer totalGallons
  BigDecimal total
  String currencyCode

  static hasMany = [receiptLineItems: ReceiptLineItem]
}
