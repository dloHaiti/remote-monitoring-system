package com.dlohaiti.dloserver

class Promotion {
  String appliesTo
  String productSku
  Date startDate
  Date endDate
  BigDecimal amount
  String type
  String sku
  String base64EncodedImage

  static hasMany = [regions: Region]
  static belongsTo = Region


    static mapping = {
    base64EncodedImage type: "text"
  }

  static constraints = {
    sku(unique: true)
    productSku(nullable: true)
  }

  boolean hasRange() {
    return false;
  }
}
