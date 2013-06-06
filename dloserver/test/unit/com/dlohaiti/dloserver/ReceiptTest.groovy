package com.dlohaiti.dloserver
import grails.test.mixin.TestFor
import org.joda.time.LocalDate

@TestFor(Receipt)
class ReceiptTest {

  void testShouldKnowIfItsOnADate() {
    def receipt = new ReceiptBuilder([createdDate: new Date(2013, 4, 15)]).build()
    def april15 = new LocalDate(2013, 4, 15)
    def dayBefore = april15.minusDays(1)
    def dayAfter = april15.plusDays(1)

    assert receipt.isOnDate(april15)
    assert !receipt.isOnDate(dayBefore)
    assert !receipt.isOnDate(dayAfter)
  }

  void testShouldKnowIfItHasASku() {
    def receipt = new ReceiptBuilder(receiptLineItems: [new ReceiptLineItemBuilder(sku: "AAA").build()]).build()
    assert receipt.hasSku("AAA")
    assert !receipt.hasSku("ZZZ")
  }
}
