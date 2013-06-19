package com.dlohaiti.dloserver

import org.joda.time.LocalDate

class Delivery {
  Date createdDate
  Integer quantity
  String type

  static belongsTo = [kiosk: Kiosk, deliveryAgent: DeliveryAgent]

  boolean isOnDate(LocalDate date) {
    return date == new LocalDate(createdDate.getYear() + 1900, createdDate.getMonth() + 1, createdDate.getDate());
  }

  boolean isOutForDelivery() {
    return 'OUT_FOR_DELIVERY' == type
  }

  boolean isReturned() {
    return 'RETURNED' == type
  }

  Integer getGallons() {
    return DeliveryConfiguration.first()?.gallons
  }

  Money getPrice() {
    return DeliveryConfiguration.first()?.price
  }
}
