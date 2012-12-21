package allison.zipcode

class ZipcodeService {
    def downloadService

    /**
     * Download the zipcodes, add them to the Domain, and update the tag cloud
     * @param id The country id for which to load the
     * @throws UnableToDownloadException
     */
    def load(Long id) throws UnableToDownloadException {

        def country = Country.get(id)
        if (!country) {
            throw new UnableToDownloadException(message: "Unable to find country")
        }

        // Delete old xml files
//        def dir = DownloadService.getCountryDir(country)
//        FileUtils.deleteDirectory(new File(dir))

        country.states.each {

            def file = DownloadService.getStateFileName(it)
            def address = DownloadService.getAddress(it)
            println "File: ${file}"
            try {

                // Download zip codes
//                DownloadService.download(file, address)
//
//                // Only clear the zip codes on a successful download
//                clearZipcodes(country, it)

                // Slurp and save in Domain
                def xml = new XmlSlurper().parse(file)
                def allCodes = xml.code
                def zipcode
                for (code in allCodes) {
                    zipcode = ZipcodeService.slurpZipcode(code)
                    ZipcodeService.addZipcodeToCountry(country, zipcode)
                }

            } catch (FileNotFoundException ex) {
                throw new UnableToDownloadException(message: "Unable to create ${file} from download")
            }
            println "Num zipcodes: " + it?.zipcodes?.size()
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
        def state = State.findByAbbreviationAndCountryCode(zipcode.adminCode1, zipcode.countryCode)
        // State has been initialized

        if (state) {
            // Only add the zipcode if it is valid
            state.addToZipcodes(zipcode)
            if (zipcode.validate()) {
                state.save()//flush: true)
            } else {
                state.removeFromZipcodes(zipcode)
                zipcode.discard()
            }
        }
    }


    /**
     * Clear all zip code data
     */
    static clearZipcodes(Long id) {
        def country = Country.get(id)

        if (!country) { // If the country doesn't exist, there is nothing to do
            return
        }

        // For each state, for each zipcode, delete it
        if (country.states) {
            country.states.each { state ->
                clearZipcodes(country, state)
            }
        } // Nothing to do if there are no states
    }

    static clearZipcodes(Country country, State state) {

        if (!country || !state) { // If the country doesn't exist, there is nothing to do
            return
        }

        // For each zipcode, delete it
                if(state.zipcodes) {
                    def tmp = []
                    tmp.addAll(state.zipcodes)
                    tmp.each { zipcode ->
                        state.removeFromZipcodes(zipcode)
                        zipcode.delete()
                    }
                } // Nothing to do if there are no zipcodes
    }

    /**
     * Generate the map of zipcodes per state for a tag cloud
     * @param id The country for which to create the cloud
     * @return  The map containing zip code counts keyed by state
     * name
     */
    static generateTagCloud(Long id) {
        def country = Country.get(id)
        if (!country) { // Zip codes are an empty map if the country is missing
            return [:]
        }
        return country.states.inject([:]) { result, state ->
            result[state.name] = state.zipcodes.size()
            result
            }
    }



}
