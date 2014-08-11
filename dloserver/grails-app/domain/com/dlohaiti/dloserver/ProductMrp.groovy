package com.dlohaiti.dloserver
import org.apache.commons.lang.builder.HashCodeBuilder

class ProductMrp implements Serializable {
    Kiosk kiosk
    SalesChannel salesChannel
    Product product
    Money price

    static embedded = ['price']

    static constraints = {
        id (composite: ['kiosk','salesChannel','product'])
        product(unique: ['kiosk', 'salesChannel'])
    }


    boolean equals(other) {
        if (!(other instanceof ProductMrp)) {
            return false
        }

        other.kiosk == kiosk && other.salesChannel == salesChannel && other.product == product
    }


    int hashCode() {
        def builder = new HashCodeBuilder()
        builder.append kiosk
        builder.append salesChannel
        builder.append product
        builder.toHashCode()
    }
}
