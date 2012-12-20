package allison.zipcode

import groovy.transform.EqualsAndHashCode


@EqualsAndHashCode
class State {
    String name

    @Override
    String toString() {
        name
    }

    static constraints = {
        name(unique: true, blank: false)
    }

    static mapping = {
        sort name: "asc"
    }

    static hasMany = [zipcodes : Zipcode]
    static belongsTo = [country: Country]
}
