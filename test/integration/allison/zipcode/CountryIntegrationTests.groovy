package allison.zipcode

import static org.junit.Assert.*
import org.junit.*

class CountryIntegrationTests {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testBootstrap() {
        def countries = Country.list()
        assertEquals 1, countries.size()

        def unitedStates = countries[0]
        assertEquals "United States of America", unitedStates.name
        assertEquals "Alaska", unitedStates.stateNames["AK"]
        assertEquals "Missouri", unitedStates.stateNames["MO"]
    }

    @Test
    void testConstraints() {
        def unitedStatesDup = new Country(name: "United States of America")

        // USA is already in the db from bootstrap, so the name will not be unique
        assertFalse unitedStatesDup.validate()
        def errors = unitedStatesDup.errors

        assertEquals "unique",
                errors.getFieldError("name").code
        assertEquals "nullable", errors.getFieldError("stateNames").code
    }

    @Test
    void testHasMany() {
        def unitedStates = Country.findByName("United States of America")
        def minnesota = new State(totalResultsCount: 1,
                                    abbreviation: "MN",
                                    fullName: "Minnesota")
        def wisconsin = new State(totalResultsCount: 1,
                                    abbreviation: "WI",
                                    fullName: "Wisconsin")

        unitedStates.addToStates(minnesota)
        unitedStates.addToStates(wisconsin)
        unitedStates.save(flush: true)

        assertEquals 2, unitedStates.states.size()
    }

    @Test
    void testCascade() {
        def unitedStates = Country.findByName("United States of America")
        def minnesota = new State(totalResultsCount: 1,
                abbreviation: "MN",
                fullName: "Minnesota")
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

        assertEquals 1, unitedStates.states.toList()[0].zipcodes.size()

        // Verify that the zipcode and state are there before delete
        assertNotNull Zipcode.get(zipcode.id)
        assertNotNull State.get(minnesota.id)

        unitedStates.delete()

        // The delete was cascaded to the zipcodes
        assertFalse Country.exists(unitedStates.id)
        assertFalse State.exists(minnesota.id)
        assertFalse Zipcode.exists(zipcode.id)
    }
}
