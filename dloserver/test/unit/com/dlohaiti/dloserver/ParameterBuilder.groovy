package com.dlohaiti.dloserver

class ParameterBuilder {
  String name = 'Gallons Distributed'
  String unit = 'gallons'
  BigDecimal min
  BigDecimal max
  boolean isUsedInTotalizer = false

  Parameter build() {
    return new Parameter(name: name, unit: unit, minimum: min, maximum: max, isUsedInTotalizer: isUsedInTotalizer)
  }
}
