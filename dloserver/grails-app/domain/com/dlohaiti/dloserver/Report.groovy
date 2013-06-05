package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class Report {
  String name

  static constraints = {
    name(blank: false, unique: true)
  }
}
