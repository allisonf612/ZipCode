import allison.zipcode.*

class BootStrap {

    def init = { servletContext ->
        // Always create the United States country
        createUnitedStates()
    }
    def destroy = {
    }

    void createUnitedStates() {
        def unitedStates = Country.findByName("United States of America")
        if (! unitedStates) {
            new Country(name: "United States of America",
                    stateNames:
                            [
                                    "AK": "Alaska", "AL": "Alabama", "AR": "Arkansas", "AZ": "Arizona",
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
                                    "WA": "Washington", "WI": "Wisconsin", "WV": "West Virginia", "WY": "Wyoming"
                            ]
            ).save()
        }
    }

}

