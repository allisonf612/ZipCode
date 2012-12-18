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

    static constraints = {
        postalCode(size: 5..5, unique: true,
            validator: StringUtils.stringIsIntegers)
        name(size: 1..100)
        countryCode(size: 1..20)
        lat(min: 0d, max: 90d)
        lng(min: -180d, max: 180d)
//        adminCode1(validator: {code, zipcode ->
//            code instanceof State
//        })
        adminCode1(size: 2..2)
        adminName1(size: 1..20)
        adminCode2(size: 3..3,
            validator: StringUtils.stringIsIntegers)
        adminName2(size: 0..20)
        adminCode3(nullable: true) // adminCode3 and adminName3 are optional
        adminName3(nullable: true)
    }

    static belongsTo = State
}
