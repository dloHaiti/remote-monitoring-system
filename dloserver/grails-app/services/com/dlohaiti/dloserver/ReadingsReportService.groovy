package com.dlohaiti.dloserver

import org.apache.tomcat.jni.Local
import org.joda.time.LocalDate

/**
 * Created by kumaranv on 29/09/14.
 */
class ReadingsReportService {
    def readingsForKioskAndCreatedDateGreaterThanOrEqualTo(Kiosk kiosk, LocalDate fromDate,LocalDate toDate) {
        return new Readings(readings: Reading.findAllByKioskAndCreatedDateGreaterThanEqualsAndCreatedDateLessThan(kiosk, fromDate.toDate(),toDate.toDate()))
    }

    def parameterMapForReadings(Readings readings, List<Parameter> parameters, def dates) {
        def paramMap = [:]
        for (parameter in parameters) {
            def sitesForParameter = parameter.samplingSites.unique()
            paramMap[parameter.name] = []
            def header = ['Dates']
            for (site in sitesForParameter) {
                header.add(site.name)
            }
            paramMap[parameter.name].add(header)

            for (day in dates) {
                def row = [day.toString('dd-MMM-yy')]
                for (site in sitesForParameter) {
                    row.add(readings.averageFor(site, parameter, day.toDate()))
                }
                paramMap[parameter.name].add(row)
            }
        }
        return paramMap
    }
}
