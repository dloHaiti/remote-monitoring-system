package com.dlohaiti.dloserver

class SalesChannel {
    String name
    String description
    String discountType
    BigDecimal discountAmount
    Boolean delayedDelivery = false


    static hasMany = [customerAccounts: CustomerAccount,productMrps: ProductMrp]

    static constraints = {
        name(unique: true,blank:false)
        description(nullable: true)
        discountAmount(nullable: true)
        discountType(nullable: true, validator: { val, obj ->
            (obj.discountAmount != null || val == null )
        })
    }

}
