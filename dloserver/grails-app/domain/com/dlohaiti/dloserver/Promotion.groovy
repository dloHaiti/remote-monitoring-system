package com.dlohaiti.dloserver

class Promotion {
  String appliesTo
  String productSku
  Date startDate
  Date endDate
  BigDecimal amount
  String type
  String sku

  static constraints = {
    sku(unique: true)
    productSku(nullable: true)
  }

  boolean hasRange() {
    return false;
  }
}
