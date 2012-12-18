package allison.zipcode

class Country {

    Map stateNames
    String name

    static constraints = {
        name(unique: true)
        stateNames(nullable: false)
    }


    static mapping = {
        states sort: 'fullName', order: 'asc' // Sort the states alphabetically by name
    }

    static hasMany = [states: State]
}

