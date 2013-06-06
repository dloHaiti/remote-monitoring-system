package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.joda.time.LocalDate

@ToString
@EqualsAndHashCode
class Reading {
  Date createdDate
  Kiosk kiosk
  String username

  static hasMany = [measurements: Measurement]

  static belongsTo = [kiosk: Kiosk, samplingSite: SamplingSite]

  static constraints = {
    username(nullable: true, blank: true)
  }

  boolean isUsedForTotalizer() {
    return samplingSite.isUsedForTotalizer
  }

  boolean isOnDate(LocalDate date) {
    return date == new LocalDate(createdDate.year + 1900, createdDate.month + 1, createdDate.date);
  }

  boolean isFollowupSite() {
    return samplingSite.isFollowup()
  }
}
