package com.dlohaiti.dloserver

import org.junit.Before
import org.junit.Test

import java.text.SimpleDateFormat

class ReadingsServiceIntegrationTests extends GroovyTestCase {

    def readingsService
    def grailsApplication

    SimpleDateFormat sdf

    Parameter temperature
    Parameter ph
    SamplingSite samplingSite

    @Before
    void setupData() {
        Country country=new Country(name: "haiti").save(failOnError: true)
        Region region = new Region(name: "Region1",country: country).save(failOnError: true)

        Kiosk kiosk = new Kiosk(name: "k1", apiKey: 'pw',region:region).save(failOnError: true)
        temperature = new Parameter(name: "temptest", isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: true).save(failOnError: true)
        ph = new Parameter(name: "pHtest", minimum: 4, maximum: 9, isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: true).save(failOnError: true)
        samplingSite = new SamplingSite(name: "Borehole").save(failOnError: true)
        new Sensor(sensorId: "s1", kiosk: kiosk, parameter: temperature, displayName: "S1", samplingSite: samplingSite).save(failOnError: true)
        new Sensor(sensorId: "s2", kiosk: kiosk, parameter: ph, displayName: "S2", samplingSite: samplingSite).save(failOnError: true)

        sdf = new SimpleDateFormat(grailsApplication.config.dloserver.measurement.timeformat.toString())

        // solar stuff
        Parameter kwh = new Parameter(name: 'kWh', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
        Parameter floatTime = new Parameter(name: 'Float Time (h:m)', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
        Parameter highPower = new Parameter(name: 'High Power (kW)', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
        Parameter highTemp = new Parameter(name: 'High Temp (C)', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
        Parameter vin = new Parameter(name: 'Vin', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
        Parameter vbatt = new Parameter(name: 'Vbatt', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
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

        assert Reading.count() == 2
        assert Reading.first().createdDate == sdf.parse("2013-12-12 00:01:02 EDT")
        assert Measurement.findByParameter(ph).value == 8
        assert Measurement.findByParameter(temperature).value == 20
    }

    @Test
    void shouldImportValidSolarFile() {
        createIncomingFile("Mon_May_6_2013.csv") <<
                """KioskId,Date,kWh,Time,Float Time (h:m),High Power (kW),High Temp (C),Vin,Vbatt,,
k1,5/5/2013,0.7,23:59,05:04,0.583,34,96.5,60.7,,
k1,5/4/2013,0.4,23:59,04:26,0.46,32.6,108,60.6,,"""
        readingsService.importIncomingFiles()

        assert Reading.count() == 2
        assert Measurement.count() == 12
    }

    @Test
    void shouldIgnoreAlreadyImportedMeasurements() {
        createIncomingFile("file1.csv") <<
                """s1,2013-12-12 00:01:02 EDT,20
s2,2013-12-12 00:01:02 EDT,8
"""
        createIncomingFile("file_with_duplicated_measurement.csv") <<
                """s1,2013-12-12 00:01:02 EDT,25
s2,2013-12-12 05:00:00 EDT,9
"""
        readingsService.importIncomingFiles()

        assert Reading.count() == 3
        assert Measurement.countByParameter(temperature) == 1
        assert Measurement.countByParameter(ph) == 2
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

        assert Reading.count() == 4
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

        assert Reading.count() == 2 //one for each line in good_file.csv
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
