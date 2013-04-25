package com.dlohaiti.dloserver

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

class ReadingControllerIntegrationTests extends GroovyTestCase {

    ReadingController controller = new ReadingController()

    void testValidData() {
        controller.request.json = '{"reading":{"timestamp":"2013-04-24 00:00:01 EDT","measurements":[' +
                '{"parameter":"PH","location":"BOREHOLE","value":"5"},' +
                '{"parameter":"COLOR","location":"WTU_EFF","value":"OK"}' +
                ']}}'

        controller.save()

        assert '{"msg":"OK"}' == controller.response.contentAsString
        assert 201 == controller.response.status
        assert 1 == Reading.count()
        assert 2 == Reading.first().measurements.size()
    }

    void testInvalidTimestampFormat() {
        controller.request.json = '{"reading":{"timestamp":"24/12/12-00:00:01","measurements":[' +
                '{"parameter":"PH","location":"BOREHOLE","value":"5"},' +
                '{"parameter":"COLOR","location":"WTU_EFF","value":"OK"}' +
                ']}}'

        controller.save()

        println controller.response.contentAsString

        assert 422 == controller.response.status
        assert 0 == Reading.count()
    }
}
