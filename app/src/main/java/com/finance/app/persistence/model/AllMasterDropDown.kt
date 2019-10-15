package com.finance.app.persistence.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
class AllMasterDropDown : Serializable {

    @PrimaryKey
    var key: String = UUID.randomUUID().toString()
    var ApprovalStatus: ArrayList<DropdownMaster>? = null
    var AssetDetail: ArrayList<DropdownMaster>? = null
    var BankName: ArrayList<DropdownMaster>? = null
    var Branch: ArrayList<DropdownMaster>? = null
    var ChannelPartnerName: ArrayList<DropdownMaster>? = null
    var ChannelType: ArrayList<DropdownMaster>? = null
    var DOBProof: ArrayList<DropdownMaster>? = null
    var DetailQualification: ArrayList<DropdownMaster>? = null
    var DocumentProof: ArrayList<DropdownMaster>? = null
    var EntityType: ArrayList<DropdownMaster>? = null
    var Gender: ArrayList<DropdownMaster>? = null
    var IdentificationType: ArrayList<DropdownMaster>? = null
    var LoanInformationInterestType: ArrayList<DropdownMaster>? = null
    var LoanOwnership: ArrayList<DropdownMaster>? = null
    var LoanScheme: ArrayList<DropdownMaster>? = null
    var LoanType: ArrayList<DropdownMaster>? = null
    var MaritalStatus: ArrayList<DropdownMaster>? = null
    var Nationality: ArrayList<DropdownMaster>? = null
    var Obligate: ArrayList<DropdownMaster>? = null
    var OfficeType: ArrayList<DropdownMaster>? = null
    var Ownership: ArrayList<DropdownMaster>? = null
    var Qualification: ArrayList<DropdownMaster>? = null
    var ReferedBy: ArrayList<DropdownMaster>? = null
    var Relationship: ArrayList<DropdownMaster>? = null
    var Religion: ArrayList<DropdownMaster>? = null
    var RepaymentBank: ArrayList<DropdownMaster>? = null
    var SourcingChannelPartner: ArrayList<DropdownMaster>? = null
    var TypeOfOrganisation: ArrayList<DropdownMaster>? = null
    var VerifiedStatus: ArrayList<DropdownMaster>? = null
    var OccupationType: ArrayList<DropdownMaster>? = null
}