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
    void setupData() {
        Kiosk kiosk = new Kiosk(name: "k1").save()
        new Sensor(sensorId: "s1", kiosk: kiosk, measurementType: temperature, displayName: "S1", location: location).save()
        new Sensor(sensorId: "s2", kiosk: kiosk, measurementType: ph, displayName: "S2", location: location).save()

        sdf = new SimpleDateFormat(grailsApplication.config.dloserver.measurement.timeformat.toString())
    }

    @Before
    void clearFolders() {
        def readingsFolders = grailsApplication.config.dloserver.readings
        [readingsFolders.incoming, readingsFolders.processed, readingsFolders.failed].each {
            new File(it.toString()).deleteDir()
            new File(it.toString()).mkdirs()
        }
    }

    @Test
    void shouldImportValidFile() {
        createIncomingFile("file1.csv") <<
                """s1,2013-12-12 00:01:02 EDT,20
s2,2013-12-12 00:01:02 EDT,8
"""
        readingsService.importIncomingFiles()

        assert Reading.count() == 1
        assert Measurement.findByParameter(ph).value == 8
        assert Measurement.findByParameter(ph).timestamp == sdf.parse("2013-12-12 00:01:02 EDT")
        assert Measurement.findByParameter(temperature).value == 20
        assert Measurement.findByParameter(temperature).timestamp == sdf.parse("2013-12-12 00:01:02 EDT")
    }

    @Test
    void shouldProcessAllValidFiles() {
        createIncomingFile("file1.csv") <<
                """s1,2013-12-12 00:01:02 EDT,20
s2,2013-12-12 00:01:02 EDT,8
"""
        createIncomingFile("file2.csv") <<
                """s1,2013-12-12 00:02:00 EDT,25
s2,2013-12-12 00:02:00 EDT,9
"""
        readingsService.importIncomingFiles()

        assert Reading.count() == 2
        assert totalProcessedFiles == 2
    }

    @Test
    void shouldRejectInvalidFilesButImportGoodOnes() {
        createIncomingFile("bad_file.csv") <<
                """s1,2013-12-12 00:01:02 EDT,20
s2,2013-12-12 00:01:02 EDT,8000
"""
        createIncomingFile("good_file.csv") <<
                """s1,2013-12-12 00:01:02 EDT,20
s2,2013-12-12 00:01:02 EDT,8
"""
        readingsService.importIncomingFiles()

        assert Reading.count() == 1
        assert Measurement.findByParameter(ph).value == 8
        assert totalProcessedFiles == 1
        assert totalFailedFiles == 1
    }

    @Test
    void shouldRejectFilesWithEmptyLines() {
        createIncomingFile("file_with_empty_line.csv") <<
                """
s1,2013-12-12 00:01:02 EDT,20
s2,2013-12-12 00:01:02 EDT,8
"""
        readingsService.importIncomingFiles()

        assert Reading.count() == 0
        assert totalFailedFiles == 1
    }

    @Test
    void shouldRejectFilesWithInvalidData() {
        createIncomingFile("file1.csv") <<
                """s4,2013-12-12 00:01:02 EDT,20
s5,2013-12-12 00:01:02 EDT,8
"""
        readingsService.importIncomingFiles()

        assert Reading.count() == 0
        assert totalFailedFiles == 1
    }

    private File createIncomingFile(String filename) {
        def dir = new File(grailsApplication.config.dloserver.readings.incoming.toString())
        def file = new File(dir, filename)
        file.delete()
        file.createNewFile()
        return file
    }

    private int getTotalFailedFiles() {
        File failedFolder = new File(grailsApplication.config?.dloserver?.readings?.failed?.toString())
        failedFolder.list()?.size()
    }

    private int getTotalProcessedFiles() {
        File processedFolder = new File(grailsApplication.config?.dloserver?.readings?.processed?.toString())
        processedFolder.list()?.size()
    }
}
