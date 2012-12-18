package allison.zipcode

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class StringUtilsUnitTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testStringIsIntegers() {
        // Valid strings
        String s0 = ""
        String s1 = "9"
        String s2 = "005"
        String s3 = "55422"

        // Invalid strings
        String s4 = "Hello"
        String s5 = "6g77"
        String s6 = "55422A"

        // Second parameter is unused in the closure, so it is
        // not varied for testing
        assertTrue StringUtils.stringIsIntegers(s0, null)
        assertTrue StringUtils.stringIsIntegers(s1, null)
        assertTrue StringUtils.stringIsIntegers(s2, null)
        assertTrue StringUtils.stringIsIntegers(s3, null)

        assertFalse StringUtils.stringIsIntegers(s4, null)
        assertFalse StringUtils.stringIsIntegers(s5, null)
        assertFalse StringUtils.stringIsIntegers(s6, null)
    }
}
