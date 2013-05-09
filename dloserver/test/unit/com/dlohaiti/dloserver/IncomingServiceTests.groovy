package com.dlohaiti.dloserver

import grails.test.mixin.*
import org.junit.*

@TestFor(IncomingService)
class IncomingServiceTests {

    @Before
    void setup() {
        grailsApplication.config.dloserver.readings.incoming = '/tmp/incoming'
        grailsApplication.config.dloserver.readings.processed = '/tmp/processed'
        grailsApplication.config.dloserver.readings.failed = '/tmp/failed'

        def readingsFolders = grailsApplication.config.dloserver.readings
        [readingsFolders.incoming, readingsFolders.processed, readingsFolders.failed].each {
            new File(it.toString()).deleteDir()
            new File(it.toString()).mkdirs()
        }
    }

    @Test
    void shouldReturnAnEmptyListWhenThereAreNoFilesInTheIncomingFolder() {
        assert service.getIncomingFiles() == []
    }

    @Test
    void shouldIncludeAbsolutePathOnFileNames() {
        createIncomingFile("file1.csv")
        assert service.getIncomingFiles().first() == grailsApplication.config.dloserver.readings.incoming + "/file1.csv"
    }

    @Test
    void shouldGetAllIncomingFiles() {
        createIncomingFile("file1")
        createIncomingFile("file2")

        List foundFiles = service.getIncomingFiles()
        assert foundFiles.size() == 2
    }

    @Test
    void shouldGetContentOfFileAsString() {
        createIncomingFile("file1.csv") << "1,2013-12-12 00:01:02 EDT,140\n"

        List foundFiles = service.getIncomingFiles()
        assert service.getFileContent(foundFiles.first()) == "1,2013-12-12 00:01:02 EDT,140\n"
    }

    @Test
    void shouldRemoveProcessedFilesFromIncomingList() {
        createIncomingFile("file1.csv") << "1,2013-12-12 00:01:02 EDT,140\n"

        List foundFiles = service.getIncomingFiles()

        assert foundFiles.size() == 1

        assert service.markAsProcessed(foundFiles.first())

        assert service.getIncomingFiles() == []
    }


    @Test
    void shouldMoveProcessedFilesToProcessedFolder() {
        createIncomingFile("file1.csv") << "1,2013-12-12 00:01:02 EDT,140\n"

        assert service.markAsProcessed(service.getIncomingFiles().first())

        def processedFile = grailsApplication.config.dloserver.readings.processed + "/file1.csv"

        assert new File(processedFile).exists()
    }


    @Test
    void shouldRemoveFailedFilesFromIncomingList() {
        createIncomingFile("file1.csv") << "1,2013-12-12 00:01:02 EDT,140\n"

        List foundFiles = service.getIncomingFiles()

        assert foundFiles.size() == 1

        assert service.markAsFailed(foundFiles.first())

        assert service.getIncomingFiles() == []
    }


    @Test
    void shouldMoveFailedFilesToFailedFolder() {
        createIncomingFile("file1.csv") << "1,2013-12-12 00:01:02 EDT,140\n"

        assert service.markAsFailed(service.getIncomingFiles().first())

        def failedFile = grailsApplication.config.dloserver.readings.failed + "/file1.csv"

        assert new File(failedFile).exists()
    }


    private File createIncomingFile(String filename) {
        def dir = new File(grailsApplication.config.dloserver.readings.incoming.toString())
        def file = new File(dir, filename)
        file.createNewFile()
        return file
    }
}
