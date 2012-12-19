package allison.zipcode

//enum State {
//    AK, AL, AR, AS, AZ, CA, CO, CT, DC, DE, FL,
//    FM, GA, GU, HI, IA, ID, IL, IN, KS, KY, LA,
//    MA, ME, MD, MH, MI, MN, MO, MP, MS, MT, NC,
//    ND, NE, NH, NJ, NM, NV, NY, OH, OK, OR, PA,
//    PW, RI, SC, SD, TN, TX, UT, VI, VT, VA, WA,
//    WI, WV, WY
//}


class Zipcode {

    String postalCode
    String name
    String countryCode
    double lat
    double lng
    String adminCode1
    String adminName1
    String adminCode2
    String adminName2
    String adminCode3
    String adminName3

    @Override
    String toString() {
        postalCode
    }

    static constraints = {
        postalCode(size: 5..5, unique: true,
            validator: StringUtils.stringIsIntegers,
            nullable: false)
        name(size: 1..100,
             nullable: false)
        countryCode(size: 2..2, nullable: false,
                validator: {code, zipcode ->
                    code == zipcode.state.country.countryCode
                })
        lat(min: 0d, max: 90d,
            nullable: true)
        lng(min: -180d, max: 180d,
            nullable: true)
        // adminCode1 is the abbraviation for the state name
        // and it must match the state to which this zipcode belongs
        adminCode1(
//                validator: {code, zipcode ->
//            code == zipcode.state.abbreviation
//            },
                nullable: false)
        // adminName1 is the full name of the state and it must match
        // the country's mapping from abbreviation to full name
        adminName1(validator: {name, zipcode ->
            name == zipcode.state.name
            }, nullable: false)
        adminCode2(size: 3..3,
            validator: StringUtils.stringIsIntegers,
            nullable: true)
        adminName2(size: 0..20, nullable: true)
        adminCode3(nullable: true) // adminCode3 and adminName3 are optional
        adminName3(nullable: true)
    }

    static belongsTo = [state: State]
}
