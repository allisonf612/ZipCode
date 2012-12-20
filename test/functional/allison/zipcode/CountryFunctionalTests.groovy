package allison.zipcode

import com.grailsrocks.functionaltest.*
import org.junit.*


class CountryFunctionalTests extends BrowserTestCase {


    void testCountryList() {
        def unitedStates = new Country(name: "United States of America",
                countryCode: "US").save()
        get("http://localhost:8080/ZipCode/country/list")
        assertTitle "Country List"
        assertContentContains "United States of America"
        assertContentContains "US"
    }

    void testNewCountry() {

        get("http://localhost:8080/ZipCode/country/create")

        form {
            name = "United States of America"
            countryCode = "US"
            //click "create"
        }
        click "Create"
        assertRedirectUrl("country/show/1")
        followRedirect()
        assertTitle("Show Country")
        assertContentContains "United States of America"
        assertContentContains "US"
        assertContentContains("Country 1 created")
    }

    void testLoad() {
        def unitedStates = new Country(name: "United States of America",
                countryCode: "US").save()
        get("http://localhost:8080/ZipCode/country/load/1")
        assertTitle("Show Country")
        assertContentContains "United States of America"
        assertContentContains "US"
        assertContentContains("Texas")
        assertContentContains("Washington")

        // check for tagCloud:
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Texas\" rel=\"1\">Texas</a>"
        assertContentContains "<a href=\"/ZipCode/state/show/1?stateName=Washington\" rel=\"1\">Washington</a>"
    }

    void testClear() {
        def unitedStates = new Country(name: "United States of America",
                countryCode: "US").save()
        get("http://localhost:8080/ZipCode/country/load/1")
        get("http://localhost:8080/ZipCode/country/clear/1")
        assertTitle("Show Country")
        assertContentContains "United States of America"
        assertContentContains "US"
        assertContentDoesNotContain("Texas")
        assertContentDoesNotContain("Washington")
    }
}

