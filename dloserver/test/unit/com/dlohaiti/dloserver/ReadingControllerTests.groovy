package com.dlohaiti.dloserver

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

@TestFor(ReadingController)
@TestMixin(DomainClassUnitTestMixin)
class ReadingControllerTests {

    void testPut() {
        mockDomain(Reading, [])

        request.json = 'reading: {"timestamp":"2013-04-24 10:31:42 EDT","measurements":[{"parameter":"PH","location":"BOREHOLE","value":"5"}]}'

        controller.update()

        println response.text
        assert "[]" == response.text
    }

    void testIndex() {
        mockDomain(Reading, [])

        controller.show()

        assert "[]" == response.text
    }
}
