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
        name(unique: true, nullable: true)
        countryCode(unique: true, size: 2..2)
    }


    static mapping = {
        states sort: 'name', order: 'asc' // Sort the states alphabetically by name
    }

    static hasMany = [states: State]
}

