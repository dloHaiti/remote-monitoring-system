package com.dlohaiti.dloserver

/**
 * Created by kumaranv on 29/09/14.
 */
class ReadingsReportService {
    def readingsForKioskAndCreatedDateGreaterThanOrEqualTo(Kiosk kiosk, Date date) {
        return new Readings(readings: Reading.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, date))
    }

    def parameterMapForReadings(Readings readings, List<Parameter> parameters, List<Date> dates) {
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
