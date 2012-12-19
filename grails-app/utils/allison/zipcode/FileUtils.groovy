package allison.zipcode

/**
 * Created with IntelliJ IDEA.
 * User: Allison
 * Date: 12/18/12
 * Time: 9:38 PM
 */
class FileUtils {

    /**
     * Ported from http://www.rgagnon.com/javadetails/java-0483.html
     * @param path
     * @return
     */
    static deleteDirectory(File path) {
        if( path.exists() ) {
            def files = path.listFiles();
            for(file in files) {
                if(file.isDirectory()) {
                    deleteDirectory(file);
                }
                else {
                    file.delete();
                }
            }
        }
        return( path.delete() );
    }
}
