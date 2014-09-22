package com.dlohaiti.dloserver

class CustomerAccount {
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
        gpsCoordinates(nullable: true,validator: { val, obj ->
            if(val == null || val=="") return true
            else {
              def locs = val.split(",")
              return  (locs.size() == 2) && (locs[0].split(":").size() == 3) &&  (locs[1].split(":").size() == 3)
            }
        })
    }

}
