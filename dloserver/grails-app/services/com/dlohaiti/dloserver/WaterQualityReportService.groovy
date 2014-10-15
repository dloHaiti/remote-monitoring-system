package com.dlohaiti.dloserver

import au.com.bytecode.opencsv.CSVWriter
import org.joda.time.LocalDate

class WaterQualityReportService {
    def sessionFactory
    def waterQualityReadings(Readings readings, List<Parameter> parameters, def dates) {
        StringWriter sw = new StringWriter();
        CSVWriter writer = new CSVWriter(sw);

        def tableHeader=['Parameter', 'Sampling Site', 'Date', 'Reading'] as String[]
        writer.writeNext(tableHeader)
        for (parameter in parameters) {
                def sitesForParameter = parameter.samplingSites.unique()
                for (day in dates) {
                    for (site in sitesForParameter) {
                        List<String[]> row=[parameter.name]
                        row.add(site.name)
                        row.add(day.toString('dd-MMM-yy'))
                        row.add(String.valueOf(readings.averageFor(site, parameter, day.toDate())))
                        writer.writeNext(row.toArray() as String[])
                    }
                }
            }

        writer.close()
        sw.toString()
    }

    def sql(){
        final session = sessionFactory.currentSession

        final String query = 'select * from kiosk'

        final sqlQuery = session.createSQLQuery(query)

        final queryResults = sqlQuery.with {
            list()
        }

        queryResults
    }
}
