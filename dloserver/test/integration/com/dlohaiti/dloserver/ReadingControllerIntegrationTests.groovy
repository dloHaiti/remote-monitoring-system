package com.dlohaiti.dloserver

class ReadingControllerIntegrationTests extends GroovyTestCase {

    ReadingController controller = new ReadingController()

    protected void reset() {
        controller.request.clearAttributes()
        controller.request.removeAllParameters()
        controller.response.committed = false
        controller.response.resetBuffer()
    }

    void testValidData() {
        controller.request.json = '{"reading":{"timestamp":"2013-04-24 12:00:01 EDT","measurements":[' +
                '{"parameter":"PH","location":"BOREHOLE","value":"5"},' +
                '{"parameter":"COLOR","location":"WTU_EFF","value":"OK"}' +
                ']}}'

        controller.save()

        assert '{"msg":"OK"}' == controller.response.contentAsString
        assert 201 == controller.response.status
        assert 1 == Reading.count()
        assert 2 == Reading.first().measurements.size()
    }

    void testShouldUpdateDataWithTheSameTimeStamp() {
        String timestamp = "2013-04-24 12:00:02 EDT"
        controller.request.json = '{"reading":{"timestamp":"' + timestamp +'","measurements":[' +
                '{"parameter":"PH","location":"BOREHOLE","value":"5"},' +
                '{"parameter":"COLOR","location":"WTU_EFF","value":"OK"}' +
                ']}}'
        controller.save()
        assert '{"msg":"OK"}' == controller.response.contentAsString
        assert 201 == controller.response.status
        assert 1 == Reading.count()
        assert timestamp == Reading.first().timestamp.format("yyyy-MM-dd hh:mm:ss z")

        reset()

        controller.request.json = '{"reading":{"timestamp":"' + timestamp +'","measurements":[' +
                '{"parameter":"PH","location":"BOREHOLE","value":"5"},' +
                '{"parameter":"COLOR","location":"WTU_EFF","value":"OK"}' +
                ']}}'
        controller.save()
        assert '{"msg":"OK"}' == controller.response.contentAsString
        assert 201 == controller.response.status
        assert 1 == Reading.count()
        assert timestamp == Reading.first().timestamp.format("yyyy-MM-dd hh:mm:ss z")
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
