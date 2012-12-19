package allison.zipcode

class ZipcodeService {

    def downloadService

    /**
     * Download the zipcodes, add them to the Domain, and update the tag cloud
     * @param id
     * @throws UnableToDownloadException
     */
    def load(Long id) throws UnableToDownloadException {

        def country = Country.get(id)
        if (!country) {
            throw new UnableToDownloadException(message: "Unable to find country")
        }

        def file = DownloadService.getCountryFileName(country)

        try {
            // Download zip codes
            downloadService.download(file,
                    "http://api.geonames.org/postalCodeSearch?placename=${country.countryCode}&username=allisoneer")

            // Only clear the zip codes on a successful download
            clearZipcodes(id)

            // Slurp and save in Domain
            def xml = new XmlSlurper().parse(file)
            def allCodes = xml.code
            def zipcode
            for (code in allCodes) {
                zipcode = slurpZipcode(country, xml)
                ZipcodeService.addZipcodeToCountry(country, zipcode)
            }

            // Generate Tag Cloud
            generateTagCloud(id)
        } catch (FileNotFoundException ex) {
            throw new UnableToDownloadException(message: "Unable to create ${file} from download")
        }


    }

    /**
     * Parse the zipcode from this segment of xml and add it to its State
     * @param country
     * @param xml
     * @return
     */
    static slurpZipcode(xml) {

        new Zipcode (
                postalCode: xml.postalcode.text(),
                name: xml.name.text(),
                countryCode: xml.countryCode.text(),
                lat: xml.lat.text(),
                lng: xml.lng.text(),
                adminCode1: xml.adminCode1.text(),
                adminName1: xml.adminName1.text(),
                adminCode2: xml.adminCode2.text(),
                adminName2: xml.adminName2.text(),
                adminCode3: xml.adminCode3.text(),
                adminName3: xml.adminName3.text()
        )

    }


    static addZipcodeToCountry(Country country, Zipcode zipcode) {
        def state = ZipcodeService.getState(country, zipcode.adminName1)
        // State has been initialized

        // Only add the zipcode if it is valid
        state.addToZipcodes(zipcode)
        if (zipcode.validate()) {
            state.save(flush: true)
        } else {
            state.removeFromZipcodes(zipcode)
            zipcode.discard()
        }
    }

    /**
     * Find the state in the country or create it if it is new
     * @param country
     * @param stateName
     * @return
     */
    static State getState(Country country, String stateName) {
        // Don't just match the stateName, verify the correct country
        def state = State.findByNameAndCountry(stateName, country)

        if (!state) { // First zipcode for this state so create it
            state = new State(name: stateName)
            country.addToStates(state)
            country.save(flush: true)
        }

        return state
    }


    /**
     * Clear all zip code data
     */
    static clearZipcodes(Long id) {
        def country = Country.get(id)

        if (!country) { // If the country doesn't exist, there is nothing to do
            return
        }
        // Delete Zipcodes and States from Countries and xml storage
        def tmp = []
        tmp.addAll(country.states)
        tmp.each {
            // Explicitly remove States, Zipcodes are deleted by cascade
            country.removeFromStates(it)
            it.delete()
        }
    }

    def generateTagCloud(Long id) {
        //TODO
    }



}
