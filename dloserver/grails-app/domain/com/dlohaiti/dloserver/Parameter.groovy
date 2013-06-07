package com.dlohaiti.dloserver

class Parameter {

  String name
  String unit
  BigDecimal min
  BigDecimal max
  boolean isUsedInTotalizer
  boolean isOkNotOk

  static hasOne = [sensor: Sensor]
  static hasMany = [samplingSites: SamplingSite]

  static constraints = {
    name(blank: false, unique: true)
    unit(nullable: true)
    min(nullable: true)
    max(nullable: true, validator: { val, obj -> ((val == null) || (obj.min < val)) })
    sensor(nullable: true, unique: true, display: false)
  }

  public String toString() {
    name
  }
}