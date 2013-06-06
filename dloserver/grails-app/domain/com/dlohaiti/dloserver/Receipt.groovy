package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.joda.time.LocalDate

@ToString
@EqualsAndHashCode
class Receipt {
  Date createdDate
  Kiosk kiosk
  Integer totalGallons
  BigDecimal total
  String currencyCode
  List receiptLineItems

  static hasMany = [receiptLineItems: ReceiptLineItem]

  boolean isOnDate(LocalDate date) {
    return date == new LocalDate(createdDate.getYear() + 1900, createdDate.getMonth() + 1, createdDate.getDate())
  }

  boolean hasSku(String sku) {
    return receiptLineItems.any { item -> item.sku == sku }
  }

  Integer totalGallonsForSku(String sku) {
    List<ReceiptLineItem> itemsWithSku = receiptLineItems.findAll({ item -> item.sku == sku })
    return itemsWithSku.inject(0, { acc, val -> acc + val.gallons })
  }
}
