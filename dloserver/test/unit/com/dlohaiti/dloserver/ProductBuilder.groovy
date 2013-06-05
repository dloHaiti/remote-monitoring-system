package com.dlohaiti.dloserver

class ProductBuilder {
  String sku = "ABC"
  String description = "A thing for sale"
  Integer gallons = 10
  Money price = new Money(amount: BigDecimal.ONE)
  String reportingCategory = "FYO"
  Integer minimumQuantity
  Integer maximumQuantity

  Product build() {
    return new Product(
        sku: sku,
        description: description,
        gallons: gallons,
        price: price,
        reportingCategory: reportingCategory,
        minimumQuantity: minimumQuantity,
        maximumQuantity: maximumQuantity
    )
  }
}
