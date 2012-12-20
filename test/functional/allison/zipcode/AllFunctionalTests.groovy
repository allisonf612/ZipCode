package allison.zipcode

import com.grailsrocks.functionaltest.*


/**
 * These only work from a freshly created database because
 * the tests assume the country is saved with id "1"
 */
class AllFunctionalTests extends BrowserTestCase {

    void testNewCountry() {
        redirectEnabled = false

        get("http://localhost:8080/ZipCode/country/create")

        form("createForm") {
            name = "United States of America"
            countryCode = "US"
            click "create"
        }

        assertRedirectUrl("http://localhost:8080/ZipCode/country/show/1")
        followRedirect()
        assertTitle("Show Country")
        assertContentContains "United States of America"
        assertContentContains "US"
        assertContentContains("created")
    }


    void testCountryList() {
        get("http://localhost:8080/ZipCode/country/list")
        assertTitle "Country List"
        assertContentContains "United States of America"
        assertContentContains "US"
    }


    void testLoad() {
        get("http://localhost:8080/ZipCode/country/load/1")
        assertTitle("Show Country")
        assertContentContains "United States of America"
        assertContentContains "US"

        // check for the states
        assertContentContains("California")
        assertContentContains("Florida")
        assertContentContains("Georgia")
        assertContentContains("Idaho")
        assertContentContains("Illinois")
        assertContentContains("Indiana")
        assertContentContains("Massachusetts")
        assertContentContains("Michigan")
        assertContentContains("Minnesota")
        assertContentContains("Missouri")
        assertContentContains("Nevada")
        assertContentContains("New York")
        assertContentContains("North Carolina")
        assertContentContains("Oklahoma")
        assertContentContains("Rhode Island")
        assertContentContains("Texas")
        assertContentContains("Washington")

        // check for tagCloud:
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=California\" rel=\"22\">California</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Florida\" rel=\"12\">Florida</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Georgia\" rel=\"2\">Georgia</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Idaho\" rel=\"2\">Idaho</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Illinois\" rel=\"4\">Illinois</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Indiana\" rel=\"1\">Indiana</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Massachusetts\" rel=\"27\">Massachusetts</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Michigan\" rel=\"1\">Michigan</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Minnesota\" rel=\"1\">Minnesota</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Missouri\" rel=\"1\">Missouri</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Nevada\" rel=\"7\">Nevada</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=New+York\" rel=\"9\">New York</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=North+Carolina\" rel=\"1\">North Carolina</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Oklahoma\" rel=\"1\">Oklahoma</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Rhode+Island\" rel=\"1\">Rhode Island</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Texas\" rel=\"7\">Texas</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Washington\" rel=\"1\">Washington</a>"
    }


    void testStateList() {
        get("http://localhost:8080/ZipCode/state/list")
        assertTitle "State List"

        // check for the states
        assertContentContains("California")
        assertContentContains("Florida")
        assertContentContains("Georgia")
        assertContentContains("Idaho")
        assertContentContains("Illinois")
        assertContentContains("Indiana")
        assertContentContains("Massachusetts")
        assertContentContains("Michigan")
        assertContentContains("Minnesota")
        assertContentContains("Missouri")

        // Check second page
        get("http://localhost:8080/ZipCode/state/list?offset=10&max=10")

        assertContentContains("Nevada")
        assertContentContains("New York")
        assertContentContains("North Carolina")
        assertContentContains("Oklahoma")
        assertContentContains("Rhode Island")
        assertContentContains("Texas")
        assertContentContains("Washington")
    }

    void testZipcodeList() {

        get("http://localhost:8080/ZipCode/zipcode/list")
        assertTitle "Zipcode List"

        // Verify headings
        assertContentContains "Postal Code"
        assertContentContains "Name"
        assertContentContains "Country Code"
        assertContentContains "Lat"
        assertContentContains "Lng"
        assertContentContains "Admin Code1"

        // Verify first zipcode
        assertContentContains "00501"
        assertContentContains "Holtsville"
        assertContentContains "US"
        assertContentContains "40.922"
        assertContentContains "-72.637"
        assertContentContains "NY"

        // Check second page
        get("http://localhost:8080/ZipCode/zipcode/list?offset=10&max=10")

        // Verify first zipcode
        assertContentContains "01057"
        assertContentContains "Monson"
        assertContentContains "US"
        assertContentContains "42.101"
        assertContentContains "-72.32"
        assertContentContains "MA"
    }



    void testClear() {
        get("http://localhost:8080/ZipCode/country/clear/1")
        assertTitle("Show Country")
        assertContentContains "United States of America"
        assertContentContains "US"

        // Verify states are gone from country
        assertContentDoesNotContain("California")
        assertContentDoesNotContain("Florida")
        assertContentDoesNotContain("Georgia")
        assertContentDoesNotContain("Idaho")
        assertContentDoesNotContain("Illinois")
        assertContentDoesNotContain("Indiana")
        assertContentDoesNotContain("Massachusetts")
        assertContentDoesNotContain("Michigan")
        assertContentDoesNotContain("Minnesota")
        assertContentDoesNotContain("Missouri")
        assertContentDoesNotContain("Nevada")
        assertContentDoesNotContain("New York")
        assertContentDoesNotContain("North Carolina")
        assertContentDoesNotContain("Oklahoma")
        assertContentDoesNotContain("Rhode Island")
        assertContentDoesNotContain("Texas")
        assertContentDoesNotContain("Washington")

        // Verify states are gone from state list
        get("http://localhost:8080/ZipCode/state/list")
        assertTitle "State List"
        assertContentDoesNotContain("California")
        assertContentDoesNotContain("Florida")
        assertContentDoesNotContain("Georgia")
        assertContentDoesNotContain("Idaho")
        assertContentDoesNotContain("Illinois")
        assertContentDoesNotContain("Indiana")
        assertContentDoesNotContain("Massachusetts")
        assertContentDoesNotContain("Michigan")
        assertContentDoesNotContain("Minnesota")
        assertContentDoesNotContain("Missouri")
        assertContentDoesNotContain("Nevada")
        assertContentDoesNotContain("New York")
        assertContentDoesNotContain("North Carolina")
        assertContentDoesNotContain("Oklahoma")
        assertContentDoesNotContain("Rhode Island")
        assertContentDoesNotContain("Texas")
        assertContentDoesNotContain("Washington")

        // Verify zipcodes are gone from state list
        get("http://localhost:8080/ZipCode/zipcode/list")
        assertTitle "Zipcode List"
        assertContentDoesNotContain "75462"
        assertContentDoesNotContain "Paris"
        assertContentDoesNotContain "US"
        assertContentDoesNotContain "33.68"
        assertContentDoesNotContain "-95.491"
        assertContentDoesNotContain "TX"

    }

}


