package com.dlohaiti.dloserver

class ReadingBuilder {
  Date createdDate = new Date(0)
  Kiosk kiosk = new Kiosk(name: 'kiosk01', apiKey: 'pw')
  String username = 'kiosk01owner'
  SamplingSite samplingSite = new SamplingSite(name: 'site', isUsedForTotalizer: false)
  Set<Measurement> measurements = []

  Reading build() {
    return new Reading(
        createdDate: createdDate,
        kiosk: kiosk,
        username: username,
        samplingSite: samplingSite,
        measurements: measurements
    )
  }
}
