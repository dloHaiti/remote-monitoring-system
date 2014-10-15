package com.dlohaiti.dloserver

import org.joda.time.LocalDate

import java.text.SimpleDateFormat
import com.dlohaiti.dloserver.utils.DateUtil
import org.joda.time.*
import au.com.bytecode.opencsv.CSVWriter
import java.text.SimpleDateFormat

class CsvReportController {
    def configurationService
    def outstandingPaymentService
    def salesReportService
    def readingsReportService
    def volumeReportService
    def waterQualityReportService

    def index() {
       def  kioskName =  request.kiosk.name
        redirect(action: "waterQuality", params:[kioskName: kioskName])
    }

    def waterQuality(){
        Kiosk kiosk = Kiosk.findByName(params.kioskName)
        [kioskName: kiosk.name]
    }

    def csvWaterQuality(){
        Kiosk kiosk = Kiosk.findByName(params.kioskName)
        def format = new SimpleDateFormat("yyyy-MM-dd")
        def fromDate,toDate
        if(params.fromDate == null || params.fromDate.toString() == ""){
            fromDate = new LocalDate()
        }else{
            Date input =format.parse(params.fromDate)
            fromDate=new LocalDate(input)
        }

        if(params.toDate == null || params.toDate.toString() == ""){
            toDate = new LocalDate()
        }else{
            Date input =format.parse(params.toDate)
            toDate=new LocalDate(input)
        }
        response.setHeader("Content-disposition", "attachment; filename=waterQuality.csv")
        def days = DateUtil.getDatesBetween(fromDate, toDate)
        def readings = readingsReportService.readingsForKioskAndCreatedDateGreaterThanOrEqualTo(kiosk, fromDate, toDate)
        def parameters = kiosk.getParameters()
        def data = waterQualityReportService.waterQualityReadings(readings, parameters, days)
//        StringWriter sw = new StringWriter();
//        CSVWriter writer = new CSVWriter(sw);
//        writer.writeAll(data)
//        writer.close()
        render(contentType: "text/csv", text:data)

    }
}
