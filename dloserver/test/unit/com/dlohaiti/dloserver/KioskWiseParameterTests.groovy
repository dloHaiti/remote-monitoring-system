package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(KioskWiseParameter)
class KioskWiseParameterTests {

    void testConstraints() {
        mockForConstraintsTests(KioskWiseParameter)
        KioskWiseParameter nullObj=new KioskWiseParameter()

        assert !nullObj.validate()
        assert "nullable" == nullObj.errors['kiosk']
        assert 'nullable' == nullObj.errors['samplingSite']
        assert 'nullable' == nullObj.errors['parameter']
    }
}
