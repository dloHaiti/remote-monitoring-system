package com.dlohaiti.dloserver

class SalesChannel {
    String name
    String description
    String discountType
    BigDecimal discountAmount


    static hasMany = [customerAccounts: CustomerAccount]


    static constraints = {
        name(unique: true,blank:false)
        description(nullable: true)
        discountAmount(nullable: true)
        discountType(nullable: true, validator: { val, obj ->
            (obj.discountAmount != null || val == null )
        })
    }

}
