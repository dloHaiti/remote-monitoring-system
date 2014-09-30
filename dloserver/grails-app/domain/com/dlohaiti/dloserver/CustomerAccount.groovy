package com.dlohaiti.dloserver

import java.sql.Timestamp

class CustomerAccount {
    String id
    String name
    String phoneNumber
    String address
    String contactName
    Double dueAmount = 0
    String gpsCoordinates
    CustomerType customerType
    Kiosk kiosk

    Collection channels

    static belongsTo = [SalesChannel,CustomerType,Kiosk]
    static hasMany = [channels: SalesChannel,sponsors: Sponsor]

    static mapping = {
        id generator: 'assigned',column:'id', name: 'id', type: 'string'
        address type: "text"
//        contactNames indexColumn: [name: "contact_name", type: String],
//                joinTable: [column: "contactName"],lazy: false
        customerType lazy: false

    }

    static constraints = {
        id bindable: true
        name(nullable: true,unique: ['kiosk','contactName'])
        contactName(validator: {
            val, obj,errors ->
                if(obj.name == null && val == null) return errors.rejectValue('contactName', 'can not be null')
                else return true
        }, unique: ['kiosk','name'])
        address(nullable: true)
        phoneNumber(nullable: true)
        gpsCoordinates(validator: { val, obj ->
            if(val == null || val=="") return true
            else {
              def locs = val.split(" ")
              return  (locs.size() == 2)
            }
        },nullable: true)
    }

}
