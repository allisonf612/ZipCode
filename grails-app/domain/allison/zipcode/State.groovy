package allison.zipcode

class State {
    int totalResultsCount
    String abbreviation
    String fullName

    String toString() {
        abbreviation + " - " + fullName
    }

    static constraints = {
        totalResultsCount(size: 0..10000)
        // The abbreviation and full name must match
        // the stateNames of the country
        abbreviation(unique: true,
                validator: {abbr, state ->
                            abbr in state.country.stateNames
                            })
        // Because country.stateNames is a map, abbreviation must
        // by unique but fullName need not be
        fullName(validator: {name, state ->
            name == state.country.stateNames[state.abbreviation]
        })
    }


    static hasMany = [zipcodes : Zipcode]
    static belongsTo = [country: Country]
}
