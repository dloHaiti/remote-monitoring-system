package com.dlohaiti.dloserver

import org.apache.commons.lang3.StringUtils

class ReceiptsService {

    def grailsApplication

    public saveReceipt(params) {
        Date createdDate = params.date("createdDate", grailsApplication.config.dloserver.measurement.timeformat.toString())
        String currencyCode = params.total.currencyCode
        def salesChannel = SalesChannel.findById(params.salesChannelId)
        def customerAccount = CustomerAccount.findById(params.customerAccountId)
        def sponsor = null
        if (StringUtils.isNotBlank(params.sponsorId)) {
            sponsor = Sponsor.findById(params.sponsorId)
        }
        if (salesChannel == null) {
            throw new RuntimeException("Unable to save receipt as sales channel is null, id: " + params.salesChannelId)
        }
        if (customerAccount == null) {
            throw new RuntimeException("Unable to save receipt as customer account is null, id: " + params.customerAccountId)
        }
        Receipt receipt = new Receipt([
                createdDate      : createdDate,
                totalGallons     : params.totalGallons,
                total            : params.total.amount,
                currencyCode     : currencyCode,
                salesChannel     : salesChannel,
                customerAccount  : customerAccount,
                paymentMode      : params.paymentMode,
                isSponsorSelected: params.isSponsorSelected,
                sponsor          : sponsor,
                sponsorAmount    : params.sponsorAmount.amount,
                customerAmount   : params.customerAmount.amount,
                paymentType      : params.paymentType,
                deliveryTime     : params.deliveryTime
        ])

        params.lineItems?.each { item ->
            def gallons = 0
            if (item.type == "PRODUCT") {
                def product = Product.findBySku(item.sku)
                if (product == null) {
                    throw new MissingProductException();
                }
                gallons = product.gallons * item.quantity
            }
            def lineItem = new ReceiptLineItem(
                    sku: item.sku,
                    quantity: item.quantity,
                    type: item.type,
                    price: item.price.amount,
                    currencyCode: currencyCode,
                    gallons: gallons
            )
            receipt.addToReceiptLineItems(lineItem)
        }
        receipt.kiosk = params.kiosk
        receipt.save(flush: true)
        return receipt
    }

}
