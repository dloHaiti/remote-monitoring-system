package com.dlohaiti.dloserver

class Rebate {
    String name
    Integer noOfSkus
    String transactionType
    String noOfFreeSkus
    Product product
    String base64EncodedImage

    static belongsTo=[Product,Region]
    static hasMany=[regions:Region]

    static mapping = {
        base64EncodedImage type: "text"
    }

    static constraints = {
        name(unique: true)
        base64EncodedImage(nullable: true)
    }
}
