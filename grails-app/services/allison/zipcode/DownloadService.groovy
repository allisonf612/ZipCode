package allison.zipcode

class UnableToDownloadException extends RuntimeException {
    String message
}

class DownloadService {

    /**
     * @param address
     */
    String download(String filename, String address)
    {
        File file = new File(filename)
        print "File: " + file
//        if (!file.getParentFile().mkdirs()) {
//            println "Dirs not made"
//        }

        if (!file.createNewFile()) {
            println "File not created"
        } else {
            println "File created"
        }

        def fileOut = new FileOutputStream(file)
        def out = new BufferedOutputStream(fileOut)
        try {
            out << new URL(address).openStream()
        } catch (UnknownHostException ex) {
            // Cleanup
            println "Deleting file"
            file.delete()

            throw new UnableToDownloadException(message: "Unable to download from url: ${address}")
        } finally {
            out.close()

        }

    }

    /**
     * Return the name of the file for download. Returns temp
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

}
