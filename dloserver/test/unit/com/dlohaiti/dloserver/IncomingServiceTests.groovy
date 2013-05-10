package com.dlohaiti.dloserver

import grails.test.mixin.TestFor
import org.junit.Before
import org.junit.Test

@TestFor(IncomingService)
class IncomingServiceTests {

    private static TEST_FOLDER = "target/csv/"

    @Before
    void setup() {
        grailsApplication.config.dloserver.readings.incoming = TEST_FOLDER + 'incoming'
        grailsApplication.config.dloserver.readings.processed = TEST_FOLDER + 'processed'
        grailsApplication.config.dloserver.readings.failed = TEST_FOLDER + 'failed'

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
    void shouldGetAllIncomingFiles() {
        createIncomingFile("file1")
        createIncomingFile("file2")

        List foundFiles = service.getIncomingFiles()
        assert foundFiles.size() == 2
    }

    @Test
    void shouldSkipDirectoriesInIncomingFolder() {
        createIncomingFile("file1")
        createIncomingFile("file2")
        createIncomingFolder("dir1")

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


    private File createIncomingFolder(String folderName) {
        def dir = new File(grailsApplication.config.dloserver.readings.incoming.toString())
        def folder = new File(dir, folderName)
        folder.mkdir()
        return folder
    }

    private File createIncomingFile(String filename) {
        def dir = new File(grailsApplication.config.dloserver.readings.incoming.toString())
        def file = new File(dir, filename)
        file.createNewFile()
        return file
    }
}
