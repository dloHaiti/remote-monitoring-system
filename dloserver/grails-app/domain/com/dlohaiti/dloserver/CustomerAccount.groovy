package com.dlohaiti.dloserver

class CustomerAccount {
    String name
    String phoneNumber
    String address

    CustomerType customerType
    Kiosk kiosk

    static belongsTo = [SalesChannel,CustomerType,Kiosk]
    static hasMany = [contactNames: String, channels: SalesChannel]

    static mapping = {
        address type: "text"
        contactNames indexColumn: [name: "contact_name", type: String],
                joinTable: [column: "contactName"],lazy: false
        customerType lazy: false

    }

    static constraints = {
        name(blank: false, unique: true)
        address(nullable: true)
        phoneNumber(nullable: true)

    }

}
