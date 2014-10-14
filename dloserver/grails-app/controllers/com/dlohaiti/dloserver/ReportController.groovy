package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.utils.DateUtil
import org.joda.time.*
import au.com.bytecode.opencsv.CSVWriter
import java.text.SimpleDateFormat

class ReportController {

    def configurationService
    def outstandingPaymentService
    def salesReportService
    def readingsReportService
    def volumeReportService
    def waterQualityReportService
    def index() {
        [kioskName: request.kiosk.name]
        String filterType = params.filterType != null ? params.filterType : 'kiosk';
        String filterParam = params.filterParam != null ? params.filterParam : 'sku';
        String filterTimeLine = params.timeLine != null ? params.timeLine : 'currentWeek';
        // Getting the fromDate and toDate based on the week string
        LocalDate fromDate = DateUtil.getFromDateByWeekString(filterTimeLine)
        LocalDate toDate = DateUtil.getToDateByWeekString(filterTimeLine);
        def model = volumeReportService.volumeReportData(request.kiosk.name, filterType, filterParam, fromDate, toDate)
        render(view: 'volume', model: model)
    }

    def menu() {
        [kioskName: request.kiosk.name]
    }

    def outstandingPayment() {
        [kioskName: request.kiosk.name]
        Kiosk kiosk = Kiosk.findByName(params.kioskName)
        def currencyCode = configurationService.currencyCode
        def tableData = outstandingPaymentService.outStandingPaymentsAtKiosk(kiosk, currencyCode)
        [kioskName: kiosk.name, tableData: tableData]
    }

    def readings() {
        Kiosk kiosk = Kiosk.findByName(params.kioskName)
        String filterTimeLine = params.timeLine != null ? params.timeLine : 'currentWeek';
        // Getting the fromDate and toDate based on the week string
        LocalDate fromDate = DateUtil.getFromDateByWeekString(filterTimeLine)
        LocalDate toDate = DateUtil.getToDateByWeekString(filterTimeLine);

        def days = DateUtil.getWeekDataByFromDate(fromDate, toDate);
        def readings = readingsReportService.readingsForKioskAndCreatedDateGreaterThanOrEqualTo(kiosk, fromDate, toDate)
        def parameters = kiosk.getParameters()
        def paramMap = readingsReportService.parameterMapForReadings(readings, parameters, days)
        def model = [kioskName                         : request.kiosk.name, parameters: parameters, lastWeek: days
                .collect({ d -> d.toDate() }), readings: readings, paramMap: paramMap]

        render(view: 'readings', model: model)
    }

    def sales() {
        String filterType = params.filterType != null ? params.filterType : '';
        String filterParam = params.filterParam != null ? params.filterParam : '';
        String filterTimeLine = params.timeLine != null ? params.timeLine : 'currentWeek';
        // Getting the fromDate and toDate based on the week string
        LocalDate fromDate = DateUtil.getFromDateByWeekString(filterTimeLine)
        LocalDate toDate = DateUtil.getToDateByWeekString(filterTimeLine);
        def model = salesReportService.salesData(params.kioskName, filterType, filterParam, fromDate, toDate)
        render(view: 'sales', model: model)
    }

    def volume() {
        String filterType = params.filterType != null ? params.filterType : 'kiosk';
        String filterParam = params.filterParam != null ? params.filterParam : 'sku';
        String filterTimeLine = params.timeLine != null ? params.timeLine : 'currentWeek';
        // Getting the fromDate and toDate based on the week string
        LocalDate fromDate = DateUtil.getFromDateByWeekString(filterTimeLine)
        LocalDate toDate = DateUtil.getToDateByWeekString(filterTimeLine);
        def model = volumeReportService.volumeReportData(params.kioskName, filterType, filterParam, fromDate, toDate)
        render(view: 'volume', model: model)
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
        def days = DateUtil.getWeekDataByFromDate(fromDate, toDate);
        def readings = readingsReportService.readingsForKioskAndCreatedDateGreaterThanOrEqualTo(kiosk, fromDate, toDate)
        def parameters = kiosk.getParameters()
        def data = waterQualityReportService.waterQualityReadings(readings, parameters, days)
        StringWriter sw = new StringWriter();
        CSVWriter writer = new CSVWriter(sw);
        writer.writeAll(data);
        writer.close()
        render(contentType: "text/csv", text:sw.toString())
    }
}