package com.dlohaiti.dloserver

/**
 * Created by kumaranv on 29/09/14.
 */
class OutstandingPaymentService {


    def outStandingPaymentsAtKiosk(Kiosk kiosk, String currencyCode) {
        def tableHeader=['Customer Name', 'Contact Name', 'Due Amount (in ' + currencyCode + ')']
        def customerAccounts = CustomerAccount.findAllByKioskAndDueAmountGreaterThan(kiosk, 0);
        def tableData = [tableHeader]
        customerAccounts.each {customer -> tableData.add([customer.name, customer.contactName,customer.dueAmount])}
        tableData
    }
}
