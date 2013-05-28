package com.dlohaiti.dloserver

class ReceiptLineItem {
  String sku
  Integer quantity
  String type
  BigDecimal price
  String currencyCode

  static belongsTo = [receipt: Receipt]
}
