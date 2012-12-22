package allison.zipcode

import static org.junit.Assert.*
import org.junit.*

class ZipcodeIntegrationTests {
    def minnesota

    @Before
    void setUp() {
        minnesota = State.findByAbbreviation("MN")
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testValidZipcode() {
        def postalCode = "55082"
        def name = "Stillwater"
        def countryCode = "US"
        def lat = 45.06142
        def lng = -92.84736
        def adminCode1 = "MN"
        def adminName1 = "Minnesota"
        def adminCode2 = "163"
        def adminName2 = "Washington"

        def zipcode = new Zipcode(postalCode: postalCode,
                name: name,
                countryCode: countryCode,
                lat: lat,
                lng: lng,
                adminCode1: adminCode1,
                adminName1: adminName1,
                adminCode2: adminCode2,
                adminName2: adminName2)

        minnesota.addToZipcodes(zipcode)

        assertNotNull minnesota.save(flush: true) // zipcode is expected to be valid

        // Compare the values from the database with those used initially
        def foundZipcode = Zipcode.get(zipcode.id)
        assertNotNull foundZipcode

        assertEquals postalCode, foundZipcode.postalCode
        assertEquals name, foundZipcode.name
        assertEquals countryCode, foundZipcode.countryCode
        assertEquals lat, foundZipcode.lat, 0.001
        assertEquals lng, foundZipcode.lng, 0.001
        assertEquals adminCode1, foundZipcode.adminCode1
        assertEquals adminName1, foundZipcode.adminName1
        assertEquals adminCode2, foundZipcode.adminCode2
        assertEquals adminName2, foundZipcode.adminName2
        assertNull foundZipcode.adminCode3
        assertNull foundZipcode.adminName3

    }

    @Test
    void testConstraints() {
        def postalCode = "550824"
        def name = "Stillwater"
        def countryCode = "UA"
        def lat = 100.0
        def lng = -200
        def adminCode1 = "MN"
        def adminName1 = "Minnesota"
        def adminCode2 = "aa1"
        def adminName2 = "Washington"

        def zipcode = new Zipcode(postalCode: postalCode,
                name: name,
                countryCode: countryCode,
                lat: lat,
                lng: lng,
                adminCode1: adminCode1,
                adminName1: adminName1,
                adminCode2: adminCode2,
                adminName2: adminName2)

        minnesota.addToZipcodes(zipcode)
        assertFalse zipcode.validate()
        assertTrue zipcode.hasErrors()

        def errors = zipcode.errors

        assertEquals "size.toobig",
                errors.getFieldError("postalCode").code
        assertEquals postalCode,
                errors.getFieldError("postalCode").rejectedValue
        assertNull errors.getFieldError("name")
        assertEquals "validator.invalid",
                errors.getFieldError("countryCode").code
        assertEquals "max.exceeded",
                errors.getFieldError("lat").code
        assertEquals "min.notmet",
                errors.getFieldError("lng").code
        assertNull errors.getFieldError("adminCode1")
        assertNull errors.getFieldError("adminName1")
        assertEquals "validator.invalid",
                errors.getFieldError("adminCode2").code
        assertNull errors.getFieldError("adminName2")
        assertNull errors.getFieldError("adminCode3")
        assertNull errors.getFieldError("adminName3")

    }

    @Test
    void testUniqueConstraints() {
        def postalCode = "55082"
        def name = "Stillwater"
        def countryCode = "US"
        def lat = 45.06142
        def lng = -92.84736
        def adminCode1 = "MN"
        def adminName1 = "Minnesota"
        def adminCode2 = "163"
        def adminName2 = "Washington"

        def zipcode = new Zipcode(postalCode: postalCode,
                name: name,
                countryCode: countryCode,
                lat: lat,
                lng: lng,
                adminCode1: adminCode1,
                adminName1: adminName1,
                adminCode2: adminCode2,
                adminName2: adminName2)


        minnesota.addToZipcodes(zipcode)
        assertNotNull zipcode.save()

        def zipcodeDup = new Zipcode(postalCode: postalCode,
                name: name,
                countryCode: countryCode,
                lat: lat,
                lng: lng,
                adminCode1: adminCode1,
                adminName1: adminName1,
                adminCode2: adminCode2,
                adminName2: adminName2)

        minnesota.addToZipcodes(zipcodeDup)
        assertFalse zipcodeDup.validate()

        def errors = zipcodeDup.errors

        assertEquals "unique",
                errors.getFieldError("postalCode").code
    }
}

