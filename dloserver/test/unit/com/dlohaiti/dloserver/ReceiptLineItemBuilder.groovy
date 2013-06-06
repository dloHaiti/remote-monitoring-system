package com.dlohaiti.dloserver

class ReceiptLineItemBuilder {
  String sku = "ABC"
  Integer quantity = 1
  String type = "PRODUCT"
  BigDecimal price = 10G
  String currencyCode = "HTG"
  Integer gallons = 10

  ReceiptLineItem build() {
    return new ReceiptLineItem(sku: sku, quantity: quantity, type: type, price: price, currencyCode: currencyCode, gallons: gallons)
  }
}
