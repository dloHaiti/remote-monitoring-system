package com.dlohaiti.dloserver

import com.grailsrocks.functionaltest.APITestCase

class EndpointsTests extends APITestCase {

    public static final String MSG_OK = '{"msg":"OK"}'

    void testServerIsUpAndRunning() {
        get("/healthcheck")

        assertStatus 200
        assertContentStrict MSG_OK
    }


    void testPostingAValidReading() {
        post('/reading') {
            headers['Content-Type'] = 'application/json'
            body { """
                {"reading":{"timestamp":"2013-04-24 00:00:01 EDT","measurements":[
                    {"parameter":"PH","location":"BOREHOLE","value":"5"},
                    {"parameter":"COLOR","location":"WTU_EFF","value":"OK"}
                ]}}
            """ }
        }

        assertStatus 200
        assertContentStrict '{"msg":"OK"}'
    }

    void testPostingAnEmptyReading() {
        post('/reading')

        assertStatus 201
    }
    void testPostingAnInvalidReading() {
        post('/reading') {
            headers['Content-Type'] = 'application/json'
            body { """
                {"reading":{"measurements":[
                    {"parameter":"PH","location":"BOREHOLE","value":"5"},
                    {"parameter":"COLOR","location":"WTU_EFF","value":"OK"}
                ]}}
            """ }
        }

        assertStatus 201
    }
}
