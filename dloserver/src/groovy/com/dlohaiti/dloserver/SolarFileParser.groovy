package com.dlohaiti.dloserver

import groovy.util.logging.Log4j

import java.text.SimpleDateFormat

@Log4j
class SolarFileParser {
  static SOLAR_DATE_FORMAT = new SimpleDateFormat("M/d/yyyy HH:mm")
  // how we tell if the file should be parsed by the SolarFileParser
  // right now we just look for the word 'vbatt' in the first row
  // there may be better ways to tell the solar files apart
  boolean isSolarFile(String[] header) {
    return isSolarHeader(header)
  }

  private boolean isSolarHeader(String[] header) {
    return header.contains("Vbatt")
  }

  List<Reading> parse(List<String[]> allLines) {
    List<Reading> readings = []
    for(line in allLines) {
      if(isSolarHeader(line)) {
        continue // don't process the header
      }
      Kiosk kiosk = Kiosk.findByName(line[0]) // we expect the first item in each line to be the kioskid
      if(kiosk == null) {
        log.error("Solar reading for Kiosk [${line[0]}]. Kiosk not found.")
        break // stop processing, IncomingService will move it to the failed folder
      }
      Date createdDate = SOLAR_DATE_FORMAT.parse("${line[1]} ${line[3]}") // we expect the 2nd column to be the date and the 4th column to be the time

      // make the reading
      def reading = new Reading(kiosk: kiosk, createdDate: createdDate, username: "${kiosk.name} solar sensor")
      // add all the expected measurements to it based on their known positions in the line
      // we're assuming these parameters exist in the database
      reading.addToMeasurements(new Measurement(parameter: Parameter.findByName("kWh"), value: new BigDecimal(line[2])))
      reading.addToMeasurements(new Measurement(parameter: Parameter.findByName("Float Time (h:m)"), value: parseMinutes(line[4])))
      reading.addToMeasurements(new Measurement(parameter: Parameter.findByName("High Power (kW)"), value: new BigDecimal(line[5])))
      reading.addToMeasurements(new Measurement(parameter: Parameter.findByName("High Temp (C)"), value: new BigDecimal(line[6])))
      reading.addToMeasurements(new Measurement(parameter: Parameter.findByName("Vin"), value: new BigDecimal(line[7])))
      reading.addToMeasurements(new Measurement(parameter: Parameter.findByName("Vbatt"), value: new BigDecimal(line[8])))
      readings.add(reading)
    }
    return readings
  }

  private BigDecimal parseMinutes(String hoursAndMinutes) {
    def split = hoursAndMinutes.split(":")
    BigDecimal hours = new BigDecimal(split[0])
    BigDecimal minutes = new BigDecimal(split[1]) + (60 * hours)
    return minutes
  }
}
