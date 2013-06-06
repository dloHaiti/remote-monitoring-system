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

  static hasMany = [receiptLineItems: ReceiptLineItem]

  boolean isOnDate(LocalDate date) {
    def date1 = new LocalDate(createdDate.getYear() + 1900, createdDate.getMonth() + 1, createdDate.getDate())
    println date1
    return date == date1
  }

  boolean hasSku(String sku) {
    return receiptLineItems.any { item -> item.sku == sku }
  }
}
