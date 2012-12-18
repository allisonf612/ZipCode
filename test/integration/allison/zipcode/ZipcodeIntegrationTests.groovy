package allison.zipcode

import static org.junit.Assert.*
import org.junit.*

class ZipcodeIntegrationTests extends GroovyTestCase {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testSave() {
        def postalCode = "55082"
        def name = "Stillwater"
        def countryCode = "US"
        def lat = 45.06142
        def lng = 92.84736
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

        zipcode.validate()
        assertNotNull zipcode.save()   // zipcode is expected to be valid

        // Compare the values from the database with those used initially
        def foundZipcode = Zipcode.get(zipcode.id)

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
    void testInvalidSave() {
        def postalCode = "550824"
        def name = "Stillwater"
        def countryCode = "US"
        def lat = 100.0
        def lng = -20
        def adminCode1 = "M"
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

        assertFalse zipcode.validate()
        assertTrue zipcode.hasErrors()

        def errors = zipcode.errors

        assertEquals "size.toobig",
                errors.getFieldError("postalCode").code
        assertEquals postalCode,
                errors.getFieldError("postalCode").rejectedValue
        assertNull errors.getFieldError("name")
        assertNull errors.getFieldError("countryCode")
        // TODO: how to do range on a double in constraints/validators?
//        assertEquals "size.toobig",
//                errors.getFieldError("lat").code
//        assertEquals "size.toosmall",
//                errors.getFieldError("lng").code
//        assertEquals "validator.invalid",
        assertEquals "size.toosmall",
                errors.getFieldError("adminCode1").code
        assertNull errors.getFieldError("adminName1")
        assertEquals "validator.invalid",
                errors.getFieldError("adminCode2").code
        assertNull errors.getFieldError("adminName2")
        assertNull errors.getFieldError("adminCode3")
        assertNull errors.getFieldError("adminName3")


    }
}
