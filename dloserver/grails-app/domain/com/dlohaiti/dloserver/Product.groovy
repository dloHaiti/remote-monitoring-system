package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode(includes = ['sku', 'description', 'gallons', 'price', 'minimumQuantity', 'maximumQuantity', 'base64EncodedImage', 'active'])
class Product {
  String sku
  String description
  Integer gallons
  Money price
  Integer minimumQuantity
  Integer maximumQuantity
  String base64EncodedImage
  Boolean active

  static embedded = ['price']

  static belongsTo = [category: ProductCategory]

  static mapping = {
    base64EncodedImage type: "text"
  }

  static constraints = {
    sku(blank: false, unique: true)
    description(blank: false)
    price(validator: { val, obj ->
      BigDecimal.ZERO.compareTo(obj.price.amount) < 1
    })
    minimumQuantity(nullable: true, min: 0)
    maximumQuantity(nullable: true, validator: { val, obj ->
      (obj.maximumQuantity == null || obj.minimumQuantity < obj.maximumQuantity)
    })
  }

  boolean requiresQuantity() {
    return minimumQuantity != null || maximumQuantity != null
  }
}
