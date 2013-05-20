package com.dlohaiti.dloserver

class Delivery {
    Date timestamp
    Integer quantity
    String type

    static belongsTo = [kiosk: Kiosk]
}
