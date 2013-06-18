package com.dlohaiti.dloserver

class DeliveryAgent {
  String name
  boolean active

  static belongsTo = [kiosk: Kiosk]

  static constraints = {
  }
}
