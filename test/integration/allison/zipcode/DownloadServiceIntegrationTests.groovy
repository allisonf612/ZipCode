package allison.zipcode

import static org.junit.Assert.*
import org.junit.*

class DownloadServiceIntegrationTests {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    /**
     * Test DownloadService.getStateFileName
     */
    void testGetStateFileName() {
        def unitedStates = new Country(name: "United States of America",
                countryCode: "US")
        assertNotNull unitedStates
        def minnesota = new State(name: "Minnesota", abbreviation: "MN", countryCode: "US")
        unitedStates.addToStates(minnesota)
        unitedStates.save(flush: true)

        assertEquals "web-app/data/US/MN",
                DownloadService.getStateFileName(minnesota)

        assertEquals "web-app/data/temp",
                DownloadService.getStateFileName(null)
    }
}
