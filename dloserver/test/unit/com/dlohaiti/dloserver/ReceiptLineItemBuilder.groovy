package com.dlohaiti.dloserver

class ReceiptLineItemBuilder {
  String sku = "ABC"
  Integer quantity = 1
  String type = "PRODUCT"
  BigDecimal price = 10G
  String currencyCode = "HTG"
  Double gallons = 10.0

  ReceiptLineItem build() {
    return new ReceiptLineItem(sku: sku, quantity: quantity, type: type, price: price, currencyCode: currencyCode, gallons: gallons)
  }
}
