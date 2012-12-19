package allison.zipcode



class CountryController {
    static scaffold = true
    def zipcodeService

    def tagCloud(Long id) {
        def cloudData = zipcodeService.generateTagCloud(id)

        [cloudData: cloudData]

    }

//    def create() {
//        [countryInstance: new Country(params)]
//    }



    def show(Long id) {
        def countryInstance = Country.get(id)
        if (!countryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'country.label', default: 'Country'), id])
            redirect(action: "list")
            return
        }

        def cloudData = zipcodeService.generateTagCloud(id)

        [countryInstance: countryInstance, cloudData: cloudData]
    }

    def load(Long id) {
        try {
            zipcodeService.load(id)
        } catch (UnableToDownloadException ex) {
            flash.message = ex.message
        } catch (FileNotFoundException ex) {
            flash.message = "Unable to save data f"
        }

        redirect(action: "show", id: 1)
    }

    def clear(Long id) {
        zipcodeService.clearZipcodes(id)
        zipcodeService.generateTagCloud(id)

        redirect(action: "show", id: 1)
    }



}