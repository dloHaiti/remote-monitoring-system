package com.dlohaiti.dloserver

class DeliveryConfiguration {
  Integer minimumValue
  Integer maximumValue
  Integer defaultValue
  Integer gallons
  Money price

  static embedded = ['price']

  static constraints = {
  }
}
