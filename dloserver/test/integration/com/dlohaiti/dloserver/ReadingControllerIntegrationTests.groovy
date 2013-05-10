package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.endpoint.ReadingController
import org.junit.BeforeClass
import org.junit.Test

class ReadingControllerIntegrationTests extends GroovyTestCase {

    ReadingController controller = new ReadingController()

    @BeforeClass
    static void setup() {
        new Kiosk(name: "k1").save()
    }

    @Test
    void shouldSaveValidDataToDb() {
        controller.request.json = '{"kiosk":"k1", "timestamp":"2013-04-24 12:00:01 EDT","measurements":[' +
                '{"parameter":"PH","location":"BOREHOLE","value":"5"},' +
                '{"parameter":"COLOR","location":"BOREHOLE","value":"OK"}' +
                ']}'

        controller.save()

        assert '{"msg":"OK"}' == controller.response.contentAsString
        assert 201 == controller.response.status
        assert 1 == Reading.count()

        Reading savedReading = Reading.first()
        assert 2 == savedReading.measurements.size()

        def ph = savedReading.measurements.find { it.parameter.name == "pH" }
        assert ph.location.name == "Borehole"
        assert ph.timestamp == savedReading.timestamp
        assert ph.value == 5

        def color = savedReading.measurements.find { it.parameter.name == "Color" }
        assert color.value == 1
    }

    @Test
    void shouldUpdateDataWithTheSameTimeStamp() {
        String timestamp = "2013-04-24 12:00:02 EDT"
        controller.request.json = '{"kiosk":"k1", "timestamp":"' + timestamp +'","measurements":[' +
                '{"parameter":"PH","location":"BOREHOLE","value":"5"},' +
                '{"parameter":"COLOR","location":"WTU_EFF","value":"OK"}' +
                ']}'
        controller.save()
        assert '{"msg":"OK"}' == controller.response.contentAsString
        assert 201 == controller.response.status
        assert 1 == Reading.count()
        assert timestamp == Reading.first().timestamp.format("yyyy-MM-dd hh:mm:ss z")
        assert 2 == Reading.first().measurements.size()

        reset()

        controller.request.json = '{"kiosk":"k1", "timestamp":"' + timestamp +'","measurements":[' +
                '{"parameter":"PH","location":"BOREHOLE","value":"5"},' +
                '{"parameter":"COLOR","location":"BOREHOLE","value":"OK"}' +
                ']}'
        controller.save()
        assert '{"msg":"OK"}' == controller.response.contentAsString
        assert 201 == controller.response.status
        assert 1 == Reading.count()
        assert timestamp == Reading.first().timestamp.format("yyyy-MM-dd hh:mm:ss z")
        assert 2 == Reading.first().measurements.size()
    }

    @Test
    void shouldRejectInvalidTimestampFormat() {
        controller.request.json = '{"kiosk":"k1", "timestamp":"24/12/12-00:00:01","measurements":[' +
                '{"parameter":"PH","location":"BOREHOLE","value":"5"},' +
                '{"parameter":"COLOR","location":"BOREHOLE","value":"OK"}' +
                ']}'

        controller.save()

        println controller.response.contentAsString

        assert 422 == controller.response.status
        assert 0 == Reading.count()
    }

    @Test
    void shouldRejectMissingKiosk() {
        controller.request.json = '{"timestamp":"2013-04-24 12:00:01 EDT","measurements":[' +
                '{"parameter":"PH","location":"BOREHOLE","value":"5"},' +
                '{"parameter":"COLOR","location":"BOREHOLE","value":"OK"}' +
                ']}'

        controller.save()

        println controller.response.contentAsString

        assert 422 == controller.response.status
        assert 0 == Reading.count()
    }

    private void reset() {
        controller.request.clearAttributes()
        controller.request.removeAllParameters()
        controller.response.committed = false
        controller.response.resetBuffer()
    }
}
