package allison.zipcode

class DownloadService {

    /**
     * @param address
     */
    def download(String filename, String address)
    {
        File file = new File(filename)
        file.getParentFile().mkdirs()
        file.createNewFile()
        def fileOut = new FileOutputStream(file)
        def out = new BufferedOutputStream(fileOut)
        out << new URL(address).openStream()
        out.close()
    }


}
