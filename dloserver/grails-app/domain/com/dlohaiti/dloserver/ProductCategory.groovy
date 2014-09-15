package com.dlohaiti.dloserver

class ProductCategory {
    String name
    String description

    static hasMany = [products: Product]

    static constraints = {
        name(unique: true,blank:false)
        description(nullable: true)
    }
}
