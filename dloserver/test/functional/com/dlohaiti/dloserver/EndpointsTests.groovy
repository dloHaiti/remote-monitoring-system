package com.dlohaiti.dloserver

import com.grailsrocks.functionaltest.APITestCase
import org.junit.BeforeClass

class EndpointsTests extends APITestCase {

    @BeforeClass
    static void setup() {
        new Kiosk(name: "k1").save()
    }

    void testServerIsUpAndRunning() {
        get("/healthcheck")

        assertStatus 200
        assertContentStrict '{"db":true}'
    }

    void testPostingAValidReading() {
        post('/reading') {
            headers['Content-Type'] = 'application/json'
            body { """
                {"kiosk":"k1", "timestamp":"2013-04-24 00:00:01 EDT","measurements":[
                    {"parameter":"PH","location":"BOREHOLE","value":"5"},
                    {"parameter":"COLOR","location":"WTU_EFF","value":"OK"}
                ]}
            """ }
        }

        assertStatus 201
        assertContentStrict '{"msg":"OK"}'
    }

    void testPostingAnEmptyReading() {
        post('/reading')

        assertStatus 422
    }

    void testPostingAnInvalidReading() {
        post('/reading') {
            headers['Content-Type'] = 'application/json'
            body { """
                {"measurements":[
                    {"parameter":"PH","location":"BOREHOLE","value":"5"},
                    {"parameter":"COLOR","location":"WTU_EFF","value":"OK"}
                ]}
            """ }
        }

        assertStatus 422
    }

    void testPostingAValidSale() {
        post('/sales') {
            headers['Content-Type'] = 'application/json'
            body { """
                {
                "kiosk":"k1",
                "timestamp":"2013-04-24 00:00:01 EDT",
                "quantity": 1,
                "sku": "10GAL"
                }
            """ }
        }

        assertStatus 201
        assertContentStrict '{"msg":"OK"}'
    }

    void testPostingAnEmptySale() {
        post('/sales')

        assertStatus 422
    }

    void testPostingAnInvalidSale() {
        post('/sales') {
            headers['Content-Type'] = 'application/json'
            body { """
                {
                "kiosk":"k1",
                "timestamp":"2013-04-24 00:00:01 EDT",
                "quantity": 1
                }
            """ }
        }

        assertStatus 422
    }
}
