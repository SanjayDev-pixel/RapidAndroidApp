package com.finance.app.persistence.model
import java.io.Serializable

class AddressDetail : Serializable {

    var address1: String = ""
    var address2: String = ""
    var addressID: Int? = 0
    var addressProof: Int? = 0
    var addressTypeDetailID: Int? = 0
    var addressTypeDetail: String? = ""
    var cityID: Int? = 0
    var cityName: String? = ""
    var districtID: Int? = 0
    var districtName: String? = ""
    var entityID: Int? = 0
    var landmark: String = ""
    var rentAmount: String? = ""
    var residenceTypeTypeDetailID: Int? = 0
    var zip: String = ""
    var stateID: Int? = 0
    var stateName: String? = ""
    var stayingInYears: Float? = 0.0f
    var contactNum: String? = ""
}