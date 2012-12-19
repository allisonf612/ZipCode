package allison.zipcode

import groovy.transform.EqualsAndHashCode


@EqualsAndHashCode
class State {
    def scaffold = true
    String name

    @Override
    String toString() {
        name
    }

    static constraints = {
        name(unique: true)
    }


    static hasMany = [zipcodes : Zipcode]
    static belongsTo = [country: Country]
}
