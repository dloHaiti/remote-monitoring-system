package com.dlohaiti.dloserver

import grails.test.mixin.TestFor
import org.joda.time.LocalDate

@TestFor(Reading)
class ReadingTest {
  void testShouldKnowIfItUsedForTotalizer() {
    def totalizerReading = new ReadingBuilder(samplingSite: new SamplingSiteBuilder(isUsedForTotalizer: true).build()).build()
    def regularReading = new ReadingBuilder(samplingSite: new SamplingSiteBuilder(isUsedForTotalizer: false).build()).build()

    assert totalizerReading.isUsedForTotalizer()
    assert !regularReading.isUsedForTotalizer()
  }

  void testShouldKnowIfItIsAFollowupReading() {
    def a = new SamplingSiteBuilder(followupToSite: null).build()
    def earlyReading = new ReadingBuilder(samplingSite: a).build()
    def lateReading = new ReadingBuilder(samplingSite: new SamplingSiteBuilder(followupToSite: a).build()).build()

    assert lateReading.isFollowupSite()
    assert !earlyReading.isFollowupSite()
  }

  void testShouldKnowIfItIsOnDate() {
    def april15Date = new LocalDate(2013, 4, 15).toDate()
    def reading = new ReadingBuilder(createdDate: april15Date).build()
    def april15 = new LocalDate(2013, 4, 15)
    def dayBefore = april15.minusDays(1)
    def dayAfter = april15.plusDays(1)

    assert reading.isOnDate(april15)
    assert !reading.isOnDate(dayBefore)
    assert !reading.isOnDate(dayAfter)
  }
}
