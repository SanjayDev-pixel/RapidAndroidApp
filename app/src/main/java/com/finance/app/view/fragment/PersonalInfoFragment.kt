package com.finance.app.view.fragment

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentPersonalBinding
import com.finance.app.databinding.PopUpVerifyOtpBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.model.Modals.AddKyc
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.*
import com.finance.app.presenter.presenter.*
import com.finance.app.utility.*
import com.finance.app.view.adapters.recycler.Spinner.CitySpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.DistrictSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.StatesSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.delete_dialog.view.*
import kotlinx.android.synthetic.main.pop_up_verify_otp.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.exGone
import motobeans.architecture.util.exVisible
import javax.inject.Inject

class PersonalInfoFragment : BaseFragment(), LoanApplicationConnector.PostLoanApp,
        LoanApplicationConnector.GetLoanApp, PinCodeDetailConnector.PinCode,
        ApplicantsAdapter.ItemClickListener, ApplicantsAdapter.ItemLongClickListener,
        DistrictCityConnector.District, DistrictCityConnector.City,
        OTPConnector.SendOTP, OTPConnector.VerifyOTP, CoApplicantsConnector.CoApplicants {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentPersonalBinding
    private lateinit var mContext: Context
    private var mLead: AllLeadMaster? = null
    private val frag = this
    private val loanAppPostPresenter = LoanAppPostPresenter(this)
    private val loanAppGetPresenter = LoanAppGetPresenter(this)
    private val pinCodePresenter = PinCodeDetailPresenter(this)
    private val districtPresenter = DistrictPresenter(this)
    private val sendOTPPresenter = SendOTPPresenter(this)
    private val verifyOTPPresenter = VerifyOTPPresenter(this)
    private val cityPresenter = CityPresenter(this)
    private var coApplicantsPresenter = CoApplicantsPresenter(this)
    private var applicantAdapter: ApplicantsAdapter? = null
    private var personalApplicantsList: ArrayList<PersonalApplicantsModel>? = ArrayList()
    private var personalInfoMaster: PersonalInfoMaster? = PersonalInfoMaster()
    private var currentApplicant: PersonalApplicantsModel = PersonalApplicantsModel()
    private var contactDetail: ContactDetail? = ContactDetail()
    private var image: Bitmap? = null
    private var maritalStatus: DropdownMaster? = null
    private var mPinCode: String = ""
    private var personalAddressDetail: ArrayList<AddressDetail>? = ArrayList()
    private var currentPosition = 0
    private var mStateId: String = ""
    private var mDistrictId: String = ""
    private var pinCodeObj: Response.PinCodeObj? = null
    private var pDraftData = PersonalApplicantList()
    private var allMasterDropDown: AllMasterDropDown = AllMasterDropDown()
    private var applicantTab: ArrayList<Response.CoApplicantsObj>? = ArrayList()
    private var relationshipList: ArrayList<DropdownMaster> = ArrayList()
    private val conversion = CurrencyConversion()
    private lateinit var verifyOTPDialog: Dialog
    private var otp: Int? = 0

    companion object {
        private const val SINGLE = 63
        private const val SELF = 221
        private const val RENTED = 253
        private const val PERMANENT = "Permanent"
        private const val COMPLETED = "Completed"
        private const val CURRENT = "Current"
        private lateinit var kycList: ArrayList<AddKyc>
        private lateinit var states: List<StatesMaster>
        private val leadAndLoanDetail = LeadAndLoanDetail()
        private val responseConversion = ResponseConversion()
        private val requestConversion = RequestConversion()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_personal)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        mLead = sharedPreferences.getLeadDetail()
        kycList = ArrayList()
        mContext = context!!
        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
        SetPersonalMandatoryField(binding)
        setDatePicker()
        checkKycDataList()
        setClickListeners()
    }

    override val leadIdForApplicant: String
        get() = mLead!!.leadID.toString()

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override val storageType: String
        get() = personalInfoMaster?.storageType!!

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        value.responseObj?.let {
            personalInfoMaster = responseConversion.toPersonalMaster(value.responseObj)
            pDraftData = personalInfoMaster?.draftData!!
            personalApplicantsList = pDraftData.applicantDetails
        }
        setCoApplicants()
        showData(personalApplicantsList)
    }

    override fun getLoanAppGetFailure(msg: String) = getDataFromDB()

    private fun saveDataToDB(personalInfo: PersonalInfoMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().personalInfoDao().insertPersonalInfo(personalInfo)
        }
    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setRelationshipList(allMasterDropDown.Relationship)
                setMasterDropDownValue(allMasterDropDown)
            }
        })
        dataBase.provideDataBaseSource().statesDao().getAllStates().observe(viewLifecycleOwner, Observer {
            states = it
            setStateDropDownValue(states)
        })
    }

    private fun setRelationshipList(dropDowns: ArrayList<DropdownMaster>?) {
        for (dropdown in dropDowns!!) {
            if (dropdown.typeDetailID != SELF) {
                relationshipList.add(dropdown)
            }
        }
    }

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().personalInfoDao().getPersonalInfo(leadIdForApplicant).observe(this, Observer { personalMaster ->
            personalMaster?.let {
                personalInfoMaster = personalMaster
                pDraftData = personalInfoMaster?.draftData!!
                personalApplicantsList = pDraftData.applicantDetails
            }
            setCoApplicants()
            showData(personalApplicantsList)
        })
    }

    private fun showData(applicantList: ArrayList<PersonalApplicantsModel>?) {
        if (applicantList != null) {
            if (applicantList.size <= 0) {
                fillFormWithLeadDetail()
                personalApplicantsList!!.add(PersonalApplicantsModel())
            } else {
                for (applicant in applicantList) {
                    if (applicant.isMainApplicant!!) {
                        currentApplicant = applicant
                        personalAddressDetail = currentApplicant.addressDetailList
                        contactDetail = currentApplicant.contactDetail
                    }
                }
            }
        }
        getDropDownsFromDB()
        fillFormWithCurrentApplicant(currentApplicant)
    }

    private fun setMasterDropDownValue(dropDown: AllMasterDropDown) {
        binding.spinnerIdentificationType.adapter = MasterSpinnerAdapter(mContext, dropDown.IdentificationType)
        binding.spinnerVerifiedStatus.adapter = MasterSpinnerAdapter(mContext, dropDown.VerifiedStatus)
        binding.basicInfoLayout.spinnerGender.adapter = MasterSpinnerAdapter(mContext, dropDown.Gender)
        binding.basicInfoLayout.spinnerNationality.adapter = MasterSpinnerAdapter(mContext, dropDown.Nationality)
        binding.basicInfoLayout.spinnerReligion.adapter = MasterSpinnerAdapter(mContext, dropDown.Religion)
        binding.basicInfoLayout.spinnerDobProof.adapter = MasterSpinnerAdapter(mContext, dropDown.DOBProof)
        binding.basicInfoLayout.spinnerCaste.adapter = MasterSpinnerAdapter(mContext, dropDown.Caste)
        binding.basicInfoLayout.spinnerQualification.adapter = MasterSpinnerAdapter(mContext, dropDown.Qualification)
        binding.basicInfoLayout.spinnerDetailQualification.adapter = MasterSpinnerAdapter(mContext, dropDown.DetailQualification)
        binding.personalAddressLayout.spinnerCurrentAddressProof.adapter = MasterSpinnerAdapter(mContext, dropDown.AddressProof)
        binding.personalAddressLayout.spinnerPermanentAddressProof.adapter = MasterSpinnerAdapter(mContext, dropDown.AddressProof)
        binding.basicInfoLayout.spinnerLivingStandard.adapter = MasterSpinnerAdapter(mContext, dropDown.LivingStandardIndicators)
        setMaritalStatus(dropDown)
        setUpResidenceTypeDropDown(binding.personalAddressLayout.spinnerCurrentResidenceType,
                dropDown.ResidenceType!!, binding.personalAddressLayout.inputLayoutCurrentRentAmount)
        setUpResidenceTypeDropDown(binding.personalAddressLayout.spinnerPermanentResidenceType,
                dropDown.ResidenceType!!, binding.personalAddressLayout.inputLayoutPermanentRentAmount)
        if (personalAddressDetail != null && personalAddressDetail!!.size > 0) {
            fillAddressInfo(personalAddressDetail!!)
        }
        fillValueInMasterDropDown()
    }

    private fun setUpResidenceTypeDropDown(spinner: Spinner, residenceType: ArrayList<DropdownMaster>, field: TextInputLayout) {
        spinner.adapter = MasterSpinnerAdapter(mContext, residenceType)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val residence = parent.selectedItem as DropdownMaster
                    if (residence.typeDetailID == RENTED) {
                        field.visibility = View.VISIBLE
                    } else {
                        field.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setStateDropDownValue(states: List<StatesMaster>) {
        binding.personalAddressLayout.spinnerCurrentState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.personalAddressLayout.spinnerCurrentDistrict.adapter = DistrictSpinnerAdapter(mContext, ArrayList())
        binding.personalAddressLayout.spinnerCurrentCity.adapter = CitySpinnerAdapter(mContext, ArrayList())
        binding.personalAddressLayout.spinnerPermanentState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.personalAddressLayout.spinnerPermanentDistrict.adapter = DistrictSpinnerAdapter(mContext, ArrayList())
        binding.personalAddressLayout.spinnerPermanentCity.adapter = CitySpinnerAdapter(mContext, ArrayList())
        setUpStateDropdown(binding.personalAddressLayout.spinnerCurrentState, AppEnums.ADDRESS_TYPE.CURRENT)
        setUpStateDropdown(binding.personalAddressLayout.spinnerPermanentState, AppEnums.ADDRESS_TYPE.PERMANENT)
    }

    private fun setUpStateDropdown(spinner: Spinner, type: AppEnums.ADDRESS_TYPE) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val state = parent.selectedItem as StatesMaster
                    mStateId = state.stateID.toString()
                    districtPresenter.callDistrictApi(addressType = type)
                }
            }
        }
    }

    private fun fillFormWithLeadDetail() {
        currentApplicant = PersonalApplicantsModel()
        currentApplicant.firstName = mLead!!.applicantFirstName
        currentApplicant.middleName = mLead!!.applicantMiddleName
        currentApplicant.lastName = mLead!!.applicantLastName
        personalAddressDetail = currentApplicant.addressDetailList
        contactDetail!!.mobile = mLead!!.applicantContactNumber
        contactDetail!!.email = mLead!!.applicantEmail
    }

    private fun fillFormWithCurrentApplicant(currentApplicant: PersonalApplicantsModel) {
        binding.basicInfoLayout.etDOB.setText(ConvertDate().convertToAppFormat(currentApplicant.dateOfBirth!!))
        binding.basicInfoLayout.cbIncomeConsidered.isChecked = currentApplicant.incomeConsidered!!
        binding.basicInfoLayout.etFatherLastName.setText(currentApplicant.fatherLastName)
        binding.basicInfoLayout.etFatherMiddleName.setText(currentApplicant.fatherMiddleName)
        binding.basicInfoLayout.etFatherFirstName.setText(currentApplicant.fatherFirstName)
        binding.basicInfoLayout.etFirstName.setText(currentApplicant.firstName)
        binding.basicInfoLayout.etMiddleName.setText(currentApplicant.middleName)
        binding.basicInfoLayout.etNumOfDependent.setText(currentApplicant.numberOfDependents.toString())
        binding.basicInfoLayout.etNumOfEarningMember.setText(currentApplicant.numberOfEarningMembers.toString())
        binding.basicInfoLayout.etLastName.setText(currentApplicant.lastName)
        binding.basicInfoLayout.etAge.setText(currentApplicant.age.toString())
        binding.basicInfoLayout.etAlternateNum.setText(currentApplicant.alternateContact.toString())
        binding.basicInfoLayout.etEmail.setText(contactDetail!!.email)
        binding.basicInfoLayout.etMobile.setText(contactDetail!!.mobile)
        if (personalAddressDetail != null && personalAddressDetail!!.size > 0) {
            fillAddressInfo(personalAddressDetail!!)
        } else {
            binding.personalAddressLayout.etCurrentAddress.setText(mLead!!.applicantAddress)
            binding.personalAddressLayout.etPermanentAddress.setText(mLead!!.applicantAddress)
        }
        if (currentApplicant.maritialStatusTypeDetailID != SINGLE) {
            binding.basicInfoLayout.etSpouseMiddleName.setText(currentApplicant.spouseMiddleName)
            binding.basicInfoLayout.etSpouseFirstName.setText(currentApplicant.spouseFirstName)
            binding.basicInfoLayout.etSpouseLastName.setText(currentApplicant.spouseLastName)
        }
        fillValueInMasterDropDown()
        checkCompletion()
    }

    private fun checkCompletion() {
        if (mLead!!.status == COMPLETED) {
            DisablePersonalForm(binding)
        }
    }

    private fun fillValueInMasterDropDown() {
        selectGenderValue(binding.basicInfoLayout.spinnerGender)
        selectNationalityValue(binding.basicInfoLayout.spinnerNationality)
        selectReligionValue(binding.basicInfoLayout.spinnerReligion)
        selectCasteValue(binding.basicInfoLayout.spinnerCaste)
        selectDobProofValue(binding.basicInfoLayout.spinnerDobProof)
        selectQualificationValue(binding.basicInfoLayout.spinnerQualification)
        selectDetailQualificationValue(binding.basicInfoLayout.spinnerDetailQualification)
        selectMaritalStatusValue(binding.basicInfoLayout.spinnerMaritalStatus)
        setUpRelationshipValue(binding.basicInfoLayout.spinnerRelationship)
        selectLivingStandardValue(binding.basicInfoLayout.spinnerLivingStandard)
    }

    private fun fillAddressInfo(addressDetailList: ArrayList<AddressDetail>) {
        fillPermanentAddressInfo(addressDetailList[1])
        fillCurrentAddressInfo(addressDetailList[0])
    }

    private fun fillCurrentAddressInfo(addressDetail: AddressDetail) {
        binding.personalAddressLayout.etCurrentAddress.setText(addressDetail.address1)
        binding.personalAddressLayout.etCurrentPinCode.setText(addressDetail.zip)
        binding.personalAddressLayout.etCurrentLandmark.setText(addressDetail.landmark)
        binding.personalAddressLayout.etCurrentRentAmount.setText(addressDetail.rentAmount.toString())
        binding.personalAddressLayout.etCurrentStaying.setText(addressDetail.stayingInYears.toString())
        selectCurrentAddressProofValue(binding.personalAddressLayout.spinnerCurrentAddressProof, addressDetail)
        selectResidenceTypeValue(binding.personalAddressLayout.spinnerCurrentResidenceType, addressDetail)
    }

    private fun fillPermanentAddressInfo(addressDetail: AddressDetail) {
        binding.personalAddressLayout.etPermanentAddress.setText(addressDetail.address1)
        binding.personalAddressLayout.etPermanentPinCode.setText(addressDetail.zip)
        binding.personalAddressLayout.etPermanentLandmark.setText(addressDetail.landmark)
        binding.personalAddressLayout.etPermanentRentAmount.setText(addressDetail.rentAmount.toString())
        binding.personalAddressLayout.etPermanentStaying.setText(addressDetail.stayingInYears.toString())
        selectResidenceTypeValue(binding.personalAddressLayout.spinnerPermanentResidenceType, addressDetail)
        selectPermanentAddressProofValue(binding.personalAddressLayout.spinnerPermanentAddressProof, addressDetail)
    }

    private fun setMaritalStatus(dropDown: AllMasterDropDown) {
        binding.basicInfoLayout.spinnerMaritalStatus.adapter = MasterSpinnerAdapter(mContext, dropDown.MaritalStatus)
        binding.basicInfoLayout.spinnerMaritalStatus?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    maritalStatus = parent.selectedItem as DropdownMaster
                    if (maritalStatus!!.typeDetailID == SINGLE) {
                        binding.basicInfoLayout.llSpouse.visibility = View.GONE
                    } else {
                        binding.basicInfoLayout.llSpouse.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun selectGenderValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.genderTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectDobProofValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.dobProofTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectLivingStandardValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.livingStandardTypeDetailId) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectNationalityValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.nationalityTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectReligionValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.religionTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun setUpRelationshipValue(spinner: Spinner) {
        if (currentPosition == 0) {
            spinner.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.Relationship)
            selectRelationshipValue(binding.basicInfoLayout.spinnerRelationship, SELF)
            spinner.isEnabled = false
        } else {
            spinner.adapter = MasterSpinnerAdapter(mContext, relationshipList)
            selectRelationshipValue(spinner, currentApplicant.relationshipTypeDetailId)
        }
    }

    private fun selectRelationshipValue(spinner: Spinner, id: Int?) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == id) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectCasteValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.casteTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectCurrentAddressProofValue(spinner: Spinner, address: AddressDetail) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == address.addressTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectPermanentAddressProofValue(spinner: Spinner, address: AddressDetail) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == address.addressTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectResidenceTypeValue(spinner: Spinner, address: AddressDetail) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == address.residenceTypeTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectMaritalStatusValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.maritialStatusTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectQualificationValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.qualificationTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectDetailQualificationValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.detailQualificationTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun getCurrentApplicant(): PersonalApplicantsModel {
        val currentApplicant = PersonalApplicantsModel()
        val caste = binding.basicInfoLayout.spinnerCaste.selectedItem as DropdownMaster?
        val detailQualification = binding.basicInfoLayout.spinnerDetailQualification.selectedItem as DropdownMaster?
        val qualification = binding.basicInfoLayout.spinnerQualification.selectedItem as DropdownMaster?
        val dobProofType = binding.basicInfoLayout.spinnerDobProof.selectedItem as DropdownMaster?
        val gender = binding.basicInfoLayout.spinnerGender.selectedItem as DropdownMaster?
        val nationality = binding.basicInfoLayout.spinnerNationality.selectedItem as DropdownMaster?
        val religion = binding.basicInfoLayout.spinnerReligion.selectedItem as DropdownMaster?
        val maritalStatus = binding.basicInfoLayout.spinnerMaritalStatus.selectedItem as DropdownMaster?
        val livingStandard = binding.basicInfoLayout.spinnerLivingStandard.selectedItem as DropdownMaster?
        val relationship = binding.basicInfoLayout.spinnerRelationship.selectedItem as DropdownMaster?
        val dependents = binding.basicInfoLayout.etNumOfDependent.text.toString()
        val earningMembers = binding.basicInfoLayout.etNumOfEarningMember.text.toString()

        currentApplicant.casteTypeDetailID = caste?.typeDetailID
        currentApplicant.detailQualificationTypeDetailID = detailQualification?.typeDetailID
        currentApplicant.qualificationTypeDetailID = qualification?.typeDetailID
        currentApplicant.dobProofTypeDetailID = dobProofType?.typeDetailID
        currentApplicant.genderTypeDetailID = gender?.typeDetailID
        currentApplicant.nationalityTypeDetailID = nationality?.typeDetailID
        currentApplicant.religionTypeDetailID = religion?.typeDetailID
        currentApplicant.relationshipTypeDetailId = relationship?.typeDetailID
        currentApplicant.maritialStatusTypeDetailID = maritalStatus?.typeDetailID
        currentApplicant.livingStandardTypeDetailId = livingStandard?.typeDetailID
        currentApplicant.leadApplicantNumber = leadAndLoanDetail.getLeadApplicantNum(currentPosition + 1)
        currentApplicant.numberOfEarningMembers = if (earningMembers == "") 0 else earningMembers.toInt()
        currentApplicant.numberOfDependents = if (dependents == "") 0 else dependents.toInt()
        currentApplicant.firstName = binding.basicInfoLayout.etFirstName.text.toString()
        currentApplicant.middleName = binding.basicInfoLayout.etMiddleName.text.toString()
        currentApplicant.lastName = binding.basicInfoLayout.etLastName.text.toString()
        currentApplicant.spouseFirstName = binding.basicInfoLayout.etSpouseFirstName.text.toString()
        currentApplicant.spouseMiddleName = binding.basicInfoLayout.etSpouseMiddleName.text.toString()
        currentApplicant.spouseLastName = binding.basicInfoLayout.etSpouseLastName.text.toString()
        currentApplicant.fatherFirstName = binding.basicInfoLayout.etFatherFirstName.text.toString()
        currentApplicant.fatherMiddleName = binding.basicInfoLayout.etFatherMiddleName.text.toString()
        currentApplicant.fatherLastName = binding.basicInfoLayout.etFatherLastName.text.toString()
        currentApplicant.dateOfBirth = ConvertDate().convertToApiFormat(binding.basicInfoLayout.etDOB.text.toString())
        currentApplicant.age = binding.basicInfoLayout.etAge.text.toString().toInt()
        currentApplicant.isMainApplicant = currentPosition == 0
        currentApplicant.incomeConsidered = binding.basicInfoLayout.cbIncomeConsidered.isChecked
        currentApplicant.alternateContact = binding.basicInfoLayout.etAlternateNum.text.toString()
        currentApplicant.contactDetail = getContactDetail()
        currentApplicant.addressDetailList = getAddressDetailList()
        return currentApplicant
    }

    private fun getContactDetail(): ContactDetail? {
        contactDetail?.email = binding.basicInfoLayout.etEmail.text.toString()
        contactDetail?.mobile = binding.basicInfoLayout.etMobile.text.toString()
        return contactDetail
    }

    private fun getAddressDetailList(): ArrayList<AddressDetail>? {
        val cAddressDetail = AddressDetail()
        val cCity = binding.personalAddressLayout.spinnerCurrentCity.selectedItem as Response.CityObj?
        val cState = binding.personalAddressLayout.spinnerCurrentState.selectedItem as StatesMaster?
        val cDistrict = binding.personalAddressLayout.spinnerCurrentDistrict.selectedItem as Response.DistrictObj?
        val cResidenceType = binding.personalAddressLayout.spinnerCurrentResidenceType.selectedItem as DropdownMaster?
        val cAddressProof = binding.personalAddressLayout.spinnerCurrentAddressProof.selectedItem as DropdownMaster?

        cAddressDetail.rentAmount = CurrencyConversion().convertToNormalValue(binding.personalAddressLayout.etCurrentRentAmount.text.toString())
        cAddressDetail.stayingInYears = binding.personalAddressLayout.etCurrentStaying.text.toString().toFloat()
        cAddressDetail.address1 = binding.personalAddressLayout.etCurrentAddress.text.toString()
        cAddressDetail.landmark = binding.personalAddressLayout.etCurrentLandmark.text.toString()
        cAddressDetail.zip = binding.personalAddressLayout.etCurrentPinCode.text.toString()
        cAddressDetail.addressTypeDetail = CURRENT
        cAddressDetail.stateID = cState?.stateID
        cAddressDetail.districtID = cDistrict?.districtID
        cAddressDetail.cityID = cCity?.cityID
        cAddressDetail.residenceTypeTypeDetailID = cResidenceType?.typeDetailID
        cAddressDetail.addressTypeDetailID = cAddressProof?.typeDetailID

        var pAddressDetail = AddressDetail()
        if (binding.personalAddressLayout.cbSameAsCurrent.isChecked) {
            pAddressDetail = cAddressDetail
        } else {
            val pCity = binding.personalAddressLayout.spinnerPermanentCity.selectedItem as Response.CityObj?
            val pState = binding.personalAddressLayout.spinnerPermanentState.selectedItem as StatesMaster?
            val pDistrict = binding.personalAddressLayout.spinnerPermanentDistrict.selectedItem as Response.DistrictObj?
            val pResidenceType = binding.personalAddressLayout.spinnerPermanentResidenceType.selectedItem as DropdownMaster?
            val pAddressProof = binding.personalAddressLayout.spinnerPermanentAddressProof.selectedItem as DropdownMaster?

            pAddressDetail.rentAmount = CurrencyConversion().convertToNormalValue(binding.personalAddressLayout.etPermanentRentAmount.text.toString())
            pAddressDetail.stayingInYears = binding.personalAddressLayout.etPermanentStaying.text.toString().toFloat()
            pAddressDetail.address1 = binding.personalAddressLayout.etPermanentAddress.text.toString()
            pAddressDetail.landmark = binding.personalAddressLayout.etPermanentLandmark.text.toString()
            pAddressDetail.zip = binding.personalAddressLayout.etPermanentPinCode.text.toString()
            pAddressDetail.residenceTypeTypeDetailID = pResidenceType?.typeDetailID
            pAddressDetail.addressTypeDetailID = pAddressProof?.typeDetailID
            pAddressDetail.stateID = pState?.stateID
            pAddressDetail.districtID = pDistrict?.districtID
            pAddressDetail.cityID = pCity?.cityID
        }
        pAddressDetail.addressTypeDetail = PERMANENT
        if (personalAddressDetail.isNullOrEmpty()) {
            personalAddressDetail?.add(0, cAddressDetail)
            personalAddressDetail?.add(1, pAddressDetail)
        } else {
            personalAddressDetail!![0] = cAddressDetail
            personalAddressDetail!![1] = pAddressDetail
        }
        return personalAddressDetail
    }

    private fun setDatePicker() {
        binding.basicInfoLayout.etDOB.setOnClickListener {
            DateDifference(mContext, binding.basicInfoLayout.etDOB, binding.basicInfoLayout.etAge)
        }
        binding.etIssueDate.setOnClickListener {
            SelectDate(binding.etIssueDate, mContext)
        }
        binding.etExpiryDate.setOnClickListener {
            SelectDate(binding.etExpiryDate, mContext)
        }
    }

    private fun setClickListeners() {
        binding.ivUploadKyc.setOnClickListener { UploadData(frag, mContext) }
        binding.basicInfoLayout.btnGetOTP.setOnClickListener { showVerifyOTPDialog() }
        binding.btnAddKYC.setOnClickListener {
            getKycData()
            clearKycData()
        }
        binding.basicInfoLayout.ivUploadDobProof.setOnClickListener { UploadData(frag, mContext) }
        binding.btnAddApplicant.setOnClickListener { onAddCoApplicantClick() }
        binding.btnPrevious.setOnClickListener { AppEvents.fireEventLoanAppChangeNavFragmentPrevious() }
        binding.btnNext.setOnClickListener {
            if (formValidation.validatePersonalInfo(binding)) {
                saveCurrentApplicant()
                loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP)
            } else {
                showToast(getString(R.string.mandatory_field_missing))
            }
        }
        binding.personalAddressLayout.cbSameAsCurrent.setOnClickListener {
            if (binding.personalAddressLayout.cbSameAsCurrent.isChecked) {
                binding.personalAddressLayout.llPermanentAddress.visibility = View.GONE
            } else {
                binding.personalAddressLayout.llPermanentAddress.visibility = View.VISIBLE
            }
        }
        pinCodeListener(binding.personalAddressLayout.etCurrentPinCode, AppEnums.ADDRESS_TYPE.CURRENT)
        pinCodeListener(binding.personalAddressLayout.etPermanentPinCode, AppEnums.ADDRESS_TYPE.PERMANENT)
        conversion.convertToCurrencyType(binding.personalAddressLayout.etPermanentRentAmount)
        conversion.convertToCurrencyType(binding.personalAddressLayout.etCurrentRentAmount)
    }

    private fun pinCodeListener(pinCodeField: TextInputEditText?, addressType: AppEnums.ADDRESS_TYPE) {
        pinCodeField!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (pinCodeField.text!!.length == 6) {
                    mPinCode = pinCodeField.text.toString()
                    pinCodePresenter.callPinCodeDetailApi(addressType = addressType)
                }
            }
        })
    }

    override val pinCode: String
        get() = mPinCode

    override val stateId: String
        get() = mStateId

    override val districtId: String
        get() = mDistrictId

    override val loanAppRequestPost: LoanApplicationRequest
        get() = requestConversion.personalInfoRequest(getPersonalInfoMaster())

    private fun clearPermanentPinCodeField() {
        binding.personalAddressLayout.spinnerPermanentState.isEnabled = true
        binding.personalAddressLayout.spinnerPermanentDistrict.isEnabled = true
        binding.personalAddressLayout.spinnerPermanentCity.isEnabled = true
        binding.personalAddressLayout.spinnerPermanentState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.personalAddressLayout.spinnerPermanentDistrict.adapter = DistrictSpinnerAdapter(mContext, ArrayList())
        binding.personalAddressLayout.spinnerPermanentCity.adapter = CitySpinnerAdapter(mContext, ArrayList())
    }

    private fun clearCurrentPinCodeField() {
        binding.personalAddressLayout.spinnerCurrentDistrict.isEnabled = true
        binding.personalAddressLayout.spinnerCurrentCity.isEnabled = true
        binding.personalAddressLayout.spinnerCurrentState.isEnabled = true
        binding.personalAddressLayout.spinnerCurrentState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.personalAddressLayout.spinnerCurrentDistrict.adapter = DistrictSpinnerAdapter(mContext, ArrayList())
        binding.personalAddressLayout.spinnerCurrentCity.adapter = CitySpinnerAdapter(mContext, ArrayList())
    }

    override fun getPinCodeFailure(msg: String) = clearPinCodes()

    override fun getPinCodeSuccess(value: Response.ResponsePinCodeDetail, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj!!.size > 0) {
            pinCodeObj = value.responseObj[0]
            when (addressType?.type) {
                AppEnums.ADDRESS_TYPE.PERMANENT.type -> selectStateValue(binding.personalAddressLayout.spinnerPermanentState, pinCodeObj!!, AppEnums.ADDRESS_TYPE.PERMANENT)
                AppEnums.ADDRESS_TYPE.CURRENT.type -> selectStateValue(binding.personalAddressLayout.spinnerCurrentState, pinCodeObj!!, AppEnums.ADDRESS_TYPE.CURRENT)
            }
        } else {
            clearPinCodes(addressType?.type)
        }
    }

    private fun selectStateValue(spinner: Spinner, pinCodeDetail: Response.PinCodeObj, type: AppEnums.ADDRESS_TYPE) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as StatesMaster
            if (obj.stateID == pinCodeDetail.stateID) {
                spinner.setSelection(index + 1)
                mStateId = pinCodeDetail.stateID.toString()
                districtPresenter.callDistrictApi(addressType = type)
                spinner.isEnabled = false
                return
            }
        }
    }

    private fun clearPinCodes(addressType: String? = null) {
        when (addressType) {
            AppEnums.ADDRESS_TYPE.PERMANENT.type -> clearPermanentPinCodeField()
            AppEnums.ADDRESS_TYPE.CURRENT.type -> clearCurrentPinCodeField()
        }
    }

    override fun getDistrictFailure(msg: String) = showToast(msg)

    override fun getDistrictSuccess(value: Response.ResponseDistrict, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj != null && value.responseObj.size > 0) {
            when (addressType) {
                AppEnums.ADDRESS_TYPE.PERMANENT -> setUpDistrict(value, binding.personalAddressLayout.spinnerPermanentDistrict, pinCodeObj, AppEnums.ADDRESS_TYPE.PERMANENT)
                AppEnums.ADDRESS_TYPE.CURRENT -> setUpDistrict(value, binding.personalAddressLayout.spinnerCurrentDistrict, pinCodeObj, AppEnums.ADDRESS_TYPE.CURRENT)
                else -> clearCity(addressType)
            }
        }
    }

    private fun clearCity(addressType: AppEnums.ADDRESS_TYPE?) {
        when (addressType) {
            AppEnums.ADDRESS_TYPE.PERMANENT -> binding.personalAddressLayout.spinnerPermanentCity.adapter = CitySpinnerAdapter(mContext, ArrayList())
            AppEnums.ADDRESS_TYPE.CURRENT -> binding.personalAddressLayout.spinnerCurrentCity.adapter = CitySpinnerAdapter(mContext, ArrayList())
            else -> return
        }
    }

    private fun setUpDistrict(response: Response.ResponseDistrict, spinner: Spinner, pinCodeObj: Response.PinCodeObj?, type: AppEnums.ADDRESS_TYPE) {
        spinner.adapter = DistrictSpinnerAdapter(mContext, response.responseObj!!)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val district = parent.selectedItem as Response.DistrictObj
                    mDistrictId = district.districtID.toString()
                    cityPresenter.callCityApi(addressType = type)
                }
            }
        }
        selectDistrictValue(spinner, pinCodeObj!!, type)
    }

    private fun selectDistrictValue(spinner: Spinner, pinCodeObj: Response.PinCodeObj, type: AppEnums.ADDRESS_TYPE) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.DistrictObj
            if (obj.districtID == pinCodeObj.districtID) {
                spinner.setSelection(index + 1)
                mDistrictId = obj.districtID.toString()
                cityPresenter.callCityApi(addressType = type)
                spinner.isEnabled = false
                return
            }
        }
    }

    override fun getCityFailure(msg: String) = showToast(msg)

    override fun getCitySuccess(value: Response.ResponseCity, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj != null && value.responseObj.size > 0) {
            when (addressType) {
                AppEnums.ADDRESS_TYPE.PERMANENT -> setUpCity(binding.personalAddressLayout.spinnerPermanentCity, value)
                AppEnums.ADDRESS_TYPE.CURRENT -> setUpCity(binding.personalAddressLayout.spinnerCurrentCity, value)
                else -> return
            }
        }
    }

    private fun setUpCity(spinner: Spinner, response: Response.ResponseCity) {
        spinner.adapter = CitySpinnerAdapter(mContext, response.responseObj!!)
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.CityObj
            if (obj.cityID == pinCodeObj!!.cityID) {
                spinner.setSelection(index + 1)
                spinner.isEnabled = false
                return
            }
        }
    }

    override fun getLoanAppPostSuccess(value: Response.ResponseGetLoanApplication) {
        saveDataToDB(getPersonalInfoMaster())
        coApplicantsPresenter.callNetwork(ConstantsApi.CALL_COAPPLICANTS_LIST)
        AppEvents.fireEventLoanAppChangeNavFragmentNext()
    }

    override fun getLoanAppPostFailure(msg: String) {
        saveDataToDB(getPersonalInfoMaster())
        showToast(msg)
    }

    private fun getPersonalInfoMaster(): PersonalInfoMaster {
        pDraftData.applicantDetails = personalApplicantsList
        personalInfoMaster?.draftData = pDraftData
        personalInfoMaster!!.leadID = leadIdForApplicant.toInt()
        return personalInfoMaster!!
    }

    private fun setCoApplicants() {
        val applicantsList = sharedPreferences.getCoApplicantsList()
        if (applicantsList == null || applicantsList.size <= 0) {
            if (personalApplicantsList == null || personalApplicantsList!!.size <= 0) applicantTab?.add(getDefaultApplicant())
            else getTabListFromApplicantList()
        } else applicantTab = applicantsList
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapter = ApplicantsAdapter(context!!, applicantTab!!)
        binding.rcApplicants.adapter = applicantAdapter
        applicantAdapter!!.setOnItemClickListener(this)
        applicantAdapter!!.setOnLongClickListener(this)
    }

    private fun getTabListFromApplicantList() {
        for (index in 0 until personalApplicantsList!!.size) {
            val lNum = personalApplicantsList!![index].leadApplicantNumber
            val iConsider = personalApplicantsList!![index].incomeConsidered
            val mApplicant = personalApplicantsList!![index].isMainApplicant
            val fName = personalApplicantsList!![index].firstName
            val coApplicant = Response.CoApplicantsObj(leadApplicantNumber = lNum!!,
                    incomeConsidered = iConsider, isMainApplicant = mApplicant!!, firstName = fName)
            applicantTab!!.add(coApplicant)
        }
    }

    private fun onAddCoApplicantClick() {
        if (formValidation.validatePersonalInfo(binding)) {
            personalApplicantsList!!.add(PersonalApplicantsModel())
            applicantTab!!.add(getDefaultCoApplicant())
            applicantAdapter!!.notifyDataSetChanged()
            ClearPersonalForm(binding, mContext, allMasterDropDown, states, relationshipList)
            try {
                val lastIndex = applicantTab!!.lastIndex
                applicantAdapter?.onClickItem(lastIndex, applicantTab!![lastIndex])
                currentPosition = lastIndex
            } catch (e: Exception){e.printStackTrace()}
        } else showToast(getString(R.string.mandatory_field_missing))
    }

    override fun onApplicantClick(position: Int, coApplicant: Response.CoApplicantsObj) {
        if (formValidation.validatePersonalInfo(binding)) {
            saveCurrentApplicant()
            ClearPersonalForm(binding, mContext, allMasterDropDown, states, relationshipList)
            currentPosition = position
            waitFor1Sec(position, coApplicant)
        } else showToast(getString(R.string.mandatory_field_missing))
    }

    private fun saveCurrentApplicant() {
        if (personalApplicantsList!!.size > 0) {
            personalApplicantsList!![currentPosition] = getCurrentApplicant()
        } else personalApplicantsList!!.add(currentPosition, getCurrentApplicant())
    }

    private fun getDefaultApplicant(): Response.CoApplicantsObj {
        return Response.CoApplicantsObj(firstName = "Applicant", isMainApplicant = currentPosition == 0,
                leadApplicantNumber = leadAndLoanDetail.getLeadApplicantNum(currentPosition + 1))
    }

    private fun getDefaultCoApplicant(): Response.CoApplicantsObj {
        return Response.CoApplicantsObj(firstName = "CoApplicant ${currentPosition + 1}",
                isMainApplicant = currentPosition == 0,
                leadApplicantNumber = leadAndLoanDetail.getLeadApplicantNum(currentPosition + 1))
    }

    private fun waitFor1Sec(position: Int, coApplicant: Response.CoApplicantsObj) {
        val progress = ProgressDialog(mContext)
        progress.setMessage(getString(R.string.msg_saving))
        progress.setCancelable(false)
        progress.show()
        val handler = Handler()
        handler.postDelayed({
            getParticularApplicantData(position, coApplicant)
            progress.dismiss()
        }, 1000)
        applicantAdapter!!.notifyDataSetChanged()
    }

    private fun getParticularApplicantData(position: Int, coApplicant: Response.CoApplicantsObj) {
        currentApplicant = personalApplicantsList!![position]
        currentApplicant.isMainApplicant = coApplicant.isMainApplicant
        currentApplicant.leadApplicantNumber = coApplicant.leadApplicantNumber
        currentApplicant.firstName = coApplicant.firstName
        contactDetail = currentApplicant.contactDetail
        personalAddressDetail = currentApplicant.addressDetailList
        fillFormWithCurrentApplicant(currentApplicant)
    }

    override fun onApplicantLongClick(position: Int) = showAlertDialog(position)

    private fun showAlertDialog(position: Int) {
        if (mLead!!.status == COMPLETED || currentPosition == 0) {
            return
        } else {
            val deleteDialogView = LayoutInflater.from(mContext).inflate(R.layout.delete_dialog, null)
            val progressDialog = ProgressDialog(mContext)
            val mBuilder = AlertDialog.Builder(mContext)
                    .setView(deleteDialogView)
                    .setCancelable(false)
                    .setTitle("Delete Applicant")
            val deleteDialog = mBuilder.show()
            deleteDialogView.tvDeleteConfirm.setOnClickListener {
                progressDialog.setMessage("Deleting Applicant")
                progressDialog.show()
                Handler().postDelayed({
                    deleteApplicant(position)
                    deleteDialog.dismiss()
                    progressDialog.dismiss()
                }, 1000)
            }

            deleteDialogView.tvDonotDelete.setOnClickListener {
                deleteDialog.dismiss()
            }
        }
    }

    private fun deleteApplicant(deletePosition: Int) {
        applicantTab!!.removeAt(deletePosition)
        applicantAdapter!!.notifyItemRemoved(deletePosition)
        applicantAdapter!!.notifyItemRangeChanged(deletePosition, applicantTab!!.size)
        binding.rcApplicants.adapter!!.notifyDataSetChanged()

        try {
            val currentApplicantPosition = when(personalApplicantsList!!.size > deletePosition) {
                true -> deletePosition
                else -> personalApplicantsList!!.size
            }
            currentApplicant = personalApplicantsList!![currentApplicantPosition - 1]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        fillFormWithCurrentApplicant(currentApplicant)
    }

    override fun getCoApplicantsListSuccess(value: Response.ResponseCoApplicants) {}

    override fun getCoApplicantsListFailure(msg: String) = showToast(msg)

    private fun showKycDetail() {}

    private fun clearKycData() {
        binding.spinnerIdentificationType.isSelected = false
        binding.etIdNum.text?.clear()
        binding.etIssueDate.text?.clear()
        binding.etExpiryDate.text?.clear()
    }

    private fun checkKycDataList() {
        if (kycList.isNullOrEmpty()) {
            binding.rcKYC.visibility = View.GONE
        }
    }

    private val kyc: AddKyc
        get() {
            return AddKyc(expiryDate = binding.etExpiryDate.text.toString(),
                    idNum = binding.etIdNum.text.toString(), kycImage = image,
                    issueDate = binding.etIssueDate.text.toString(),
                    verifiedStatus = binding.spinnerVerifiedStatus.selectedItem.toString(),
                    idType = binding.spinnerIdentificationType.selectedItem.toString())
        }

    private fun getKycData() {
        kycList.add(kyc)
        showKycDetail()
    }

    private val otpSendRequest: Requests.RequestSendOTP
        get() {
            val leadId = mLead!!.leadID!!.toInt()
            val mobile = binding.basicInfoLayout.etMobile.text.toString()
            return Requests.RequestSendOTP(leadID = leadId, mobile = mobile)
        }

    override val sendOTPRequest: Requests.RequestSendOTP
        get() = otpSendRequest

    override fun getSendOTPFailure(msg: String?) {
        msg?.let {
            showToast(msg)
        }
    }

    override fun getSendOTPSuccess(value: Response.ResponseSendOTP) {
        value?.responseMsg?.let {
            showToast(value.responseMsg!!)
        }
    }

    private var verifyOTPDialogView: View? = null
    private fun showVerifyOTPDialog() {
        verifyOTPDialogView = LayoutInflater.from(mContext).inflate(R.layout.pop_up_verify_otp, null)
        val mBuilder = AlertDialog.Builder(mContext)
                .setView(verifyOTPDialogView)
                .setTitle("Verify OTP")
                .setCancelable(false)

        verifyOTPDialog = mBuilder.show()
        val pinEntry = verifyOTPDialogView?.etOTP
        verifyOTPDialogView?.btnVerify?.setOnClickListener {
            pinEntry?.setOnPinEnteredListener { str ->
                if (str.toString().length == 4) {
                    otp = str.toString().toInt()
                    verifyOTPPresenter.callNetwork(ConstantsApi.CALL_VERIFY_OTP)
                }
            }
        }
        verifyOTPDialogView?.tvResendOTP?.setOnClickListener {
            handleResendOtpEvent()
        }

        verifyOTPDialogView?.ivCross?.setOnClickListener {
            dismissOtpVerificationDialog()
        }

        verifyOTPDialogView?.tvResendOTP?.callOnClick()

        timerOtpResend.start()
    }

    private fun handleResendOtpEvent() {
        verifyOTPDialogView?.tvResendOTP?.exGone()
        verifyOTPDialogView?.tvResendOTPTimeLeftInfo?.exVisible()
        timerOtpResend.start()
        sendOTPPresenter.callNetwork(ConstantsApi.CALL_SEND_OTP)
    }

    private fun handleOtpResnedTimerEndEvent() {
        verifyOTPDialogView?.tvResendOTP?.exVisible()
        verifyOTPDialogView?.tvResendOTPTimeLeftInfo?.exGone()
        timerOtpResend.cancel()
    }

    private fun dismissOtpVerificationDialog() {
        timerOtpResend.cancel()
        verifyOTPDialog.dismiss()
    }

    private val minutes = 1L
    private val seconds = 60L
    private val millisecond = 1000L
    private val timerOtpResend = object: CountDownTimer(minutes * seconds * millisecond, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val secondsUntilFinish = (millisUntilFinished / millisecond).toInt()
            verifyOTPDialogView?.tvResendOTPTimeLeftInfo?.text = "$secondsUntilFinish seconds"
        }

        override fun onFinish() {
            handleOtpResnedTimerEndEvent()
        }
    }

    private val otpVerifyRequest: Requests.RequestVerifyOTP
        get() {
            val leadId = mLead!!.leadID!!.toInt()
            val mobile = binding.basicInfoLayout.etMobile.text.toString()
            return Requests.RequestVerifyOTP(leadID = leadId, mobile = mobile, otpValue = otp!!)
        }

    override val verifyOTPRequest: Requests.RequestVerifyOTP
        get() = otpVerifyRequest

    override fun getVerifyOTPFailure(msg: String?) {
        msg?.let {
            showToast(msg)
        }
    }

    override fun getVerifyOTPSuccess(value: Response.ResponseVerifyOTP) {
        dismissOtpVerificationDialog()

        binding.basicInfoLayout.btnGetOTP.visibility = View.GONE
        binding.basicInfoLayout.ivVerifiedStatus.visibility = View.VISIBLE
    }
}
