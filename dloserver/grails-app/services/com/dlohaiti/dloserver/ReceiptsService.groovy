package com.dlohaiti.dloserver

import org.apache.commons.lang3.StringUtils
import org.joda.time.LocalDate

class ReceiptsService {

    def grailsApplication

    public saveReceipt(params) {
        Date createdDate = params.date("createdDate", grailsApplication.config.dloserver.measurement.timeformat.toString())
        String currencyCode = params.total.currencyCode
        def salesChannel = SalesChannel.findById(params.salesChannelId)
        def customerAccount = CustomerAccount.findById(params.customerAccountId)
        def sponsor = null
        def receiptWithUUID = null

        if(params.uuid == null){
            log.info "UUID is not being sent for this transaction";
        }

        if( params.uuid != null || StringUtils.isNotBlank(params.uuid)){
            receiptWithUUID = Receipt.findByUuid(params.uuid);
        }

        if (receiptWithUUID != null){
            log.info "Duplicate transaction information received" + receiptWithUUID
            return receiptWithUUID
        }

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
                deliveryTime     : params.deliveryTime,
                uuid             : params.uuid

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
        try{
            if (params.paymentHistory || StringUtils.isNotBlank(params.sponsorId)) {
                def history = new PaymentHistory(
                        [
                                paymentDate:  params.date("paymentHistory.paymentDate", grailsApplication.config.dloserver.measurement.timeformat.toString()),
                                customerAccount: customerAccount,
                                amount: params.paymentHistory.amount,
                                receipt: receipt
                        ]
                )
                history.save(flush: true)
            }
        }catch (MissingPropertyException e){
            log.info "There is no payment history to save"
        }

        return receipt
    }

    /**
     * Processes the receipts for the given date range
     * @param fromDate the start Date
     * @param toDate the end Date
     * @return the {@link List} of {@link Receipt}'s
     */
    List<Receipt> getReceiptsBetWeenDate(LocalDate fromDate, LocalDate toDate) {
        def receipts = []
        receipts.addAll(Receipt.findAllByCreatedDateGreaterThanEqualsAndCreatedDateLessThan(fromDate.toDate(), toDate.toDate()))
        receipts
    }

    /**
     * Processes and returns the receipts By Customers in specific date range
     * @param customerId
     * @param fromDate
     * @param toDate
     * @return
     */
    def getReceiptsByCustomerInDateRange(def customers, LocalDate fromDate, LocalDate toDate) {
        def receipts = []
        receipts.addAll(Receipt.findAllByCustomerAccountInListAndCreatedDateGreaterThanEqualsAndCreatedDateLessThan(customers, fromDate.toDate(), toDate.plusDays(1).toDate()))
        receipts
    }
}
