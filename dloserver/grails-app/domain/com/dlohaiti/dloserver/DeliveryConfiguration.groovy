package com.dlohaiti.dloserver

class DeliveryConfiguration {
  Integer minimumValue
  Integer maximumValue
  Integer defaultValue
  Double gallons
  Money price

  static embedded = ['price']

  static constraints = {
  }
}
