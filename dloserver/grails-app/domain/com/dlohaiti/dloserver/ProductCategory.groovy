package com.dlohaiti.dloserver

class ProductCategory {
    String name
    String description
    String base64EncodedImage

    static hasMany = [products: Product]

    static mapping = {
        base64EncodedImage type: "text"
    }

    static constraints = {
        name(unique: true,blank:false)
        description(nullable: true)
    }
}
