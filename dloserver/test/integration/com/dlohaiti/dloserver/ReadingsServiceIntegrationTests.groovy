package com.dlohaiti.dloserver

import org.junit.Before
import org.junit.Test

import java.text.SimpleDateFormat

class ReadingsServiceIntegrationTests extends GroovyTestCase {

    def readingsService
    def grailsApplication

    private SimpleDateFormat sdf

    private MeasurementType temperature = MeasurementType.findByName("Temperature")
    private MeasurementType ph = MeasurementType.findByName("pH")
    private Location location = Location.first()

    @Before
    void setup() {
        Kiosk kiosk = new Kiosk(name: "k1").save()
        Sensor s1 = new Sensor(sensorId: "s1", kiosk: kiosk, measurementType: temperature, displayName: "S1", location: location).save()
        Sensor s2 = new Sensor(sensorId: "s2", kiosk: kiosk, measurementType: ph, displayName: "S2", location: location).save()

        sdf = new SimpleDateFormat(grailsApplication.config.dloserver.measurement.timeformat.toString())
    }

    @Test
    void shouldInject() {
        createIncomingFile("file1.csv") <<
"""
s1,2013-12-12 00:01:02 EDT,20
s2,2013-12-12 00:01:02 EDT,8
"""
        readingsService.importIncomingFiles()

        assert Reading.count() == 1
        assert Measurement.findByParameter(ph).value == 8
        assert Measurement.findByParameter(ph).timestamp == sdf.parse("2013-12-12 00:01:02 EDT")
        assert Measurement.findByParameter(temperature).value == 20
        assert Measurement.findByParameter(temperature).timestamp == sdf.parse("2013-12-12 00:01:02 EDT")

    }

    private File createIncomingFile(String filename) {
        def dir = new File(grailsApplication.config.dloserver.readings.incoming.toString())
        def file = new File(dir, filename)
        file.createNewFile()
        return file
    }
}
