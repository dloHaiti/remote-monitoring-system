package com.dlohaiti.dloserver
import grails.test.mixin.TestFor
import org.joda.time.LocalDate

@TestFor(Receipt)
class ReceiptTest {

  void testShouldKnowIfItsOnADate() {
    def receipt = new ReceiptBuilder([createdDate: new LocalDate(2013, 4, 15).toDate()]).build()
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

  void testShouldKnowTheTotalGallonsOfSku() {
    def lineItems = [
        new ReceiptLineItemBuilder(sku: "AAA", gallons: 10).build(),
        new ReceiptLineItemBuilder(sku: "AAA", gallons: 10).build(),
        new ReceiptLineItemBuilder(sku: "ZZZ", gallons: 30).build()
    ]
    def receipt = new ReceiptBuilder(receiptLineItems: lineItems).build()

    println "----------------------------"
    println receipt
    println "----------------------------"

    assert 20 == receipt.totalGallonsForSku("AAA")
    assert 30 == receipt.totalGallonsForSku("ZZZ")
  }
}
