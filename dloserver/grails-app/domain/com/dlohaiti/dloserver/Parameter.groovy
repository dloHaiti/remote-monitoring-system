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

  // eager loading of the sampling sites, for serializing to JSON in configuration controller
  // http://blog.springsource.org/2010/07/28/gorm-gotchas-part-3/
  static mapping = {
    samplingSites lazy: false
  }

  public String toString() {
    name
  }
}