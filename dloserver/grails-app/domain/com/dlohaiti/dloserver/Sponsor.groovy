package com.dlohaiti.dloserver

class Sponsor {
    String name
    String contactName
    String description

    static belongsTo =[kiosk: Kiosk]

    static mapping = {
        description type: "text"
    }

    static constraints = {
        name(unique: true,blank:false)
        description(nullable: true)
    }
}
