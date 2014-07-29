package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Country)
class CountryTests {

    void testConstraints() {

        mockForConstraintsTests(Country)

        Country nullCountry = new Country()

        assert !nullCountry.validate()
        assert "nullable" == nullCountry.errors['name']
        assert 'nullable' == nullCountry.errors['unitOfMeasure']
    }

    def testForUnique(){
        mockForConstraintsTests(Country)

        Country c1 = new Country(name: 'c1',unitOfMeasure: 'litter')
        assert c1.validate()


        Country c2 = new Country(name: 'c1',unitOfMeasure: 'litter')
        mockForConstraintsTests(Country, [c1,c2])
        assert !c2.validate()
        c2.name="c2"
        assert c2.validate()

    }
}
