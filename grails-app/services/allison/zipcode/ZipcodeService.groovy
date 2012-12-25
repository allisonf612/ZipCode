package allison.zipcode


import groovyx.gpars.GParsPool


class ZipcodeService {
    def downloadService


    /**
     * Download the zipcodes, add them to the Domain
     * @param id The country id for which to load the
     * @throws UnableToDownloadException
     */
    def load(Long id) throws UnableToDownloadException {

        def country = Country.get(id)
        if (!country) {
            throw new UnableToDownloadException(message: "Unable to find country")
        }

        // Delete old xml files
        def dir = DownloadService.getCountryDir(country)
        FileUtils.deleteDirectory(new File(dir))

        def start = System.currentTimeMillis()
        GParsPool.withPool {

            country.states.eachParallel { State state ->

                def file = DownloadService.getStateFileName(state)

                try {
                    def address = downloadService.getAddress(state)
                    println "Downloading file: ${file}"

                    // Download zip codes
                    def downloadStart = System.currentTimeMillis()
                    DownloadService.download(file, address)
                    println "Download time: " + (System.currentTimeMillis() - downloadStart)
                    Zipcode.withTransaction {
                        State.withTransaction {

                            // Only clear the zip codes on a successful download
                            ZipcodeService.clearZipcodes(state)

                            // Slurp and save in Domain
                            def xml = new XmlSlurper().parse(file)
                            def zipcodes = xml.code.collect { code ->
                                ZipcodeService.parseZipcode(code)
                            }

                            // Lock the state
                            state = State.lock(state.id)
                            zipcodes.each {
                                ZipcodeService.addZipcodeToState(state, it)
                            }

                            // Execute the batch save
                            def saveStart = System.currentTimeMillis()
                            state.save(flush: true)
                            println "State batch save zipcodes: " + (System.currentTimeMillis() - saveStart)

                            state = State.lock(state.id)
                            println "Num zipcodes: " + state?.zipcodes?.size()
                        } // State.withTransaction
                    } // Zipcode.withTransaction
                } catch (FileNotFoundException ex) {
                    throw new UnableToDownloadException(message: "Unable to create ${file} from download")
                } catch (UnableToDownloadException ex) {
                    throw ex
                }
            } // country.states.each
        } // withPool

        println "Total time to load zipcodes: " + (System.currentTimeMillis() - start)

    }

    /**
     * Parse the zipcode from this segment of xml and add it to its State
     * @param country
     * @param xml
     * @return
     */
    static parseZipcode(xml) {
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

    /**
     * Add a zipcode to the state or discard it if it is not
     * valid
     * @param state The state to add the zipcode to
     * @param zipcode The zipcode to add
     * @return
     */
    static addZipcodeToState(State state, Zipcode zipcode) {
        if (state) {  // Only add to a state that exists
            state.addToZipcodes(zipcode)
            if (!zipcode.validate()) {
                state.removeFromZipcodes(zipcode)
                zipcode.discard()
            } // if
        }  else {
            zipcode.discard() // Get rid of the zipcode if there is no state
        }
    }


    /**
     * Clear all zipcodes for a country
     * @param id The id of the country to remove zipcodes from
     * @return
     */
    static clearZipcodes(Long id) {
        def country = Country.get(id)

//        if (!country) { // If the country doesn't exist, there is nothing to do
//            return
//        }

        // For each state, for each zipcode, delete it
//        if (country.states) {
        GParsPool.withPool {
            country?.states?.eachParallel { state ->
                State.withTransaction {
                    clearZipcodes(state)
                }
            }
        }
    }


    /**
     * Clear all zipcode data for a state
     * @param state The state to remove zipcodes from
     * @return
     */
    static clearZipcodes(State state) {
            def tmp = []
            state = State.lock(state.id)
            if (state?.zipcodes) {
                tmp.addAll(state.zipcodes)
                tmp.each { zipcode ->
                    state.removeFromZipcodes(zipcode)
                    zipcode = Zipcode.lock(zipcode.id)
                    zipcode.delete()
                }
                state.save(flush: true)
            }
//        } // Nothing to do if there are no zipcodes
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
