package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.utils.DateUtil
import org.joda.time.LocalDate

import java.text.SimpleDateFormat

class CsvReportController {
    def configurationService
    def outstandingPaymentService
    def salesReportService
    def readingsReportService
    def volumeReportService
    def waterQualityReportService
    def customerDataReportService;
    def receiptsService;

    def index() {
        def kioskName = request.kiosk.name
        redirect(action: "waterQuality", params: [kioskName: kioskName])
    }

    def waterQuality() {
        Kiosk kiosk = Kiosk.findByName(params.kioskName)
        [kioskName: kiosk.name]
    }

    def csvWaterQuality() {
        Kiosk kiosk = Kiosk.findByName(params.kioskName)
        def format = new SimpleDateFormat("yyyy-MM-dd")
        def fromDate, toDate
        if (params.fromDate == null || params.fromDate.toString() == "") {
            fromDate = new LocalDate()
        } else {
            Date input = format.parse(params.fromDate)
            fromDate = new LocalDate(input)
        }

        if (params.toDate == null || params.toDate.toString() == "") {
            toDate = new LocalDate()
        } else {
            Date input = format.parse(params.toDate)
            toDate = new LocalDate(input)
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
        render(contentType: "text/csv", text: data)

    }

    /**
     * Handles the customer report data.
     */
    def customer() {
        Kiosk kiosk = Kiosk.findByName(params.kioskName)
        log.debug "Kiosk " + kiosk.getName()
        [kioskName: kiosk.name]
    }

    /**
     * Generates the customer report in to the CSV format.
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

        // Finding the dates in given range
        def days = DateUtil.getDatesBetween(startOfMonth, endOfMonth)

        // Get the customers associated to the respective KIOSK
        def customers = customerDataReportService.getCustomersByKiosk(kiosk)

        // Get the receipts within the given date range
        def receipts = receiptsService.getReceiptsByCustomerInDateRange(customers, startOfMonth, endOfMonth)

        // Generate the CSV Report with the receipts data (It has internally customer data and sales data)
        def text = customerDataReportService.generateCustomerReport(receipts, days,customers);
        render(contentType: "text/csv", text: text)
    }
}