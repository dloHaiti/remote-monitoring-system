package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode(includes = ['name', 'active', 'kiosk'])
class DeliveryAgent {
  String name
  Boolean active

  static belongsTo = [kiosk: Kiosk]

  static constraints = {
  }
}
