package allison.zipcode

import java.lang.reflect.UndeclaredThrowableException

import static org.junit.Assert.*
import org.junit.*

class ZipcodeServiceIntegrationTests extends GroovyTestCase {
    def zipcodeService
    def downloadService

    static transactional = false

    def unitedStates
    def minnesota
    def wisconsin
    def texas

    /**
     *
     */
    @Before
    void setUp() {
        unitedStates = Country.findByCountryCode("US")
        minnesota = State.findByAbbreviation("MN")
        wisconsin = State.findByAbbreviation("WI")
        texas = State.findByAbbreviation("TX")
    }

    @After
    void tearDown() {
        Country.all.each { country ->
            zipcodeService.clearZipcodes(country.id)
        }
    }
    def setUpFauxLoad() {
        def mini = Country.findByNameAndCountryCode("Mini", "MI")
        if (!mini) {
            mini = new Country(name: "Mini", countryCode: "MI").save()
        }
        def state1 = State.findByNameAndAbbreviation("State 1", "S1")
        if (!state1 || !(state1 in mini.states)) {
            state1 = new State(name: "State 1", abbreviation: "S1", countryCode: "MI")
            mini.addToStates(state1)
        }
        def state2 = State.findByNameAndAbbreviation("State 2", "S2")
        if (!state2 || !(state2 in mini.states)) {
            state2 = new State(name: "State 2", abbreviation: "S2", countryCode: "MI")
            mini.addToStates(state2)
        }
        mini.save(flush: true)

        downloadService.setGetAddressForTest()

        [mini, state1, state2]
    }

    def tearDownFauxLoad(mini) {
        mini.delete()

        downloadService.resetGetAddress()

    }


    /**
     *  Use Dynamic methods to test load from a local source
     */
    @Test
    void testFauxLoad() {
        def setup = setUpFauxLoad()
        def mini = setup[0]
        def state1 = setup[1]
        def state2 = setup[2]

        zipcodeService.load(mini.id)

        // mini has been modified
        mini.refresh()

        assertEquals 2, Zipcode.all.size()

        // There is a single zipcode in each state
        assertEquals 1, state1.zipcodes.size()
        assertTrue getState1Zipcode() in state1.zipcodes

        assertEquals 1, state2.zipcodes.size()
        assertTrue getState2Zipcode() in state2.zipcodes

        tearDownFauxLoad(mini)
    }


    @Test
    void testLoad() {
        assertEquals 0, Zipcode.all.size()

        zipcodeService.load(unitedStates.id)

        // unitedStates has been modified
        unitedStates.refresh()

        assert getMNZipcode() in minnesota.zipcodes
        assert getWIZipcode() in wisconsin.zipcodes
    }


    @Test
    void testParseZipcode() {
        State.withTransaction { status ->
            // Parsing a valid zipcode
            def xmlTest = getXmlTest()
            def zipcode = getTXZipcode()

            def xml = new XmlSlurper().parseText(xmlTest)
            assertEquals zipcode, ZipcodeService.parseZipcode(xml.code)

            // Parsing an invalid zipcode does not throw any error
            // it simply does not validate
            def xmlInvalidTest = getXmlInvalidTest()
            def invalidZipcode = getInvalidZipcode()

            def invalidXml = new XmlSlurper().parseText(xmlInvalidTest)
            def parsedInvalidZipcode = ZipcodeService.parseZipcode(invalidXml.code)
            assertEquals invalidZipcode, parsedInvalidZipcode
            texas.addToZipcodes(parsedInvalidZipcode)
            println "Validate: " + parsedInvalidZipcode.validate()

            // Roll everything back
            status.setRollbackOnly()
        }
    }

    /**
     * Test the Exception thrown on an "bad download" (empty file)
     */
    void testParseZipcodeException() {
        def setup = setUpFauxLoad()
        def mini = setup[0]
        def state3 = new State(name: "State 3", abbreviation: "S3", countryCode: "MI")
        mini.addToStates(state3)
        mini.save(flush: true)

        def message = shouldFail(UnableToProcessException) { zipcodeService.load(mini.id)}
        assertEquals "Unable to load.  Xml parse error: file:///Users/lightning/IdeaProjects/ZipCode/web-app/source/S3",
                message

        mini.refresh()

        tearDownFauxLoad(mini)
    }


    @Test
    void testAddZipcodeToState() {
        State.withTransaction { status ->
            def validZipcode = getMNZipcode()

            def invalidZipcode = getCAZipcode1()

            // Verify zipcodes do not exist
            assertNull Zipcode.findByPostalCode(validZipcode.postalCode)
            assertNull Zipcode.findByPostalCode(invalidZipcode.postalCode)

            // successful add of valid zipcode
            assertFalse validZipcode in minnesota.zipcodes
            ZipcodeService.addZipcodeToState(minnesota, validZipcode)
            assertTrue validZipcode in minnesota.zipcodes

            // Unsuccessful add of invalid zipcode
            ZipcodeService.addZipcodeToState(wisconsin, invalidZipcode)
            assertFalse invalidZipcode in wisconsin.zipcodes

            // Teardown
            minnesota.removeFromZipcodes(validZipcode)
            validZipcode.discard()
            invalidZipcode.discard()

            // Rollback the transaction
            status.setRollbackOnly()
        }
    }


    @Test
    void testClearZipcodes() {
        def zipcode = getMNZipcode()

        minnesota.addToZipcodes(zipcode)
        minnesota.save(flush: true)

        def zipcode2 = getWIZipcode()
        wisconsin.addToZipcodes(zipcode2)
        wisconsin.save(flush: true)

        // Verify that the states and zipcodes are there
        assertEquals 1, minnesota.zipcodes.size()
        assertEquals 1, wisconsin.zipcodes.size()

        zipcodeService.clearZipcodes(unitedStates.id)

        // Verify that the zipcodes are gone
        // and the states and country remains
        assertTrue Country.exists(unitedStates.id)
        assertTrue State.exists(minnesota.id)
        assertTrue State.exists(wisconsin.id)
        assertFalse Zipcode.exists(zipcode.id)
        assertFalse Zipcode.exists(zipcode2.id)
        assertEquals 0, Zipcode.all.size()


    }

    def setupCanada() {
        def canada = new Country(name: "Canada", countryCode: "CA").save()
        assertEquals [:],
                ZipcodeService.generateTagCloud(canada.id)

        def quebec = new State(name: "Quebec", abbreviation: "QE", countryCode: "CA")
        canada.addToStates(quebec)
        quebec.addToZipcodes(getCAZipcode1())
        canada.save(flush: true)

        def other = new State(name: "Other", abbreviation: "OT", countryCode: "CA")
        canada.addToStates(other)
        other.addToZipcodes(getCAZipcode2())

        canada
    }

    @Test
    void testGenerateTagCloud() {
        State.withTransaction { status ->
            def canada = setupCanada()

            ZipcodeService.generateTagCloud(canada.id).each {k,v->
                assertEquals v, ["Quebec": 1, "Other": 1][k]
            }

            assertEquals ZipcodeService.generateTagCloud(canada.id).size(),
                    ["Quebec": 1, "Other": 1].size()

            status.setRollbackOnly()
        }
    }

/********************
 * Factory Methods
 ********************/

    String getXmlTest() {
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
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
    }


    String getXmlInvalidTest() {
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<geonames>" +
                "<totalResultsCount>43634</totalResultsCount>" +
                "<code>" +
                "<postalcode>75462</postalcode>" +
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
    }

    Zipcode getInvalidZipcode() {
        new Zipcode (
                postalCode: "75462",
                name: "",
                countryCode: "US",
                lat: 33.68045,
                lng: -95.49054,
                adminCode1: "TX",
                adminName1: "Texas",
                adminCode2: "277",
                adminName2: "Lamar",
        )
    }

    Zipcode getState1Zipcode() {
        new Zipcode(postalCode: "03060",
                name: "Nashua",
                countryCode: "MI",
                lat: 42.7564,
                lng: -71.46668,
                adminCode1: "S1",
                adminName1: "State 1",
                adminCode2: "011",
                adminName2: "Hillsborough",)
    }

    Zipcode getState2Zipcode() {
        new Zipcode(postalCode: "98104",
                name: "Seattle",
                countryCode: "MI",
                lat: 47.60363,
                lng: -122.32564,
                adminCode1: "S2",
                adminName1: "State 2",
                adminCode2: "033",
                adminName2: "King",)
    }

    def getTXZipcode() {
        new Zipcode (
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
    }

    Zipcode getMNZipcode() {
        new Zipcode(postalCode: "55082",
                name: "Stillwater",
                countryCode: "US",
                lat: 45.06142,
                lng: -92.84736,
                adminCode1: "MN",
                adminName1: "Minnesota",
                adminCode2: "163",
                adminName2: "Washington")
    }

    Zipcode getWIZipcode() {
        new Zipcode(postalCode: "53004",
                name: "Belgium",
                countryCode: "US",
                lat: 43.49946,
                lng: -87.85091,
                adminCode1: "WI",
                adminName1: "Wisconsin",
                adminCode2: "089",
                adminName2: "Ozaukee")
    }

    Zipcode getCAZipcode1() {
        new Zipcode(postalCode: "55082",
                name: "Stillwater",
                countryCode: "CA",
                lat: 45.06142,
                lng: -92.84736,
                adminCode1: "QE",
                adminName1: "Quebec",
                adminCode2: "163",
                adminName2: "Washington")
    }

    Zipcode getCAZipcode2() {
        new Zipcode(postalCode: "53004",
                name: "Belgium",
                countryCode: "CA",
                lat: 43.49946,
                lng: -87.85091,
                adminCode1: "OT",
                adminName1: "Other",
                adminCode2: "089",
                adminName2: "Ozaukee")
    }
}
