package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ProductMrp)
class ProductMrpTests {

    void testConstraints() {
      mockForConstraintsTests(ProductMrp)
        ProductMrp nullMrp=new ProductMrp()

        assert !nullMrp.validate()
        assert "nullable" == nullMrp.errors['kiosk']
        assert 'nullable' == nullMrp.errors['salesChannel']
        assert 'nullable' == nullMrp.errors['product']
    }

    void testUnique(){
        mockForConstraintsTests(ProductMrp)
        Kiosk k = new Kiosk(name:"kiosk01",apiKey:"pw")
        mockForConstraintsTests(Product, [new ProductBuilder(sku: "ABC").build()])
        def product = new ProductBuilder(sku: "ABC").build()
        SalesChannel c1 = new SalesChannel(name: 'channel_without_discount',description: '')

        ProductMrp productPrice=new ProductMrp(kiosk: k,salesChannel: c1,product:product,price: new Money(amount: 10))

        assert productPrice.validate()

        ProductMrp productPrice2=new ProductMrp(kiosk: k,salesChannel: c1,product:product,price: new Money(amount: 10))
        ProductMrp productPrice3=new ProductMrp(kiosk: new Kiosk(name:"kiosk02",apiKey:"pw"),salesChannel: c1,product:product,price: new Money(amount: 10))

        mockForConstraintsTests(ProductMrp, [productPrice,productPrice2,productPrice3])

        assert !productPrice2.validate()
        assert productPrice3.validate()

    }
}
