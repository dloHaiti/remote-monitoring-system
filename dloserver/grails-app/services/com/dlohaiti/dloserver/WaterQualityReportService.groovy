package com.dlohaiti.dloserver

import org.joda.time.LocalDate

class WaterQualityReportService {
    def waterQualityReadings(Readings readings, List<Parameter> parameters, def dates) {
        def tableHeader=['Parameter', 'Sampling Site', 'Date', 'Reading']
        def tableData = [tableHeader]
        for (parameter in parameters) {
                def sitesForParameter = parameter.samplingSites.unique()
                for (day in dates) {
                    for (site in sitesForParameter) {
                        def row=[parameter.name]
                        row.add(site.name)
                        row.add(day.toString('dd-MMM-yy'))
                        row.add(String.valueOf(readings.averageFor(site, parameter, day.toDate())))
                        tableData.add(row)
                    }
                }
            }
        tableData
    }
}
