package allison.zipcode

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Country {

    String name
    String countryCode

    @Override
    String toString() {
        name
    }

    static constraints = {
        name(unique: true, nullable: true, blank: true)
        countryCode(unique: true, size: 2..2, blank: false)
    }


    static mapping = {
        states sort: 'name', order: 'asc' // Sort the states alphabetically by name
    }

    static hasMany = [states: State]
}

