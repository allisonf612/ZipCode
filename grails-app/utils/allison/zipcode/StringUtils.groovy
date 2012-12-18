package allison.zipcode

/**
 * Created with IntelliJ IDEA.
 * User: Allison
 * Date: 12/17/12
 * Time: 5:28 PM
 */
class StringUtils {
    static stringIsIntegers = {code, zipcode ->    // Validate that the postalCode is
        // composed of integers
        try {
            for (i in code) {
                Integer.parseInt(i)
            }
        } catch(NumberFormatException ex) {
            return false
        }
        return true // No parsing errors for any of the characters
        // means they were all Integers
    }
}
