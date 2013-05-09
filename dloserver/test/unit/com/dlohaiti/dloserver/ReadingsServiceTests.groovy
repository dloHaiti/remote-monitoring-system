package com.dlohaiti.dloserver

import grails.test.mixin.*
import grails.test.mixin.services.ServiceUnitTestMixin
import org.junit.*

@TestFor(ReadingsService)
class ReadingsServiceTests {

    @Test
    void shouldProcessAllFilesAvailable() {
        def incoming = ['file1', 'file2', 'file3'] as Set
        def processed = [] as Set
        def failed = [] as Set
        service.incomingService = [
                getIncomingFiles: {-> incoming as List },
                markAsProcessed: { filename -> processed << filename; true },
                markAsFailed: { filename -> failed << filename; true }
        ] as IncomingService

        service.importIncomingFiles()

        assert processed == incoming
    }
}
