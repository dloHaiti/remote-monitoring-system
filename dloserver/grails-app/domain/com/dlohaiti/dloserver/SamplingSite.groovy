package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class SamplingSite {
  String name
  boolean isUsedForTotalizer
  SamplingSite followupToSite

  static hasMany = [readings: Reading, parameters: Parameter]
  static belongsTo = Parameter
  static constraints = {
    followupToSite(nullable: true)
  }

  boolean isFollowup() {
    return followupToSite != null
  }
}
