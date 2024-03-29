package allison.zipcode

class UnableToDownloadException extends RuntimeException {
    String message
}

class DownloadService {

    /**
     * Download from the address to the file called filename
     * @param filename The file to save the downloaded data
     * @param address The address to get the data from
     * @return void
     */
    static download(String filename, String address)
    {
        File file = new File(filename)
        file.getParentFile().mkdirs()
        file.createNewFile()

        def fileOut = new FileOutputStream(file)
        def out = new BufferedOutputStream(fileOut)
        try {
            out << new URL(address).openStream()
        } catch (UnknownHostException ex) {
            // Cleanup
            println "Deleting file"
            file.delete()

            throw new UnableToDownloadException(message: "Unable to download from url: ${address}")
        } catch (IOException ex) {
            throw new UnableToDownloadException(message: "Unable to download from url: ${address}")
        }
        finally {
            out.close()

        }

    }

    static String getCountryDir(Country country) {
        def prefix = "web-app/data/"
        if (country?.countryCode) {
            return "${prefix}${country.countryCode}/"
        }
        return prefix
    }

    /**
     * Return the name of the file to save the download. Returns temp
     * if country is null.
     * @param country
     * @return
     */
    static String getStateFileName(State state) {
        def prefix = getCountryDir(state?.country)
        if (state?.abbreviation) {
            return "${prefix}${state.abbreviation}"
        }
        return "${prefix}temp"
    }

    String getAddress(State state) {
        "http://api.geonames.org/postalCodeSearch?placename=${state.abbreviation}&username=allisoneer"
    }

    def setGetAddressForTest() {
        DownloadService.metaClass."getAddress" = {State state ->
            File downloadFile = new File("web-app/source/${state.abbreviation}")
            downloadFile.getParentFile().mkdirs()
            downloadFile.createNewFile()
            def downloadFilename = downloadFile.absolutePath
            "file://${downloadFilename}"
        }
    }

    def resetGetAddress() {
        DownloadService.metaClass."getAddress" = {State state ->
            "http://api.geonames.org/postalCodeSearch?placename=${state.abbreviation}&username=allisoneer"
        }
    }

}
