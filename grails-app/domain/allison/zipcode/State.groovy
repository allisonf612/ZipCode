package allison.zipcode

class State {
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
