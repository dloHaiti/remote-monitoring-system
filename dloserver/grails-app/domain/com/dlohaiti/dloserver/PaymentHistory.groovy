package com.dlohaiti.dloserver

class PaymentHistory {
    Date paymentDate
    static belongsTo = [customerAccount: CustomerAccount,receipt: Receipt]
    Double amount=0

    static constraints = {
        receipt(nullable: true)
    }
}
