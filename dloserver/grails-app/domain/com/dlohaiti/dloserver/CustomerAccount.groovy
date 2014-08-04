package com.dlohaiti.dloserver

class CustomerAccount {
    String name
    String phoneNumber
    String address

    static belongsTo = [ customerType: CustomerType ]
    static hasMany = [contactNames: String]

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
