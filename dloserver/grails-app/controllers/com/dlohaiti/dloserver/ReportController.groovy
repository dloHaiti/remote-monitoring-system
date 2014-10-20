package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.utils.DateUtil
import org.joda.time.LocalDate

class ReportController {

    def configurationService
    def outstandingPaymentService
    def salesReportService
    def readingsReportService
    def volumeReportService
    def waterQualityReportService
    def customerDataReportService;
    def receiptsService;


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

    /**
     * Handles the customer report request.
     */
    def customer() {
        Kiosk kiosk = Kiosk.findByName(params.kioskName)
        log.debug "Kiosk " + kiosk.getName()
        [kioskName: kiosk.name]
    }

    /**
     * Generates the customer report in CSV format.
     */
    def csvCustomerReport() {
        Kiosk kiosk = Kiosk.findByName(params.kioskName)
        def fromDate, toDate
        if (params.fromDate == null || params.fromDate.toString() == "") {
            fromDate = new LocalDate()
        } else {
            fromDate = new LocalDate(params.fromDate);
        }
        response.setHeader("Content-disposition", "attachment; filename=customerReport.csv")
        // Calculating the start date and end date of the month
        LocalDate endOfMonth = fromDate.dayOfMonth().withMaximumValue();
        LocalDate startOfMonth = fromDate.dayOfMonth().withMinimumValue();
        // Get the customers associated to the respective KIOSK
        def customers = customerDataReportService.getCustomersByKiosk(kiosk)
        // Get the receipts within the given date range
        def receipts = receiptsService.getReceiptsByCustomerInDateRange(customers, startOfMonth, endOfMonth)
        // Finding the dates in given range
        def days = DateUtil.getDatesBetween(startOfMonth, endOfMonth)
        // Generate the CSV Report with the receipts data (It has internally customer data and sales data)
        def text = customerDataReportService.generateCustomerReport(receipts, days, customers);
        render(contentType: "text/csv", text: text)
    }

}