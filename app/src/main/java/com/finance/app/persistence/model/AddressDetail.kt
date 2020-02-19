package com.finance.app.persistence.model
import java.io.Serializable

class AddressDetail : Serializable {

    var address1: String = ""
    var address2: String = ""
    var addressID: Int? = null
    var addressProof: Int? = null
    var addressTypeDetailID: Int? = null
    var addressTypeDetail: String? = ""
    var cityID: Int? = null
    var cityName: String? = ""
    var districtID: Int? = null
    var districtName: String? = ""
    var entityID: Int? = 0
    var landmark: String = ""
    var rentAmount: String? = ""
    var residenceTypeTypeDetailID: Int? = null
    var zip: String = ""
    var stateID: Int? = null
    var stateName: String? = ""
    var stayingInYears: Float? = 0.0f
    var contactNum: String? = ""
}