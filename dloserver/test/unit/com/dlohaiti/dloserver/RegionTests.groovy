package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Region)
class RegionTests {

    void testConstraints() {
        def country=new Country(name: "haiti")
        mockForConstraintsTests(Region)

        Region nullRegion = new Region(country: country)

        assert !nullRegion.validate()
        assert "nullable" == nullRegion.errors['name']
        assertNull nullRegion.errors["description"]
        nullRegion.name="sample"
        assert nullRegion.validate()
    }


}
