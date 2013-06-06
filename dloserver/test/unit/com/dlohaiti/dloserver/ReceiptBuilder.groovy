package com.dlohaiti.dloserver

class ReceiptBuilder {
  Date createdDate = new Date(0)
  Kiosk kiosk = new Kiosk(name: "k1")
  Integer totalGallons = 10
  BigDecimal total = 10G
  String currencyCode = "HTG"
  List<ReceiptLineItem> receiptLineItems = []

  Receipt build() {
    return new Receipt(createdDate: createdDate,
        kiosk: kiosk,
        totalGallons: totalGallons,
        total: total,
        currencyCode: currencyCode,
        receiptLineItems: receiptLineItems)
  }
}
