package allison.zipcode

import static org.junit.Assert.*
import org.junit.*

class ZipcodeServiceIntegrationTests {
    def zipcodeService

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testLoadZipcodes() {

    }

    @Test
    void testGetState() {
        def unitedStates = new Country(name: "United States of America",
                countryCode: "US")

        assertTrue unitedStates.validate()
        assertNotNull unitedStates.save()

        def stateName = "Minnesota"

        CreateNewState:
        {
            // Verify that Minnesota does not exist
            assertNull State.findByName(stateName)

            // Create the state
            def state = ZipcodeService.getState(unitedStates, stateName)
            assertTrue state in unitedStates.states
            assertNotNull State.findByName(stateName)
        }

        FindExistingState:
        {
            def minnesota = State.findByName(stateName)
            // Verify that Minnesota already exists
            assertNotNull minnesota

            // Add a zipcode to verify it is still there after the get
            def zipcode = new Zipcode(postalCode: "55082",
                    name: "Stillwater",
                    countryCode: "US",
                    lat: 45.06142,
                    lng: -92.84736,
                    adminCode1: "MN",
                    adminName1: "Minnesota",
                    adminCode2: "163",
                    adminName2: "Washington")

            minnesota.addToZipcodes(zipcode)
            minnesota.save(flush: true)

            def state = ZipcodeService.getState(unitedStates, stateName)
            assertEquals stateName, state.name
            assertTrue state in unitedStates.states
            assertNotNull State.findByName(stateName)
            assertTrue zipcode in state.zipcodes
        }

    }

    @Test
    void testClearZipcodes() {

        def unitedStates = new Country(name: "United States of America",
                                          countryCode: "US")

        assertTrue unitedStates.validate()
        assertNotNull unitedStates.save()

        // Add some states with zipcodes
        def minnesota = new State(name: "Minnesota")
        unitedStates.addToStates(minnesota)
        unitedStates.save(flush: true)

        def zipcode = new Zipcode(postalCode: "55082",
                name: "Stillwater",
                countryCode: "US",
                lat: 45.06142,
                lng: -92.84736,
                adminCode1: "MN",
                adminName1: "Minnesota",
                adminCode2: "163",
                adminName2: "Washington")

        minnesota.addToZipcodes(zipcode)
        minnesota.save(flush: true)

        def wisconsin = new State(name: "Wisconsin")
        unitedStates.addToStates(wisconsin)
        unitedStates.save(flush: true)

        def zipcode2 = new Zipcode(postalCode: "53004",
                            name: "Belgium",
                            countryCode: "US",
                            lat: 43.49946,
                            lng: -87.85091,
                            adminCode1: "WI",
                            adminName1: "Wisconsin",
                            adminCode2: "089",
                            adminName2: "Ozaukee")
        wisconsin.addToZipcodes(zipcode2)
        wisconsin.save(flush: true)

        // Verify that the states and zipcodes are there
        assertEquals 2, unitedStates.states.size()
        for (state in unitedStates.states) {
            assertEquals 1, state.zipcodes.size()
        }

        ZipcodeService.clearZipcodes(unitedStates.id)

        // Verify that the states and zipcodes are gone
        // and the country remains
        assertTrue Country.exists(unitedStates.id)
        assertEquals 0, unitedStates.states.size()
        assertFalse State.exists(minnesota.id)
        assertFalse State.exists(wisconsin.id)
        assertFalse Zipcode.exists(zipcode.id)
        assertFalse Zipcode.exists(zipcode2.id)


    }
}
