package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.endpoint.ReceiptsController
import groovy.json.JsonBuilder
import org.junit.Before
import org.junit.Test

class ReceiptsControllerIntegrationTests extends GroovyTestCase {

  ReceiptsController controller

  @Before void setup() {
    def kiosk = new Kiosk(name: 'k1', apiKey: 'pw').save(failOnError: true)
    def category = new ProductCategory(name: "Category1",base64EncodedImage: "").save(failOnError: true)
    new Product(sku: 'AAA', gallons: 300, description: 'a', price: new Money(amount: 1), base64EncodedImage: 'a', active: true,category:category).save(failOnError: true)
    new Product(sku: 'BBB', gallons: 200, description: 'b', price: new Money(amount: 1), base64EncodedImage: 'b', active: true,category:category).save(failOnError: true)

    controller = new ReceiptsController()
    controller.request.contentType = 'application/json'
    controller.request.kiosk = kiosk
  }

  @Test void shouldRespondWithCreatedWhenSuccessful() {
    def req = [
        createdDate: '2013-04-24 12:00:01 EDT',
        totalGallons: 200,
        total: [
            amount: 500,
            currencyCode: 'HTG'
        ],
        lineItems: [
            [
                sku: 'AAA',
                quantity: 1,
                type: 'PRODUCT',
                price: [
                    amount: 300,
                    currencyCode: 'HTG'
                ]
            ],
            [
                sku: 'BBB',
                quantity: 1,
                type: 'PRODUCT',
                price: [
                    amount: 200,
                    currencyCode: 'HTG'
                ]
            ]
        ]
    ]
    controller.request.json = new JsonBuilder(req).toString()

    controller.save()

    assert 201 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":[]}' == controller.response.contentAsString
    assert 1 == Receipt.count()
    assert 2 == ReceiptLineItem.count()
  }

  @Test void shouldRespondWithUnprocessableWhenProductOnReceiptIsMissing() {
    def req = [
        createdDate: '2013-04-24 12:00:01 EDT',
        totalGallons: 200,
        total: [
            amount: 500,
            currencyCode: 'HTG'
        ],
        lineItems: [
            [
                sku: 'MISSING',
                quantity: 1,
                type: 'PRODUCT',
                price: [
                    amount: 300,
                    currencyCode: 'HTG'
                ]
            ]
        ]
    ]
    controller.request.json = new JsonBuilder(req).toString()

    controller.save()

    assert 422 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":["PRODUCT_MISSING"]}' == controller.response.contentAsString
    assert 0 == Receipt.count()
    assert 0 == ReceiptLineItem.count()
  }

  @Test void shouldRespondWithUnprocessableWhenErrors() {
    def req = [
        createdDate: null, // not nullable as per constraints in Receipt
        totalGallons: 200,
        total: [
            amount: 500,
            currencyCode: 'HTG'
        ],
        lineItems: [
            [
                sku: 'AAA',
                quantity: 1,
                type: 'PRODUCT',
                price: [
                    amount: 300,
                    currencyCode: 'HTG'
                ]
            ]
        ]
    ]
    controller.request.json = new JsonBuilder(req).toString()

    controller.save()

    assert 422 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":["CREATEDDATE_NULLABLE"]}' == controller.response.contentAsString
    assert 0 == Receipt.count()
    assert 0 == ReceiptLineItem.count()
  }

  @Test void shouldRespondWithServerErrorWhenUnforeseenErrorOccurs() {
    controller.receiptsService = [saveReceipt: {-> throw new RuntimeException() }] as DeliveriesService

    controller.save()

    assert 500 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":["SERVER_ERROR"]}' == controller.response.contentAsString
    assert 0 == Receipt.count()
    assert 0 == ReceiptLineItem.count()
  }
}
