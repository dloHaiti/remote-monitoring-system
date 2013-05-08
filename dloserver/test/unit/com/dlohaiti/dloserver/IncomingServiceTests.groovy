package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(IncomingService)
class IncomingServiceTests {

    @Before
    void setup() {
        grailsApplication.config.dloserver.readings.incoming = '/tmp/incoming'

        def readingsFolders = grailsApplication.config.dloserver.readings
        [readingsFolders.incoming].each {
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
        createIncomingFile("file1.csv") << "1,2013-12-12 00:01:02 EDT\n"

        List foundFiles = service.getIncomingFiles()
        assert service.getFileContent(foundFiles.first()) == "1,2013-12-12 00:01:02 EDT\n"
    }

    File createIncomingFile(String filename) {
        def dir = new File(grailsApplication.config.dloserver.readings.incoming.toString())
        def file = new File(dir, filename)
        file.createNewFile()
        return file
    }
}
