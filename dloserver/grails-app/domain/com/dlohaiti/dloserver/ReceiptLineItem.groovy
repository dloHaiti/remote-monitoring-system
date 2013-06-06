package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class ReceiptLineItem {
  String sku
  Integer quantity
  String type
  BigDecimal price
  String currencyCode
  Integer gallons

  static belongsTo = [receipt: Receipt]
}
