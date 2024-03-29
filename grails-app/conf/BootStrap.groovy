import allison.zipcode.*

class BootStrap {

    def init = { servletContext ->
        // Always create the United States country
        createUnitedStates()
    }
    def destroy = {
    }

    void createUnitedStates() {
        def US = "US"
        def unitedStates = Country.findByCountryCode(US)

        if (! unitedStates) {
            unitedStates = new Country(name: "United States of America", countryCode: US).save(flush: true)
        }

        def stateData = ["AK": "Alaska", "AL": "Alabama", "AR": "Arkansas", "AZ": "Arizona",
                "CA": "California", "CO": "Colorado", "CT": "Connecticut",
                "DC": "District of Columbia", "DE": "Delaware",
                "FL": "Florida",
                "GA": "Georgia",
                "HI": "Hawaii",
                "IA": "Iowa", "ID": "Idaho", "IL": "Illinois", "IN": "Indiana",
                "KS": "Kansas", "KY": "Kentucky",
                "LA": "Louisiana",
                "MA": "Massachusetts", "ME": "Maine", "MD": "Maryland", "MI": "Michigan",
                "MN": "Minnesota", "MO": "Missouri", "MS": "Mississippi", "MT": "Montana",
                "NC": "North Carolina", "ND": "North Dakota", "NE": "Nebraska", "NH": "New Hampshire",
                "NJ": "New Jersey", "NM": "New Mexico", "NV": "Nevada", "NY": "New York",
                "OH": "Ohio", "OK": "Oklahoma", "OR": "Oregon",
                "PA": "Pennsylvania",
                "RI": "Rhode Island",
                "SC": "South Carolina", "SD": "South Dakota",
                "TN": "Tennessee", "TX": "Texas",
                "UT": "Utah",
                "VT": "Vermont", "VA": "Virginia",
                "WA": "Washington", "WI": "Wisconsin", "WV": "West Virginia", "WY": "Wyoming"]


        if (unitedStates) { // Add all the states to US
            def state
            stateData.each {abbr, name ->
                state = State.findByAbbreviationAndCountryCode(abbr, US)
                if (!state) {
                    state = new State(name: name, abbreviation: abbr, countryCode: US)
                    unitedStates.addToStates(state)
                } else if (state.name != name) { // State name will vary if xml names vary for
                                                 // static abbreviation and countryCode
                    state.name = name
                }

                state.validate()
                if (state.hasErrors() || !unitedStates.save(failOnError: true, flush: true)) {
                    unitedStates.removeFromStates(state)
                    unitedStates.save()
                }
//                    unitedStates.save(flush: true, failOnError: true) // save for each state
            }
        }
    }

}

