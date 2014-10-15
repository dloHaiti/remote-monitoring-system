package com.dlohaiti.dloserver

import au.com.bytecode.opencsv.CSVWriter

/**
 * Created by swamyb on 15/10/14.
 */
class CustomerDataReportService {

    def receiptsService;
    def salesReportService;

    /**
     * Generates the customer report data in CSV format
     * @param receipts
     * @return
     */
    def generateCustomerReport(List<Receipt> receipts) {
        StringWriter sw = new StringWriter();
        CSVWriter writer = new CSVWriter(sw);

        def tableHeader = ['CustomerName', 'ContactName', 'PhoneNumber', 'Address', 'Sale Date', 'Sale Channel',
                           'Customer Amount', 'Sponsor Name', 'Sponsor Amount', 'Products'] as String[]
        // Adding Table Header to the CSV file
        writer.writeNext(tableHeader)
        //Iterating over the receipts data and adding it to the StringWriter
        int i = 0;
        for (receipt in receipts) {
            List<String[]> row = new ArrayList<String[]>();
            // Adding the report data to the CSV file
            if (receipt.getCustomerAccount() != null) {
                row.add(receipt.getCustomerAccount().getName())
                row.add(receipt.getCustomerAccount().getContactName())
                row.add(receipt.getCustomerAccount().getPhoneNumber())
                row.add(receipt.getCustomerAccount().getAddress())
            }
            row.add(receipt.getCreatedDate().toString('dd-MMM-yy'))
            row.add(receipt.getSalesChannel().getName())
            row.add(receipt.getCustomerAmount())
            if (receipt.getSponsor() != null)
                row.add(receipt.getSponsor().getName())
            row.add(receipt.getSponsorAmount())
            // If it is a PRODUCT then only adding to the result data
            for (receiptLineItem in receipt.getReceiptLineItems()) {
                if (receiptLineItem.getType().equalsIgnoreCase("PRODUCT"))
                    row.add(receiptLineItem.getSku());
            }
            writer.writeNext(row.toArray() as String[])
            i++;
        }
        writer.close()
        sw.toString()
    }
}