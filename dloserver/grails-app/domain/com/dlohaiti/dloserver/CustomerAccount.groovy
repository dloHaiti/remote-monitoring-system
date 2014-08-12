package com.dlohaiti.dloserver

class CustomerAccount {
    String name
    String phoneNumber
    String address
    String contactName

    CustomerType customerType
    Kiosk kiosk

    static belongsTo = [SalesChannel,CustomerType,Kiosk]
    static hasMany = [channels: SalesChannel]

    static mapping = {
        address type: "text"
//        contactNames indexColumn: [name: "contact_name", type: String],
//                joinTable: [column: "contactName"],lazy: false
        customerType lazy: false

    }

    static constraints = {
        name(nullable: true,unique: ['kiosk'])
        contactName(validator: {
            val, obj,errors ->
                if(obj.name == null && val == null) return errors.rejectValue('contactName', 'can not be null')
                else return true
        }, nullable: true, unique: ['kiosk'])
        address(nullable: true)
        phoneNumber(nullable: true)

    }

}
