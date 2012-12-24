package allison.zipcode


import jsr166y.ForkJoinPool
import groovyx.gpars.GParsPool


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
        def dir = DownloadService.getCountryDir(country)
        FileUtils.deleteDirectory(new File(dir))

//        def file
//        def address
//        def xml
//        def allCodes
//        def zipcode
//        def slurper = new XmlSlurper()



        def start = System.currentTimeMillis()

//        country.states.each { state ->
//            def file = DownloadService.getStateFileName(state)
//            def address = downloadService.getAddress(state)
//            println "Downloading file: ${file}"
//            try {
//
//                // Download zip codes
//                DownloadService.download(file, address)
//
//                // Only clear the zip codes on a successful download
//                clearZipcodes(state)
//            } catch (FileNotFoundException ex) {
//                throw new UnableToDownloadException(message: "Unable to create ${file} from download")
//            }
//
//        }
        GParsPool.withPool {
            country.states.eachParallel { state ->
                def file = DownloadService.getStateFileName(state)
                def address = downloadService.getAddress(state)
                println "Downloading file: ${file}"
                try {

                    // Download zip codes
                    DownloadService.download(file, address)
                    Zipcode.withTransaction {
                        State.withTransaction {
                            // Only clear the zip codes on a successful download
                            clearZipcodes(state)

                            // Slurp and save in Domain
                            def xml = new XmlSlurper().parse(file)
        //                    xml = slurper.parse(file)
        //                    def allCodes = xml.code

                            def zipcode
                            //for (code in allCodes) {
                            xml.code.each {
                                zipcode = ZipcodeService.slurpZipcode(it)
                                ZipcodeService.addZipcodeToState(state, zipcode)
                            }

                        } // State.withTransaction
                    } // Zipcode.withTransaction
                } catch (FileNotFoundException ex) {
                    throw new UnableToDownloadException(message: "Unable to create ${file} from download")
                }
                println "Num zipcodes: " + state?.zipcodes?.size()
            } // country.states.each
        } // withPool
//        session.merge(country)
//        }// withTransaction

//        // Wait for all the threads to finish
//        threads.each {
//            try {
//                it.join()
//            } catch (InterruptedException ignore) { println "Interrupted Exception"}
//        }

        println "Total time to load zipcodes: " + (System.currentTimeMillis() - start)

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


    static addZipcodeToState(State state, Zipcode zipcode) {
        if (state) {
            // Only add the zipcode if it is valid
//            state = state.lock(state.id)
//            state.addToZipcodes(zipcode)
//            Zipcode.withTransaction {
//                State.withTransaction {
                    state = State.lock(state.id) // Without this lock, zipcode validation doesn't work
                    state.addToZipcodes(zipcode)
//                    if (zipcode.validate()) {
//                        state = state.lock(state.id)
//                        state.save()
//                    } else {
//                        state.removeFromZipcodes(zipcode)
//                        zipcode.discard()
//                    }

                    zipcode.validate()
//                    state = State.lock(state.id)
                    if (zipcode.hasErrors() ||
                            ((state = State.lock(state.id)) && !state.save(failOnError: true, flush: true))) {
                        state = State.lock(state.id)
                        state.removeFromZipcodes(zipcode)
                        zipcode.discard()
                    }


//                    state = state.lock(state.id)
//                    if (! state.save()) {
//                        state.removeFromZipcodes(zipcode)
//                        zipcode.discard()
//                    }
//                } // State.withTransaction
//            } // Zipcode.withTransaction
        }
    }


    /**
     * Clear all zip code data
     */
    static clearZipcodes(Long id) {
        def country = Country.get(id)

//        if (!country) { // If the country doesn't exist, there is nothing to do
//            return
//        }

        // For each state, for each zipcode, delete it
//        if (country.states) {

            country?.states?.each { state ->
                clearZipcodes(state)
            }
//        } // Nothing to do if there are no states
    }

    static clearZipcodes(State state) {

//        if (!country || !state) { // If the country doesn't exist, there is nothing to do
//            return
//        }

        // For each zipcode, delete it
//        if(state?.zipcodes)
//        state = State.lock(state.id)
//        state?.zipcodes?.each {
            def tmp = []
            tmp.addAll(state?.zipcodes)
            tmp.each { zipcode ->
                state = State.lock(state.id)
                state.removeFromZipcodes(zipcode)
                zipcode = Zipcode.lock(zipcode.id)
                zipcode.delete()
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
