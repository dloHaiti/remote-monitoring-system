package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class Parameter {

  String name
  String unit
  BigDecimal minimum
  BigDecimal maximum
  boolean isUsedInTotalizer
  boolean isOkNotOk

  static hasOne = [sensor: Sensor]
  static hasMany = [samplingSites: SamplingSite]

  static constraints = {
    name(blank: false, unique: true)
    unit(nullable: true)
    minimum(nullable: true)
    maximum(nullable: true, validator: { val, obj -> ((val == null) || (obj.minimum < val)) })
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

  boolean hasRange() {
    return minimum != null || maximum != null;
  }
}