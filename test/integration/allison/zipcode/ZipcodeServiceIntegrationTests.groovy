package allison.zipcode

import com.sun.xml.internal.ws.wsdl.writer.document.StartWithExtensionsType

import static org.junit.Assert.*
import org.junit.*

class ZipcodeServiceIntegrationTests {
    def unitedStates
    def minnesota
    def wisconsin
    def zipcodeService
    def downloadService

    @Before
    void setUp() {
        unitedStates = Country.findByCountryCode("US")
        minnesota = State.findByAbbreviation("MN")
        wisconsin = State.findByAbbreviation("WI")
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    /**
     *  Use Dynamic methods to test load from a local source
     */
    @Test
    void testLoad() {
        def mini = Country.findByNameAndCountryCode("Mini", "MI")
        if (!mini) {
            mini = new Country(name: "Mini", countryCode: "MI").save()
        }
        def state1 = State.findByNameAndAbbreviation("State 1", "S1")
        if (!state1 || !(state1 in mini.states)) {
            state1 = new State(name: "State 1", abbreviation: "S1", countryCode: "MI")
            mini.addToStates(state1)
        }
        mini.save(flush: true)
        def state2 = State.findByNameAndAbbreviation("State 2", "S2")
        if (!state2 || !(state2 in mini.states)) {
            state2 = new State(name: "State 2", abbreviation: "S2", countryCode: "MI")
            mini.addToStates(state2)
        }
        mini.save(flush: true)
        println "State 1 id: " + state1.id
        println "State 2 id: " + state2.id

        downloadService.setGetAddressForTest()

        ZipcodeService.clearZipcodes(mini.id)
        assertNotNull mini
        zipcodeService.load(mini.id)

        assertTrue state1 in mini.states
        assertTrue state2 in mini.states
        assertEquals 2, mini.states.size()

        // There is a single zipcode in each state
        assertEquals 1, state1.zipcodes.size()
        assertTrue new Zipcode(postalCode: "03060",
                                name: "Nashua",
                                countryCode: "MI",
                                lat: 42.7564,
                                lng: -71.46668,
                                adminCode1: "S1",
                                adminName1: "State 1",
                                adminCode2: "011",
                                adminName2: "Hillsborough",) in state1.zipcodes

        assertEquals 1, state2.zipcodes.size()
        assertTrue new Zipcode(postalCode: "98104",
                name: "Seattle",
                countryCode: "MI",
                lat: 47.60363,
                lng: -122.32564,
                adminCode1: "S2",
                adminName1: "State 2",
                adminCode2: "033",
                adminName2: "King",) in state2.zipcodes


        // Reset the getAddress method:
        downloadService.resetGetAddress()
    }

    @Test
    void testSlurpZipcode() {

        def xmlTest = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<geonames>" +
                "<totalResultsCount>43634</totalResultsCount>" +
                "<code>" +
                "<postalcode>75462</postalcode>" +
                "<name>Paris</name>" +
                "<countryCode>US</countryCode>" +
                "<lat>33.68045</lat>" +
                "<lng>-95.49054</lng>" +
                "<adminCode1>TX</adminCode1>" +
                "<adminName1>Texas</adminName1>" +
                "<adminCode2>277</adminCode2>" +
                "<adminName2>Lamar</adminName2>" +
                "<adminCode3/>" +
                "<adminName3/>" +
                "</code>" +
                "</geonames>"

        def zipcode = new Zipcode (
                postalCode: "75462",
                name: "Paris",
                countryCode: "US",
                lat: 33.68045,
                lng: -95.49054,
                adminCode1: "TX",
                adminName1: "Texas",
                adminCode2: "277",
                adminName2: "Lamar",
        )

        def xml = new XmlSlurper().parseText(xmlTest)
        def allCodes = xml.code
        assertEquals 1, allCodes.size()
        assertEquals zipcode, ZipcodeService.slurpZipcode(allCodes[0])
    }


    @Test
    void testAddZipcodeToState() {

        def validZipcode = new Zipcode(postalCode: "55082",
                name: "Stillwater",
                countryCode: "US",
                lat: 45.06142,
                lng: -92.84736,
                adminCode1: "MN",
                adminName1: "Minnesota",
                adminCode2: "163",
                adminName2: "Washington")

        def invalidZipcode = new Zipcode(postalCode: "55082",
                name: "Stillwater",
                countryCode: "CA",
                lat: 45.06142,
                lng: -92.84736,
                adminCode1: "WI",
                adminName1: "Wisconsin",
                adminCode2: "163",
                adminName2: "Washington")

        // Verify zipcodes do not exist
        assertNull Zipcode.findByPostalCode(validZipcode.postalCode)
        assertNull Zipcode.findByPostalCode(invalidZipcode.postalCode)

        // successful add of valid zipcode
        ZipcodeService.addZipcodeToState(minnesota, validZipcode)
        assertTrue validZipcode in minnesota.zipcodes

        // Unsuccessful add of invalid zipcode
        ZipcodeService.addZipcodeToState(wisconsin, invalidZipcode)
        assertFalse invalidZipcode in wisconsin.zipcodes
    }


    @Test
    void testClearZipcodes() {
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
        assertEquals 1, minnesota.zipcodes.size()
        assertEquals 1, wisconsin.zipcodes.size()

        ZipcodeService.clearZipcodes(unitedStates.id)

        // Verify that the zipcodes are gone
        // and the states and country remains
        assertTrue Country.exists(unitedStates.id)
        assertTrue State.exists(minnesota.id)
        assertTrue State.exists(wisconsin.id)
        assertFalse Zipcode.exists(zipcode.id)
        assertFalse Zipcode.exists(zipcode2.id)


    }

    @Test
    void testGenerateTagCloud() {
        def canada = new Country(name: "Canada", countryCode: "CA").save()
        assertEquals [:],
            ZipcodeService.generateTagCloud(canada.id)

        def zipcode = new Zipcode(postalCode: "55082",
                name: "Stillwater",
                countryCode: "CA",
                lat: 45.06142,
                lng: -92.84736,
                adminCode1: "QE",
                adminName1: "Quebec",
                adminCode2: "163",
                adminName2: "Washington")

        def quebec = new State(name: "Quebec", abbreviation: "QE", countryCode: "CA")
        canada.addToStates(quebec)
        quebec.addToZipcodes(zipcode)
        canada.save(flush: true)

        def zipcode2 = new Zipcode(postalCode: "53004",
                name: "Belgium",
                countryCode: "CA",
                lat: 43.49946,
                lng: -87.85091,
                adminCode1: "OT",
                adminName1: "Other",
                adminCode2: "089",
                adminName2: "Ozaukee")

        def other = new State(name: "Other", abbreviation: "OT", countryCode: "CA")
        canada.addToStates(other)
        other.addToZipcodes(zipcode2)

        ZipcodeService.generateTagCloud(canada.id).each {k,v->
            assertEquals v, ["Quebec": 1, "Other": 1][k]
        }

        assertEquals ZipcodeService.generateTagCloud(canada.id).size(),
                ["Quebec": 1, "Other": 1].size()
    }
}
