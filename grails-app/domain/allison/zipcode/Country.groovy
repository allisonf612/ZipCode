package allison.zipcode

class Country {

    Map stateNames
    String name

    @Override
    String toString() {
        name
    }

    static constraints = {
        name(unique: true)
        stateNames(nullable: false)
    }


    static mapping = {
        states sort: 'fullName', order: 'asc' // Sort the states alphabetically by name
    }

    static hasMany = [states: State]
}

