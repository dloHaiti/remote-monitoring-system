package com.dlohaiti.dloserver

class SamplingSiteBuilder {
  String name = 'Fill Station'
  boolean isUsedForTotalizer = false
  SamplingSite followupToSite = null

  SamplingSite build() {
    return new SamplingSite(name: name, isUsedForTotalizer: isUsedForTotalizer, followupToSite: followupToSite)
  }
}
