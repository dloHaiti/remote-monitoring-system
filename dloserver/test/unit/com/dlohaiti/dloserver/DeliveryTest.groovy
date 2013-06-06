package com.dlohaiti.dloserver

import grails.test.mixin.TestFor
import org.joda.time.LocalDate

@TestFor(Delivery)
class DeliveryTest {

  void testShouldKnowIfItsOnADate() {
    def april15Date = new LocalDate(2013, 4, 15).toDate()
    def delivery = new Delivery(timestamp: april15Date, quantity: 24, type: "OUT_FOR_DELIVERY")
    def april15 = new LocalDate(2013, 4, 15)
    def dayBefore = april15.minusDays(1)
    def dayAfter = april15.plusDays(1)

    assert delivery.isOnDate(april15)
    assert !delivery.isOnDate(dayBefore)
    assert !delivery.isOnDate(dayAfter)
  }

}
