package com.dlohaiti.dloserver

class Sponsor {
    String name
    String contactName
    String description

    static hasMany =[accounts: CustomerAccount]
    static belongsTo=CustomerAccount


    static mapping = {
        description type: "text"
    }

    static constraints = {
        name(unique: true,blank:false)
        description(nullable: true)
    }
}
