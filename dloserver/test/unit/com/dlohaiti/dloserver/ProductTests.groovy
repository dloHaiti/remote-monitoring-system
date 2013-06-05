package com.dlohaiti.dloserver

import grails.test.mixin.TestFor
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Product)
class ProductTests {

  void testConstraints() {
    mockForConstraintsTests(Product)

    def nullProduct = new Product()
    assert !nullProduct.validate()
    assert 'nullable' == nullProduct.errors['sku']
    assert 'nullable' == nullProduct.errors['description']
    assert 'nullable' == nullProduct.errors['gallons']
    assert 'nullable' == nullProduct.errors['price']

    def blankProduct = new ProductBuilder(sku: '', description: '', reportingCategory: '').build()
    assert !blankProduct.validate()
    assert 'blank' == blankProduct.errors['sku']
    assert 'blank' == blankProduct.errors['description']

    def negativePriceProduct = new ProductBuilder(price: new Money(amount: -1)).build()
    assert !negativePriceProduct.validate()
    assert 'validator' == negativePriceProduct.errors['price']

    def negativeMinimumQuantityProduct = new ProductBuilder(minimumQuantity: -10).build()
    assert !negativeMinimumQuantityProduct.validate()
    assert 'min' == negativeMinimumQuantityProduct.errors['minimumQuantity']

    def maxLessThanMinProduct = new ProductBuilder(minimumQuantity: 10, maximumQuantity: 5).build()
    assert !maxLessThanMinProduct.validate()
    assert 'validator' == maxLessThanMinProduct.errors['maximumQuantity']
  }

  void testUniqueSkuConstraint() {
    mockForConstraintsTests(Product, [new ProductBuilder(sku: "ABC").build()])

    def product = new ProductBuilder(sku: "ABC").build()
    assert !product.validate()
    assert 'unique' == product.errors['sku']
  }
}
