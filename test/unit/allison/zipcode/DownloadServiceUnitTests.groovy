package allison.zipcode



import grails.test.mixin.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(DownloadService)
class DownloadServiceUnitTests {

    void setUp() {

    }

    void tearDown() {
        FileUtils.deleteDirectory(new File("test/temp"))
    }

    /**
     * Test DownloadService.getStateFileName
     */
    void testGetCountryFileName() {
        def unitedStates = new Country(name: "United States of America",
                countryCode: "US")
        assertNotNull unitedStates

        assertEquals "web-app/data/US",
                DownloadService.getStateFileName(unitedStates)

        assertEquals "web-app/data/temp",
                DownloadService.getStateFileName(null)
    }

    void testSuccessfulDownload() {
        // Write a temporary test file
        File uploadFile = new File("test/temp/upload_test")
        uploadFile.getParentFile().mkdirs()
        uploadFile.createNewFile()
        def fileOut = new FileOutputStream(uploadFile)
        def outs = new BufferedOutputStream(fileOut)
        String testString = "<root>Hello XML Test Download</root>"
        outs << testString
        outs.close()

        // 'Download' the test file
        def uploadFilename = uploadFile.absolutePath

        DownloadService.download("test/temp/download_test",
                        "file://${uploadFilename}")

        File downloadFile = new File("test/temp/download_test")
        def fileIn = new FileReader(downloadFile)

        def reader = new BufferedReader(fileIn)
        String downloadedString = ""
        for (line in reader.readLine()) {
            downloadedString += line
        }

        // Verify that what we uploaded is what was downloaded
        assertEquals testString, downloadedString
    }

    // TODO: test null input, test unabletodownloadexception
}
