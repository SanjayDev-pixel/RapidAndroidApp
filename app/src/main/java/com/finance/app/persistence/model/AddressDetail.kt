package com.finance.app.persistence.model
import java.io.Serializable

class AddressDetail : Serializable {

    var address1: String = ""
    var address2: String = ""
    var addressID: Int? = null
    var addressProof: Int? = null
    var addressTypeDetailID: Int? = null
    var addressTypeDetail: String? = null
    var cityID: Int? = 0
    var cityName: String? = ""
    var districtID: Int? = 0
    var districtName: String? = null
    var entityID: Int? = 0
    var landmark: String = ""
    var rentAmount: Int? = 0
    var residenceTypeTypeDetailID: Int? = null
    var zip: String = ""
    var stateID: Int? = null
    var stateName: String? = null
    var stayingInYears: Int? = 0

}