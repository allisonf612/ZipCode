package allison.zipcode

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class FileUtilsUnitTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }


    void testDeleteDirectory() {
        // Make non-empty directory
        File tempFile = new File("test/temp/temp_file")
        tempFile.getParentFile().mkdirs()
        tempFile.createNewFile()

        // Verify it is there
        File tempDir = new File("test/temp")
        assertTrue tempDir.exists()

        // Delete directory
        FileUtils.deleteDirectory(tempDir)

        // Verify that it is gone
        assertFalse tempDir.exists()
    }


}
