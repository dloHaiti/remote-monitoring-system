package com.dlohaiti.dloserver

class Sponsor {
    String name
    String contactName
    String phoneNumber

    Kiosk kiosk

    static belongsTo=[Kiosk,CustomerAccount]
    static hasMany =[accounts: CustomerAccount]

    static constraints = {
        name(nullable: false,unique: ['kiosk'])
        phoneNumber(nullable: true)
        kiosk(nullable: false)
    }
}
