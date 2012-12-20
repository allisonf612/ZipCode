package allison.zipcode

import groovy.transform.EqualsAndHashCode


@EqualsAndHashCode
class State {
    String name
    String abbreviation

    @Override
    String toString() {
        name
    }

    static constraints = {
        name(unique: true, blank: false)
        abbreviation(unique: true, blank: false, size: 2..2)
    }

    static mapping = {
        sort name: "asc"
    }

    static hasMany = [zipcodes : Zipcode]
    static belongsTo = [country: Country]
}
