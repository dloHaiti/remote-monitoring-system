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
  Boolean isUsedInTotalizer
  Boolean isOkNotOk
  Boolean manual
  Boolean active
  Integer priority

  static transients = [ "samplingSites" ]

  List<SamplingSite> samplingSites;


  static constraints = {
    name(blank: false, unique: true)
    unit(nullable: true)
    minimum(nullable: true)
    maximum(nullable: true, validator: { val, obj -> ((val == null) || (obj.minimum < val)) })
    priority(nullable: true)
  }

  // eager loading of the sampling sites, for serializing to JSON in configuration controller
  // http://blog.springsource.org/2010/07/28/gorm-gotchas-part-3/
  static mapping = {
//    samplingSites lazy: false
  }

  public String toString() {
    name
  }

  boolean hasRange() {
    return minimum != null || maximum != null;
  }


}