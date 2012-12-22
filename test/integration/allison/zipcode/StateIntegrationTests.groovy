package allison.zipcode

import static org.junit.Assert.*
import org.junit.*

class StateIntegrationTests {
    def unitedStates
    def minnesota

    @Before
    void setUp() {
        unitedStates = Country.findByCountryCode("US")
        minnesota = State.findByAbbreviation("MN")
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testValidState() {
        def fake = new State(name: "Fake", abbreviation: "FK", countryCode: "US")

        unitedStates.addToStates(fake)
        assertNotNull unitedStates.save(flush: true)

        def found = State.get(fake.id)
        assertNotNull found

        assertEquals "Fake", found.name
    }

    @Test
    void testHasMany() {
        def minnesota = State.findByAbbreviation("MN")

        def zipcode1 = new Zipcode(postalCode: "55082",
                name: "Stillwater",
                countryCode: "US",
                lat: 45.06142,
                lng: -92.84736,
                adminCode1: "MN",
                adminName1: "Minnesota",
                adminCode2: "163",
                adminName2: "Washington")

        def zipcode2 = new Zipcode(postalCode: "55044",
                name: "Lakeville",
                countryCode: "US",
                lat: 44.67486,
                lng: -93.2578,
                adminCode1: "MN",
                adminName1: "Minnesota",
                adminCode2: "037",
                adminName2: "Dakota")

        minnesota.addToZipcodes(zipcode1)
        minnesota.addToZipcodes(zipcode2)
        minnesota.save(flush: true)

        assertEquals 2, minnesota.zipcodes.size()

        // Verify that zipcode exists
        assertTrue Zipcode.exists(zipcode1.id)
    }


    @Test
    void testCascadeDelete() {


        def zipcode1 = new Zipcode(postalCode: "55082",
                name: "Stillwater",
                countryCode: "US",
                lat: 45.06142,
                lng: -92.84736,
                adminCode1: "MN",
                adminName1: "Minnesota",
                adminCode2: "163",
                adminName2: "Washington")

        def zipcode2 = new Zipcode(postalCode: "55044",
                name: "Lakeville",
                countryCode: "US",
                lat: 44.67486,
                lng: -93.2578,
                adminCode1: "MN",
                adminName1: "Minnesota",
                adminCode2: "037",
                adminName2: "Dakota")

        minnesota.addToZipcodes(zipcode1)
        minnesota.addToZipcodes(zipcode2)
        minnesota.save(flush: true)

        assertEquals 2, minnesota.zipcodes.size()

        // Verify that the zipcodes are there before delete
        assertNotNull Zipcode.get(zipcode1.id)
        assertNotNull Zipcode.get(zipcode2.id)

        minnesota.country.removeFromStates(minnesota)
        minnesota.delete()

        // The delete was cascaded to the zipcodes
        assertFalse State.exists(minnesota.id)
        assertFalse Zipcode.exists(zipcode1.id)
        assertFalse Zipcode.exists(zipcode2.id)
    }
}
