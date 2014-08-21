package com.dlohaiti.dloserver
import au.com.bytecode.opencsv.CSVReader
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test

class SolarFileParserTest {

  SolarFileParser parser
  Kiosk kiosk
  Parameter kwh
  Parameter floatTime
  Parameter highPower
  Parameter highTemp
  Parameter vin
  Parameter vbatt

  @Before void setUp() {
    parser = new SolarFileParser()
     Country country=new Country(name: "haiti").save(failOnError: true)
    Region region = new Region(name: "Region1",country: country).save(failOnError: true)
    kiosk = new Kiosk(name: "testkiosk01", apiKey: "pw",region:region).save(failOnError: true)
    kwh = new Parameter(name: 'kWh', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
    floatTime = new Parameter(name: 'Float Time (h:m)', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
    highPower = new Parameter(name: 'High Power (kW)', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
    highTemp = new Parameter(name: 'High Temp (C)', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
    vin = new Parameter(name: 'Vin', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
    vbatt = new Parameter(name: 'Vbatt', isUsedInTotalizer: false, isOkNotOk: false, active: true, manual: false).save(failOnError: true)
  }

  @Test void shouldKnowIfItLooksLikeASolarFile() {
    def csvReader = new CSVReader(new StringReader("""KioskId,Date,kWh,Time,Float Time (h:m),High Power (kW),High Temp (C),Vin,Vbatt,,
testkiosk01,5/5/2013,0.7,23:59,05:04,0.583,34,96.5,60.7,,
testkiosk01,5/4/2013,0.4,23:59,04:26,0.46,32.6,108,60.6,,"""))

    assert parser.isSolarFile(csvReader.readNext())
  }

  @Test void shouldKnowIfItDoesNotLookLikeASolarFile() {
    def csvReader = new CSVReader(new StringReader("""s1,2013-12-12 00:01:02 EDT,20
s2,2013-12-12 00:01:02 EDT,8
"""))

    assert !parser.isSolarFile(csvReader.readNext())
  }

  @Test void shouldGiveBackAListOfReadings() {
    def csvReader = new CSVReader(new StringReader("""KioskId,Date,kWh,Time,Float Time (h:m),High Power (kW),High Temp (C),Vin,Vbatt,,
testkiosk01,5/5/2013,0.7,23:59,05:04,0.583,34,96.5,60.7,,
testkiosk01,5/4/2013,0.4,23:59,04:26,0.46,32.6,108,60.6,,"""))

    List<Reading> readings = parser.parse(csvReader.readAll())

    assert 2 == readings.size()

    def firstReading = readings[0]
    def secondReading = readings[1]
    assert kiosk == firstReading.kiosk
    assert new DateTime(2013, 5, 5, 23, 59).toDate() == firstReading.createdDate
    assert new DateTime(2013, 5, 4, 23, 59).toDate() == secondReading.createdDate
    assert "testkiosk01 solar sensor" == firstReading.username
  }

  @Test void shouldHaveCorrectMeasurementsRecorded() {
    def csvReader = new CSVReader(new StringReader("""KioskId,Date,kWh,Time,Float Time (h:m),High Power (kW),High Temp (C),Vin,Vbatt,,
testkiosk01,5/5/2013,0.7,23:59,05:04,0.583,34,96.5,60.7,,
testkiosk01,5/4/2013,0.4,23:59,04:26,0.46,32.6,108,60.6,,"""))

    List<Reading> readings = parser.parse(csvReader.readAll())

    def firstReadingMeasurements = readings[0].measurements
    assert 6 == firstReadingMeasurements.size()
    assert 0.7G == firstReadingMeasurements.find({m -> m.parameter == kwh}).value
    assert 304G == firstReadingMeasurements.find({m -> m.parameter == floatTime}).value //we save this as minutes
    assert 0.583G == firstReadingMeasurements.find({m -> m.parameter == highPower}).value
    assert 34G == firstReadingMeasurements.find({m -> m.parameter == highTemp}).value
    assert 96.5G == firstReadingMeasurements.find({m -> m.parameter == vin}).value
    assert 60.7G == firstReadingMeasurements.find({m -> m.parameter == vbatt}).value
  }
}
