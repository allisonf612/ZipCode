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

    /**
     * Return the name of the file to save the download. Returns temp
     * if country is null.
     * @param country
     * @return
     */
    static String getCountryFileName(Country country) {
        def prefix = "web-app/data/"
        if (country?.countryCode) {
            return "${prefix}${country.countryCode}"
        }
        return "${prefix}temp"
    }

    String getAddress(Country country) {
        "http://api.geonames.org/postalCodeSearch?placename=${country.countryCode}&username=allisoneer"
    }

}
