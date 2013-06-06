package com.dlohaiti.dloserver

import org.joda.time.LocalDate

class Readings {
  static mapWith = "none"

  List<Reading> readings;

  Integer totalizeGallonsFor(LocalDate date) {
    Integer total = 0
    println readings.size()
    List<Reading> totalizerReadings = readings.findAll({ r -> r.isUsedForTotalizer() && r.isOnDate(date) })
    println totalizerReadings.size()
    def seenLateSamplingSites = []
    def readingsWithUniqueSampleSites = []
    def lateDay = totalizerReadings.findAll({ rd -> rd.isFollowupSite() })
    def sortedLateDays = lateDay.sort({ r1, r2 -> r2.createdDate.compareTo(r1.createdDate) })
    for(sld in sortedLateDays) {
      if(!seenLateSamplingSites.contains(sld.samplingSite)) {
        readingsWithUniqueSampleSites.add(sld)
        seenLateSamplingSites.add(sld.samplingSite)
      }
    }
    for(firstLateDay in readingsWithUniqueSampleSites) {
    println firstLateDay
      def relevantMeasurements = firstLateDay.measurements.findAll({ m -> m.parameter.isUsedInTotalizer })
      for(m in relevantMeasurements) {
        total += m.value
      }
    }

    def seenEarlySamplingSites = []
    def uniqueEarlyDaysBySampleSite = []
    def earlyDay = totalizerReadings.findAll({ rd -> !rd.isFollowupSite() })
    def sortedEarlyDays = earlyDay.sort { r1, r2 -> r1.createdDate.compareTo(r2.createdDate) }
    for(sed in sortedEarlyDays) {
      if(!seenEarlySamplingSites.contains(sed.samplingSite)) {
        uniqueEarlyDaysBySampleSite.add(sed)
        seenEarlySamplingSites.add(sed.samplingSite)
      }
    }
    for(oneEarlyDay in uniqueEarlyDaysBySampleSite) {
      println oneEarlyDay
      def relevantMeasurementsEarly = oneEarlyDay.measurements.findAll({ m -> m.parameter.isUsedInTotalizer })
      for(m in relevantMeasurementsEarly) {
        total -= m.value
      }
    }
    return total
  }
}
