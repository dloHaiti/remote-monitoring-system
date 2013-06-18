package com.dlohaiti.dloserver

class DeliveryAgent {
  String name

  static belongsTo = [kiosk: Kiosk]

  static constraints = {
  }
}
