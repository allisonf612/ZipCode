package allison.zipcode

import org.springframework.dao.DataIntegrityViolationException

class CountryController {
    static scaffold = true

    def downloadService // Inject the download and zipcode services
    def zipcodeService

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
