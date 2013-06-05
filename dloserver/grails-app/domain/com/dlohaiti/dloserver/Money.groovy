package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class Money {
  BigDecimal amount;
  Currency currency = Currency.getInstance("HTG")
}
