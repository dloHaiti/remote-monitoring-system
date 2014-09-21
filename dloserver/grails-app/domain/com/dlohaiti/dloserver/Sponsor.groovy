package com.dlohaiti.dloserver

class Sponsor {
    String name
    String contactName
    String description

    Kiosk kiosk

    static belongsTo=[Kiosk,CustomerAccount]
    static hasMany =[accounts: CustomerAccount]

    static mapping = {
        description type: "text"
    }

    static constraints = {
        name(nullable: false,unique: ['kiosk'])
        description(nullable: true)
        kiosk(nullable: false)
    }
}
