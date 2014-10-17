package com.dlohaiti.dloserver

import au.com.bytecode.opencsv.CSVWriter
import org.apache.commons.lang3.time.DateUtils
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 * Created by swamyb on 15/10/14.
 *
 * Maintains the activities related to the customer data report generation.
 */
class CustomerDataReportService {

    /**
     * Generates the customer report data in CSV format
     * @param receipts
     * @return
     */
    def generateCustomerReport(def receipts, def days, def customers) {
        StringWriter sw = new StringWriter();
        CSVWriter writer = new CSVWriter(sw);

        // Adding Table Header to the CSV file
        def monthHeader = ['']
        for (day in days) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM")
            monthHeader.add(formatter.print(day))
        }
        writer.writeNext(monthHeader as String[])

        // Iterating over all customers in the selected KIOSK
        for (customer in customers) {
            def content = []
            content.add(customer.getName())
            // Iterating over the days
            for (day in days) {
                def totalSales = getCustomerTotalSales(receipts, day, customer.getId())
                if (totalSales != 0)
                    content.add(totalSales)
                else
                    content.add('0')
            }
            writer.writeNext(content as String[])
        }
        writer.close()
        sw.toString()
    }

    /**
     * Processes for the customer total sales on given date.
     *
     * @param receipts
     * @param day
     * @param customerId
     * @return
     */
    def getCustomerTotalSales(def receipts, def day, def customerId) {
        int totalSaleForTheDay = 0;
        for (receipt in receipts) {
            // If Customer Id Matches
            if (receipt.getCustomerAccount().getId().equals(customerId)) {
                Date saleDate = receipt.getCreatedDate();
                // If Date Matches
                if (DateUtils.isSameDay(saleDate, day.toDate())) {
                    totalSaleForTheDay = totalSaleForTheDay + receipt.getTotalGallons()
                }
            }
        }
        return totalSaleForTheDay;
    }

    /**
     * Identifies the customers respective to the given KIOSK.
     *
     * @param kiosk
     * @return
     */
    def getCustomersByKiosk(Kiosk kiosk) {
        def customers = []
        customers.addAll(CustomerAccount.findAllByKiosk(kiosk))
        customers
    }

}