package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.endpoint.ReceiptsController
import org.junit.Before
import org.junit.Test

class ReceiptsControllerIntegrationTests extends GroovyTestCase {

    ReceiptsController controller = new ReceiptsController()

    @Before
    void setup() {
        new Kiosk(name: "k1").save()
    }

    @Test
    void shouldSaveValidDataToDb() {
        controller.request.json = '{"kioskId":"k1", "createdAt":"2013-04-24 12:00:01 EDT","orderedProducts":[{"quantity":1,"sku":"10GAL"}]}'

        controller.save()

        assert '{"msg":"OK"}' == controller.response.contentAsString
        assert 201 == controller.response.status
        assert 1 == Receipt.count()

        Receipt receipt = Receipt.first()
        assert receipt.kiosk == new Kiosk(name: "k1")
        assert 1 == receipt.receiptLineItems.size()
        assert receipt.receiptLineItems.first().quantity == 1
        assert receipt.receiptLineItems.first().sku == "10GAL"
    }

    @Test
    void shouldRejectInvalidTimestampFormat() {
        controller.request.json = '{"kioskId":"k1", "createdAt":1213431241234,"orderedProducts":[{"quantity":1,"sku":"10GAL"}]}'

        controller.save()

        assert 422 == controller.response.status
        assert 0 == Receipt.count()
    }

    @Test
    void shouldRejectMissingKiosk() {
        controller.request.json = '{"kioskId":"k0001", "createdAt":"2013-04-24 12:00:01 EDT","orderedProducts":[{"quantity":1,"sku":"10GAL"}]}'

        controller.save()

        assert 422 == controller.response.status
        assert 0 == Receipt.count()
    }
}
