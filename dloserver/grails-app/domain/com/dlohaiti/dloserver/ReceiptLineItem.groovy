package com.dlohaiti.dloserver

class ReceiptLineItem {
  String sku
  Integer quantity

  static belongsTo = [receipt: Receipt]
}
