package allison.zipcode

class DownloadService {

    /**
     * @param address
     */
    String download(String filename, String address)
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
            file.delete()

            return "Unable to download from url: ${address}"
        }
        out.close()

        return "Successfully downloaded zip codes"
    }


}
