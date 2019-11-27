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
    var Caste: ArrayList<DropdownMaster>? = null
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
    var ProfileSegment: ArrayList<DropdownMaster>? = null
    var SubProfileSegment:ArrayList<DropdownMaster>? = null
    var EmploymentType:ArrayList<DropdownMaster>? = null
    var BusinessSetupType:ArrayList<DropdownMaster>? = null
    var Industry:ArrayList<DropdownMaster>? = null
    var Sector:ArrayList<DropdownMaster>? = null
    var Constitution:ArrayList<DropdownMaster>? = null
    var AccountType:ArrayList<DropdownMaster>? = null
    var AddressProof:ArrayList<DropdownMaster>? = null
    var ResidenceType:ArrayList<DropdownMaster>? = null
    var LivingStandardIndicators: ArrayList<DropdownMaster>? = null
    var PropertyUnitType: ArrayList<DropdownMaster>? = null
    var AlreadyOwnedProperty: ArrayList<DropdownMaster>? = null
    var PropertyOwnership: ArrayList<DropdownMaster>? = null
    var NatureOfPropertyTransaction: ArrayList<DropdownMaster>? = null
    var PropertyOccupiedBy: ArrayList<DropdownMaster>? = null
    var TenantNocAvailable: ArrayList<DropdownMaster>? = null
}