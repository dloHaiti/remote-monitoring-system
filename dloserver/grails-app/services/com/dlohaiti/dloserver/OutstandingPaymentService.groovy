package com.dlohaiti.dloserver

/**
 * Created by kumaranv on 29/09/14.
 */
class OutstandingPaymentService {


    def outStandingPaymentsAtKiosk(Kiosk kiosk, String currencyCode,def customerNameMessage, def contactNameMessage,def dueAmountMessage) {
        def tableHeader=[customerNameMessage, contactNameMessage, dueAmountMessage]
        def customerAccounts = CustomerAccount.findAllByKioskAndDueAmountGreaterThan(kiosk, 0);
        def tableData = [tableHeader]
        customerAccounts.each {customer -> tableData.add([customer.name, customer.contactName,customer.dueAmount])}
        tableData
    }
}