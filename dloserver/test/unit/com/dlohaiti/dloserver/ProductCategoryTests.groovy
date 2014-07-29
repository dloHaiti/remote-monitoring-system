package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ProductCategory)
class ProductCategoryTests {

    void testConstraints() {

        mockForConstraintsTests(ProductCategory)

        ProductCategory category = new ProductCategory()

        assert !category.validate()
        assert "nullable" == category.errors['name']
        assertNull category.errors["description"]
    }

}
