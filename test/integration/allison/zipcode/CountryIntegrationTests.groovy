package allison.zipcode

import static org.junit.Assert.*
import org.junit.*

class CountryIntegrationTests {
    def unitedStates

    @Before
    void setUp() {
        unitedStates = new Country(name: "United States of America",
                countryCode: "US").save()
    }

    @After
    void tearDown() {
        // Tear down logic here
    }


    /**
     * Test the constraints on the properties of Country
     */
    @Test
    void testConstraints() {
        def unitedStatesDup = new Country(name: "United States of America",
                                          countryCode: "US")

        // Verify that country codes and names must be unique
        assertFalse unitedStatesDup.validate()
        def errors = unitedStatesDup.errors

        assertEquals "unique",
                errors.getFieldError("name").code
        assertEquals "unique", errors.getFieldError("countryCode").code

        // Verify that name is nullable
        def canada = new Country(countryCode: "CA")
        assertTrue canada.validate()

    }

    @Test
    void testHasMany() {
        def minnesota = new State(name: "Minnesota")
        def wisconsin = new State(name: "Wisconsin")

        unitedStates.addToStates(minnesota)
        unitedStates.addToStates(wisconsin)
        unitedStates.save(flush: true)

        assertEquals 2, unitedStates.states.size()
    }

    @Test
    void testCascade() {
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
