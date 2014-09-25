package com.dlohaiti.dloserver

class Sponsor {
    String id
    String name
    String contactName
    String phoneNumber

    Kiosk kiosk

    static belongsTo=[Kiosk,CustomerAccount]
    static hasMany =[accounts: CustomerAccount]

    static mapping = {
        id generator: 'assigned', column: 'id', name: 'id', type: 'string'
    }

    static constraints = {
        id bindable: true
        name(nullable: false,unique: ['kiosk'])
        phoneNumber(nullable: true)
        kiosk(nullable: false)
    }
}
