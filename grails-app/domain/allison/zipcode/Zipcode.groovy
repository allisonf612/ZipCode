package allison.zipcode

import groovy.transform.EqualsAndHashCode


@EqualsAndHashCode
class Zipcode {

    String postalCode
    String name
    String countryCode
    double lat
    double lng
    String adminCode1
    String adminName1
    String adminCode2
    String adminName2
    String adminCode3
    String adminName3

    @Override
    String toString() {
        postalCode
    }

    static constraints = {
        postalCode(size: 5..5, unique: true,
            validator: StringUtils.stringIsIntegers,
            nullable: false)
        name(size: 1..100,
             nullable: false)
        countryCode(size: 2..2, nullable: false,
                validator: {code, zipcode ->
                    code == zipcode.state.country.countryCode
                })
        lat(min: 0d, max: 90d,
            nullable: true)
        lng(min: -180d, max: 180d,
            nullable: true)
        // adminCode2 is the abbreviation of the state name
        adminCode1(nullable: false, validator: {abbr, zipcode ->
            abbr == zipcode.state.abbreviation
            })
        // adminName1 is the full name of the state and it must match
        // the name of the state this zipcode belongs to
        adminName1(validator: {name, zipcode ->
            name == zipcode.state.name
            }, nullable: false)
        adminCode2(size: 3..3,
            validator: StringUtils.stringIsIntegers,
            nullable: true)
        adminName2(size: 0..20, nullable: true)
        adminCode3(nullable: true) // adminCode3 and adminName3 are optional
        adminName3(nullable: true)
    }

    static mapping = {
        sort postalCode: "asc"
    }

    static belongsTo = [state: State]
}
