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
            name = "Canada"
            countryCode = "CA"
            click "create"
        }

        assertRedirectUrl("http://localhost:8080/ZipCode/country/show/2")
        followRedirect()
        assertTitle("Show Country")
        assertContentContains "Canada"
        assertContentContains "CA"
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
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=California\" rel=\"96\">California</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Florida\" rel=\"99\">Florida</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Georgia\" rel=\"89\">Georgia</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Idaho\" rel=\"100\">Idaho</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Illinois\" rel=\"98\">Illinois</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Indiana\" rel=\"9\">Indiana</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Massachusetts\" rel=\"100\">Massachusetts</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Michigan\" rel=\"98\">Michigan</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Minnesota\" rel=\"100\">Minnesota</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Missouri\" rel=\"85\">Missouri</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Nevada\" rel=\"98\">Nevada</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=New+York\" rel=\"99\">New York</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=North+Carolina\" rel=\"100\">North Carolina</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Oklahoma\" rel=\"100\">Oklahoma</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Rhode+Island\" rel=\"91\">Rhode Island</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Texas\" rel=\"100\">Texas</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Washington\" rel=\"96\">Washington</a>"
    }


    void testStateList() {
        get("http://localhost:8080/ZipCode/state/list")
        assertTitle "State List"

        // check for the states
        assertContentContains("Alabama")
        assertContentContains("Alaska")
        assertContentContains("Arizona")
        assertContentContains("Arkansas")
        assertContentContains("California")
        assertContentContains("Colorado")
        assertContentContains("Connecticut")
        assertContentContains("Delaware")
        assertContentContains("District of Columbia")
        assertContentContains("Florida")

        // Check second page
        get("http://localhost:8080/ZipCode/state/list?offset=10&max=10")

        assertContentContains("Georgia")
        assertContentContains("Hawaii")
        assertContentContains("Idaho")
        assertContentContains("Illinois")
        assertContentContains("Indiana")
        assertContentContains("Iowa")
        assertContentContains("Kansas")
        assertContentContains("Kentucky")
        assertContentContains("Louisiana")
        assertContentContains("Maine")
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

        // Verify states are still there
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

        // Verify tag cloud states all show 0
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=California\" rel=\"0\">California</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Florida\" rel=\"0\">Florida</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Georgia\" rel=\"0\">Georgia</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Idaho\" rel=\"0\">Idaho</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Illinois\" rel=\"0\">Illinois</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Indiana\" rel=\"0\">Indiana</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Massachusetts\" rel=\"0\">Massachusetts</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Michigan\" rel=\"0\">Michigan</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Minnesota\" rel=\"0\">Minnesota</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Missouri\" rel=\"0\">Missouri</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Nevada\" rel=\"0\">Nevada</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=New+York\" rel=\"0\">New York</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=North+Carolina\" rel=\"0\">North Carolina</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Oklahoma\" rel=\"0\">Oklahoma</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Rhode+Island\" rel=\"0\">Rhode Island</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Texas\" rel=\"0\">Texas</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Washington\" rel=\"0\">Washington</a>"

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


