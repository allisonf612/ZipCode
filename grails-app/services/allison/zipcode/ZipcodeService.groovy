package allison.zipcode


import groovyx.gpars.GParsPool
import org.xml.sax.SAXParseException

import java.lang.reflect.UndeclaredThrowableException
import java.util.concurrent.locks.ReentrantLock

class UnableToProcessException extends RuntimeException {
    String message
}

class ZipcodeService {
    def downloadService
    def ReentrantLock lock = new ReentrantLock()

    /**
     * Download the zipcodes, add them to the Domain
     * @param id The country id for which to load the
     * @throws UnableToDownloadException
     */
    def load(Long id) throws UnableToProcessException {

        def country = Country.get(id)
        if (!country) {
            throw new UnableToProcessException(message: "Unable to find country")
        }

        if (!lock.tryLock()) {
            throw new UnableToProcessException(message: "Cannot load zipcodes for ${country}. Access is locked by another process")
        }

        try {
            // Delete old xml files
            def dir = DownloadService.getCountryDir(country)
            FileUtils.deleteDirectory(new File(dir))

            def start = System.currentTimeMillis()
            GParsPool.withPool {

                country.states.eachParallel { State state ->

                    def file = DownloadService.getStateFileName(state)

                    def address = downloadService.getAddress(state)
                    println "Downloading file: ${file}" //+ " from: ${address}"

                    try {
                        // Download zip codes
                        DownloadService.download(file, address)

                        // Start a new session to interact with the database
                        State.withNewSession {
                            // Only clear the zip codes on a successful download
                            ZipcodeService.clearZipcodes(state)

                            // Parse and save in Domain
                            def xml = new XmlSlurper().parse(file)
                            def zipcodes = xml.code.collect { code ->
                                ZipcodeService.parseZipcode(code)
                            }

                            // Add all the zipcodes
                            ZipcodeService.addZipcodesToState(zipcodes, state)
                            state.refresh()

                            println "Num zipcodes: " + state?.zipcodes?.size()
                        } // State.withNewSession
                    } catch (FileNotFoundException ex) {
                        throw new UnableToProcessException(message: "Unable to create ${file} from download")
                    } catch (UnableToDownloadException ex) {
                        throw new UnableToProcessException(message: ex.message)
                    } catch (UndeclaredThrowableException ex) {
                        if (ex.getCause() instanceof SAXParseException) {
                            throw new UnableToProcessException(message: "Unable to load.  Xml parse error: ${address}")
                        } else {
                            throw new UnableToProcessException(message: "Unable to load")
                        }
                    }
                } // country.states.each
            } // withPool

            println "Total time to load zipcodes: " + (System.currentTimeMillis() - start)

        } finally {
            lock.unlock()
        }
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
     * Add a list of zipcodes to the state
     * @param zipcodes The list of zipcodes to add
     * @param state The state to add the zipcodes to
     * @return
     */
    static addZipcodesToState(List<Zipcode> zipcodes, State state) {
        // Lock the state before adding zipcodes
        state = State.lock(state.id)
        zipcodes.each { zipcode ->
            state.addToZipcodes(zipcode)
            if (!zipcode.validate()) {
                state.removeFromZipcodes(zipcode)
                zipcode.discard()
            } // if
        }

        // Execute the batch save
        state.save(flush: true)
    }


    /**
     * Clear all zipcodes for a country
     * @param id The id of the country to remove zipcodes from
     * @return
     */
    def clearZipcodes(id) throws UnableToProcessException {
        def country = Country.get(id)

        if (!lock.tryLock()) {
            throw new UnableToProcessException(message: "Cannot clear zipcodes from ${country}. Access is locked by another process")
        }

        try {
            GParsPool.withPool {
                country?.states?.eachParallel { state ->
                    State.withTransaction {
                        clearZipcodes(state)
                    }
                }
            }
        } finally {
            lock.unlock()
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
        if (state.zipcodes) {
            tmp.addAll(state.zipcodes)
            tmp.each { zipcode ->
                state.removeFromZipcodes(zipcode)
                zipcode = Zipcode.lock(zipcode.id)
                zipcode.delete()
            }
            state.save(flush: true)
        }
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
