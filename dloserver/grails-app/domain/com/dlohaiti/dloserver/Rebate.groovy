package com.dlohaiti.dloserver

class Rebate {

    Integer noOfSkus
    String transactionType
    String noOfFreeSkus
    Product product

    static belongsTo=[Product,Region]
    static hasMany=[regions:Region]

    static constraints = {

    }
}
