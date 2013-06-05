package com.dlohaiti.dloserver

class Product {
  String sku
  String description
  Integer gallons
  Money price
  String reportingCategory
  Integer minimumQuantity
  Integer maximumQuantity

  static embedded = ['price']

  static constraints = {
    sku(blank: false, unique: true)
    description(blank: false)
    price(validator: { val, obj ->
      BigDecimal.ZERO.compareTo(obj.price.amount) < 1
    })
    reportingCategory(blank: false)
    minimumQuantity(nullable: true, min: 0)
    maximumQuantity(nullable: true, validator: { val, obj ->
      (obj.maximumQuantity == null || obj.minimumQuantity < obj.maximumQuantity)
    })
  }
}
