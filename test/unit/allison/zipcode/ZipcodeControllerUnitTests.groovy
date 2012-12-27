package allison.zipcode



import org.junit.*
import grails.test.mixin.*

@TestFor(ZipcodeController)
@Mock([Zipcode, State, Country])
class ZipcodeControllerUnitTests {
    def state

    void setUp() {
        def country = new Country(name: "United States of America", countryCode: "US").save()
        state = new State(name: "Minnesota", abbreviation: "MN", countryCode: "US")
        country.addToStates(state)
        country.save(flush: true)
    }

    def populateValidParams(params) {
        assert params != null
        params["postalCode"] = "55082"
        params["name"] = "Stillwater"
        params["countryCode"] = "US"
        params["lat"] = 45.06142
        params["lng"] = -92.84736
        params["adminCode1"] = "MN"
        params["adminName1"] = "Minnesota"
        params["adminCode2"] = "163"
        params["adminName2"] = "Washington"
        params["state"] = state
    }

    void testIndex() {
        controller.index()
        assert "/zipcode/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.zipcodeInstanceList.size() == 0
        assert model.zipcodeInstanceTotal == 0
    }


    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/zipcode/list'

        populateValidParams(params)
        def zipcode = new Zipcode(params)

        assert zipcode.save() != null

        params.id = zipcode.id

        def model = controller.show()

        assert model.zipcodeInstance == zipcode
    }

}
