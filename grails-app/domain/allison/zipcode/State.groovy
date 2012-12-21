package allison.zipcode

import groovy.transform.EqualsAndHashCode


@EqualsAndHashCode
class State {
    String name
    String abbreviation
    String countryCode

    @Override
    String toString() {
        name
    }

    static constraints = {
        name(unique: true, blank: false)
        abbreviation(unique: true, size: 2..2)
        countryCode(size: 2..2,
             validator: {countryCode, state ->
                  countryCode == state.country.countryCode
             })
    }

    static mapping = {
        sort name: "asc"
    }

    static hasMany = [zipcodes : Zipcode]
    static belongsTo = [country: Country]
}
