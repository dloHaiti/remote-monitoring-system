package com.dlohaiti.dloserver

class PaymentHistoryService {

    def grailsApplication

    def saveHistory(params) {
        def customerAccount = CustomerAccount.findById(params.customerId)

        def history = new PaymentHistory(
                [
                        paymentDate:  params.date("paymentDate", grailsApplication.config.dloserver.measurement.timeformat.toString()),
                        customerAccount: customerAccount,
                        amount: params.amount,
                ]
        )
        history.save(flush: true)

        return history
    }
}
