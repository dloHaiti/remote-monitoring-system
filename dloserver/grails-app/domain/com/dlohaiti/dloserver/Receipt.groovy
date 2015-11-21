package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.joda.time.LocalDate

@ToString
@EqualsAndHashCode
class Receipt {
    Date createdDate
    Kiosk kiosk
    Double totalGallons
    BigDecimal total
    String currencyCode
    List<ReceiptLineItem> receiptLineItems
    SalesChannel salesChannel
    CustomerAccount customerAccount
    String paymentMode
    Boolean isSponsorSelected
    Sponsor sponsor
    BigDecimal sponsorAmount
    BigDecimal customerAmount
    String paymentType
    String deliveryTime
    String uuid

    static hasMany = [receiptLineItems: ReceiptLineItem]

    static mapping = {
        uuid column: 'uuid', index: 'uuid_Idx'
    }

    boolean isOnDate(LocalDate date) {
        return date == new LocalDate(createdDate.getYear() + 1900, createdDate.getMonth() + 1, createdDate.getDate())
    }

    boolean hasSku(String sku) {
        return receiptLineItems.any { ReceiptLineItem item -> item.sku == sku }
    }

    Double totalGallonsForSku(String sku) {
        List<ReceiptLineItem> itemsWithSku = receiptLineItems.findAll({ ReceiptLineItem item -> item.sku == sku })
        return itemsWithSku.inject(0, { acc, val -> acc + val.gallons })
    }

    static constraints = {
        createdDate(nullable: false)
        kiosk(nullable: false)
        totalGallons(nullable: true)
        total(nullable: false)
        currencyCode(nullable: false)
        salesChannel(nullable: false)
        customerAccount(nullable: false)
        paymentMode(nullable: false)
        isSponsorSelected(nullable: false)
        sponsor(nullable: true)
        sponsorAmount(nullable: true)
        customerAmount(nullable: true)
        paymentType(nullable: false)
        deliveryTime(nullable: true)
        uuid(nullable: true)
    }
}
