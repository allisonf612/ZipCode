package allison.zipcode



import org.junit.*
import grails.test.mixin.*

@TestFor(ZipcodeController)
@Mock(Zipcode)
class ZipcodeControllerUnitTests {

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

    void testCreate() {
        def model = controller.create()

        assert model.zipcodeInstance != null
    }

    void testSave() {
        controller.save()

        assert model.zipcodeInstance != null
        assert view == '/zipcode/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/zipcode/show/1'
        assert controller.flash.message != null
        assert Zipcode.count() == 1
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

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/zipcode/list'

        populateValidParams(params)
        def zipcode = new Zipcode(params)

        assert zipcode.save() != null

        params.id = zipcode.id

        def model = controller.edit()

        assert model.zipcodeInstance == zipcode
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/zipcode/list'

        response.reset()

        populateValidParams(params)
        def zipcode = new Zipcode(params)

        assert zipcode.save() != null

        // test invalid parameters in update
        params.id = zipcode.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/zipcode/edit"
        assert model.zipcodeInstance != null

        zipcode.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/zipcode/show/$zipcode.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        zipcode.clearErrors()

        populateValidParams(params)
        params.id = zipcode.id
        params.version = -1
        controller.update()

        assert view == "/zipcode/edit"
        assert model.zipcodeInstance != null
        assert model.zipcodeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/zipcode/list'

        response.reset()

        populateValidParams(params)
        def zipcode = new Zipcode(params)

        assert zipcode.save() != null
        assert Zipcode.count() == 1

        params.id = zipcode.id

        controller.delete()

        assert Zipcode.count() == 0
        assert Zipcode.get(zipcode.id) == null
        assert response.redirectedUrl == '/zipcode/list'
    }
}
