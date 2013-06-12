package com.dlohaiti.dloserver

import grails.test.mixin.TestFor
import org.joda.time.DateTime
import org.joda.time.LocalDate

@TestFor(Readings)
class ReadingsTest {

  def nontotalizerSamplingSite = new SamplingSiteBuilder(isUsedForTotalizer: false, followupToSite: null).build()
  def nontotalizerFollowupSamplingSite = new SamplingSiteBuilder(isUsedForTotalizer: false, followupToSite: nontotalizerSamplingSite).build()

  def totalizerSamplingSite = new SamplingSiteBuilder(name: "A1", isUsedForTotalizer: true, followupToSite: null).build()
  def totalizerFollowupSamplingSite = new SamplingSiteBuilder(name: "A2", isUsedForTotalizer: true, followupToSite: totalizerSamplingSite).build()

  def differentTotalizerSamplingSite = new SamplingSiteBuilder(name: "B1", isUsedForTotalizer: true, followupToSite: null).build()
  def differentTotalizerFollowupSamplingSite = new SamplingSiteBuilder(name: "B2", isUsedForTotalizer: true, followupToSite: totalizerSamplingSite).build()

  void testShouldTotalizeBySubtractingLateDayReadingsFromEarlyDay() {
    Readings readings = new Readings(readings: [
        new ReadingBuilder(
            samplingSite: totalizerSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 20G)
            ],
            createdDate: new LocalDate().toDate()
        ).build(),
        new ReadingBuilder(
            samplingSite: totalizerFollowupSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 25G)
            ],
            createdDate: new LocalDate().toDate()
        ).build()
    ])

    assert 5G == readings.totalizeGallonsFor(new LocalDate())
  }

  void testShouldIgnoreMeasurementsThatAreNotUsedInTotalizer() {
    Readings readings = new Readings(readings: [
        new ReadingBuilder(
            samplingSite: totalizerSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 20G),
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: false).build(), value: 100G)
            ],
            createdDate: new LocalDate().toDate()
        ).build(),
        new ReadingBuilder(
            samplingSite: totalizerFollowupSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 25G),
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: false).build(), value: 300G)
            ],
            createdDate: new LocalDate().toDate()
        ).build()
    ])

    assert 5G == readings.totalizeGallonsFor(new LocalDate())
  }

  void testShouldIgnoreReadingsThatAreNotUsedInTotalizerEvenWithMeasurementsThatAreUsedInTotalizer() {
    Readings readings = new Readings(readings: [
        new ReadingBuilder(
            samplingSite: totalizerSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 20G)
            ],
            createdDate: new LocalDate().toDate()
        ).build(),
        new ReadingBuilder(
            samplingSite: totalizerFollowupSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 25G)
            ],
            createdDate: new LocalDate().toDate()
        ).build(),
        new ReadingBuilder(
            samplingSite: new SamplingSiteBuilder(isUsedForTotalizer: false, followupToSite: nontotalizerFollowupSamplingSite).build(),
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 50G)
            ],
            createdDate: new LocalDate().toDate()
        ).build()
    ])

    assert 5G == readings.totalizeGallonsFor(new LocalDate())
  }

  void testShouldOnlyUseReadingsForGivenDay() {
    Readings readings = new Readings(readings: [
        new ReadingBuilder(
            samplingSite: totalizerSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 20G)
            ],
            createdDate: new LocalDate(2013, 4, 15).toDate()
        ).build(),
        new ReadingBuilder(
            samplingSite: totalizerFollowupSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 25G)
            ],
            createdDate: new LocalDate(2013, 4, 16).toDate()
        ).build(),
        new ReadingBuilder(
            samplingSite: totalizerFollowupSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 50G)
            ],
            createdDate: new LocalDate(2013, 4, 15).toDate()
        ).build()
    ])

    assert 30G == readings.totalizeGallonsFor(new LocalDate(2013, 4, 15))
  }

  void testShouldOnlyUseEarliestAndLatestReadingsPerSampleSite() {
    Readings readings = new Readings(readings: [
        new ReadingBuilder(
            samplingSite: totalizerSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 20G)
            ],
            createdDate: new DateTime(2013, 4, 15, 12, 0, 0).toDate() //noon early reading for site A
        ).build(),
        new ReadingBuilder(
            samplingSite: totalizerSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 30G)
            ],
            createdDate: new DateTime(2013, 4, 15, 20, 0, 0).toDate() //8pm early reading for site A
        ).build(),


        new ReadingBuilder(
            samplingSite: differentTotalizerSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 1G)
            ],
            createdDate: new DateTime(2013, 4, 15, 12, 0, 0).toDate() //noon early reading for site B
        ).build(),
        new ReadingBuilder(
            samplingSite: differentTotalizerSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 1000G)
            ],
            createdDate: new DateTime(2013, 4, 15, 20, 0, 0).toDate() //8pm early reading for site B
        ).build(),


        new ReadingBuilder(
            samplingSite: totalizerFollowupSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 40G)
            ],
            createdDate: new DateTime(2013, 4, 15, 20, 0, 0).toDate() //8pm late reading for site A
        ).build(),
        new ReadingBuilder(
            samplingSite: totalizerFollowupSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 50G)
            ],
            createdDate: new DateTime(2013, 4, 15, 22, 0, 0).toDate() // 10pm late reading for site A
        ).build(),


        new ReadingBuilder(
            samplingSite: differentTotalizerSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 5000G)
            ],
            createdDate: new DateTime(2013, 4, 15, 21, 0, 0).toDate() //9pm late reading for site B
        ).build(),
        new ReadingBuilder(
            samplingSite: differentTotalizerFollowupSamplingSite,
            measurements: [
                new Measurement(parameter: new ParameterBuilder(isUsedInTotalizer: true).build(), value: 20000G)
            ],
            createdDate: new DateTime(2013, 4, 15, 23, 0, 0).toDate() // 11pm late reading for site B
        ).build()
    ])

    //site A should be 50 (latest) - 20 (earliest) = 30
    //site B should be 20000 (latest) - 1 (earliest) = 19999
    //they combine for totalized gallons
    assert 20029G == readings.totalizeGallonsFor(new LocalDate(2013, 4, 15))
  }

  void testShouldGiveReadingAveragePerDayParameterAndSite() {
    def pH = new ParameterBuilder(name: 'pH').build()
    def april10 = new LocalDate(2013, 4, 10).toDate()
    def site1 = new SamplingSiteBuilder(name: 'site1').build()
    Readings readings = new Readings(readings: [
        new ReadingBuilder(
            samplingSite: site1,
            measurements: [
                new Measurement(parameter: pH, value: 5G),
                new Measurement(parameter: new ParameterBuilder(name: 'Temperature').build(), value: 10G),
            ],
            createdDate: april10
        ).build(),
        new ReadingBuilder(
            samplingSite: site1,
            measurements: [
                new Measurement(parameter: pH, value: 7G),
                new Measurement(parameter: new ParameterBuilder(name: 'Temperature').build(), value: 20G),
            ],
            createdDate: april10
        ).build()
    ])

    assert 6G == readings.averageFor(site1, pH, april10)
    assert 15G == readings.averageFor(site1, new ParameterBuilder(name: 'Temperature').build(), april10)
    assert 0 == readings.averageFor(site1, new ParameterBuilder(name: 'Nonsense').build(), april10)
    assert 0 == readings.averageFor(new SamplingSiteBuilder(name: 'site2').build(), pH, april10)
    assert 0 == readings.averageFor(site1, pH, new LocalDate(2013, 4, 9).toDate())
  }
}
