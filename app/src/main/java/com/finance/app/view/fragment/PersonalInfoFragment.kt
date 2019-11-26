package com.finance.app.view.fragment

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentPersonalBinding
import com.finance.app.model.Modals.AddKyc
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.DistrictCityConnector
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.connector.PinCodeDetailConnector
import com.finance.app.presenter.presenter.*
import com.finance.app.utility.*
import com.finance.app.view.adapters.recycler.Spinner.CitySpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.DistrictSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.StatesSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
import com.google.android.material.textfield.TextInputEditText
import fr.ganfra.materialspinner.MaterialSpinner
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class PersonalInfoFragment : BaseFragment(), LoanApplicationConnector.PostLoanApp,
        LoanApplicationConnector.GetLoanApp, PinCodeDetailConnector.PinCode,
        ApplicantsAdapter.ItemClickListener, DistrictCityConnector.District,
        DistrictCityConnector.City {

    private lateinit var binding: FragmentPersonalBinding
    private lateinit var mContext: Context
    private var mLead: AllLeadMaster? = null
    private val frag = this
    private val loanAppPostPresenter = LoanAppPostPresenter(this)
    private val loanAppGetPresenter = LoanAppGetPresenter(this)
    private val pinCodePresenter = PinCodeDetailPresenter(this)
    private val districtPresenter = DistrictPresenter(this)
    private val cityPresenter = CityPresenter(this)
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
    private lateinit var allMasterDropDown: AllMasterDropDown

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil

    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val SINGLE_TYPE_DETAIL_ID = 63
        private const val PERMANENT = "Permanent"
        private const val CURRENT = "Current"
        private lateinit var kycList: ArrayList<AddKyc>
        private lateinit var states: List<StatesMaster>
        private val leadAndLoanDetail = LeadAndLoanDetail()
        private val responseConversion = ResponseConversion()
        private val requestConversion = RequestConversion()
        private lateinit var applicantTab: ArrayList<String>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_personal)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        kycList = ArrayList()
        mContext = context!!
        getPersonalInfo()
        SetPersonalMandatoryField(binding)
        setDatePicker()
        checkKycDataList()
        setClickListeners()
    }

    private fun getPersonalInfo() {
        mLead = sharedPreferences.getLeadDetail()
        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
    }

    override val storageType: String
        get() = personalInfoMaster?.storageType!!

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        value.responseObj?.let {
            personalInfoMaster = responseConversion.toPersonalMaster(value.responseObj)
            pDraftData = personalInfoMaster?.draftData!!
            personalApplicantsList = pDraftData.applicantDetails
        }
        setCoApplicants(personalApplicantsList)
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
                setMasterDropDownValue(allMasterDropDown)
            }
        })
        dataBase.provideDataBaseSource().statesDao().getAllStates().observe(viewLifecycleOwner, Observer {
            states = it
            setStateDropDownValue(states)
        })
    }

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().personalInfoDao().getPersonalInfo(leadId).observe(this, Observer { personalMaster ->
            personalMaster?.let {
                personalInfoMaster = personalMaster
                pDraftData = personalInfoMaster?.draftData!!
                personalApplicantsList = pDraftData.applicantDetails
                if (personalApplicantsList!!.size < 0) {
                    personalApplicantsList!!.add(PersonalApplicantsModel())
                }
            }
            setCoApplicants(personalApplicantsList)
            showData(personalApplicantsList)
        })
    }

    private fun setCoApplicants(applicants: ArrayList<PersonalApplicantsModel>?) {
        applicantTab = ArrayList()
        applicantTab.add("Applicant")
        if (applicants != null && applicants.size > 1) {
            for (position in 1 until applicants.size) {
                applicantTab.add("CoApplicant $position")
            }
        }
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapter = ApplicantsAdapter(context!!, applicantTab)
        applicantAdapter!!.setOnItemClickListener(this)
        binding.rcApplicants.adapter = applicantAdapter
    }

    private fun onAddCoApplicantClick() {
        if (formValidation.validatePersonalInfo(binding)) {
            applicantTab.add("CoApplicant ${applicantTab.size}")
            binding.rcApplicants.adapter!!.notifyDataSetChanged()
            personalApplicantsList!!.add(PersonalApplicantsModel())
        } else {
            showToast(getString(R.string.mandatory_field_missing))
        }
    }

    override fun onApplicantClick(position: Int) {
        if (formValidation.validatePersonalInfo(binding)) {
            saveCurrentApplicant()
            ClearPersonalForm(binding, mContext, allMasterDropDown, states)
            currentPosition = position
            waitFor1Sec(position)
        } else showToast(getString(R.string.mandatory_field_missing))
    }

    private fun waitFor1Sec(position: Int) {
        val progress = ProgressDialog(mContext)
        progress.setMessage(getString(R.string.msg_saving))
        progress.setCancelable(false)
        progress.show()
        val handler = Handler()
        handler.postDelayed({
            getParticularApplicantData(position)
            progress.dismiss()
        }, 1000)
        applicantAdapter!!.notifyDataSetChanged()
    }

    private fun getParticularApplicantData(position: Int) {
        currentApplicant = if (position >= personalApplicantsList!!.size) {
            PersonalApplicantsModel()
        } else {
            personalApplicantsList!![position]
        }
        contactDetail = currentApplicant.contactDetail
        personalAddressDetail = currentApplicant.addressDetailList
        fillFormWithCurrentApplicant(currentApplicant)
    }

    private fun showData(applicantList: ArrayList<PersonalApplicantsModel>?) {
        if (applicantList != null) {
            if (applicantList.size <= 0) {
                fillFormWithLeadDetail()
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
        binding.basicInfoLayout.etDOB.setText(currentApplicant.dateOfBirth!!)
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
        fillValueInMasterDropDown()
        if (personalAddressDetail != null && personalAddressDetail!!.size > 0) {
            fillAddressInfo(personalAddressDetail!!)
        } else {
            binding.personalAddressLayout.etCurrentAddress.setText(mLead!!.applicantAddress)
            binding.personalAddressLayout.etPermanentAddress.setText(mLead!!.applicantAddress)
        }
        if (currentApplicant.maritialStatusTypeDetailID != SINGLE_TYPE_DETAIL_ID) {
            binding.basicInfoLayout.etSpouseMiddleName.setText(currentApplicant.spouseMiddleName)
            binding.basicInfoLayout.etSpouseFirstName.setText(currentApplicant.spouseFirstName)
            binding.basicInfoLayout.etSpouseLastName.setText(currentApplicant.spouseLastName)
        }
    }

    private fun setMasterDropDownValue(dropDown: AllMasterDropDown) {
        binding.spinnerIdentificationType.adapter = MasterSpinnerAdapter(mContext, dropDown.IdentificationType!!)
        binding.spinnerVerifiedStatus.adapter = MasterSpinnerAdapter(mContext, dropDown.VerifiedStatus!!)
        binding.basicInfoLayout.spinnerGender.adapter = MasterSpinnerAdapter(mContext, dropDown.Gender!!)
        binding.basicInfoLayout.spinnerNationality.adapter = MasterSpinnerAdapter(mContext, dropDown.Nationality!!)
        binding.basicInfoLayout.spinnerReligion.adapter = MasterSpinnerAdapter(mContext, dropDown.Religion!!)
        binding.basicInfoLayout.spinnerDobProof.adapter = MasterSpinnerAdapter(mContext, dropDown.DOBProof!!)
        binding.basicInfoLayout.spinnerCaste.adapter = MasterSpinnerAdapter(mContext, dropDown.Caste!!)
        binding.basicInfoLayout.spinnerQualification.adapter = MasterSpinnerAdapter(mContext, dropDown.Qualification!!)
        binding.basicInfoLayout.spinnerDetailQualification.adapter = MasterSpinnerAdapter(mContext, dropDown.DetailQualification!!)
        binding.basicInfoLayout.spinnerRelationship.adapter = MasterSpinnerAdapter(mContext, dropDown.Relationship!!)
        binding.personalAddressLayout.spinnerCurrentAddressProof.adapter = MasterSpinnerAdapter(mContext, dropDown.AddressProof!!)
        binding.personalAddressLayout.spinnerPermanentAddressProof.adapter = MasterSpinnerAdapter(mContext, dropDown.AddressProof!!)
        binding.personalAddressLayout.spinnerCurrentResidenceType.adapter = MasterSpinnerAdapter(mContext, dropDown.ResidenceType!!)
        binding.personalAddressLayout.spinnerPermanentResidenceType.adapter = MasterSpinnerAdapter(mContext, dropDown.ResidenceType!!)
        binding.basicInfoLayout.spinnerLivingStandard.adapter = MasterSpinnerAdapter(mContext, dropDown.LivingStandardIndicators!!)
        setMaritalStatus(dropDown)
        fillValueInMasterDropDown()
        if (personalAddressDetail != null && personalAddressDetail!!.size > 0) {
            fillAddressInfo(personalAddressDetail!!)
        }
    }

    private fun setStateDropDownValue(states: List<StatesMaster>) {
        binding.personalAddressLayout.spinnerCurrentState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.personalAddressLayout.spinnerCurrentState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val state = parent.selectedItem as StatesMaster
                    mStateId = state.stateID.toString()
                    districtPresenter.callDistrictApi(addressType = AppEnums.ADDRESS_TYPE.CURRENT)
                }
            }
        }

        binding.personalAddressLayout.spinnerPermanentState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.personalAddressLayout.spinnerPermanentState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val state = parent.selectedItem as StatesMaster
                    mStateId = state.stateID.toString()
                    districtPresenter.callDistrictApi(addressType = AppEnums.ADDRESS_TYPE.PERMANENT)
                }
            }
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
        selectRelationshipValue(binding.basicInfoLayout.spinnerRelationship)
        selectLivingStandardValue(binding.basicInfoLayout.spinnerLivingStandard)
        selectRelationshipValue(binding.personalAddressLayout.spinnerPermanentAddressProof)
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

    private fun selectCurrentDistrictValue(spinner: Spinner, pinCodeObj: Response.PinCodeObj) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.DistrictObj
            if (obj.districtID == pinCodeObj.districtID) {
                spinner.setSelection(index + 1)
                mDistrictId = obj.districtID.toString()
                cityPresenter.callCityApi(addressType = AppEnums.ADDRESS_TYPE.CURRENT)
                spinner.isEnabled = false
                return
            }
        }
    }

    private fun selectCurrentCityValue(spinner: Spinner, cityId: Int?) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.CityObj
            if (obj.cityID == cityId) {
                spinner.setSelection(index + 1)
                spinner.isEnabled = false
                return
            }
        }
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

    private fun selectPermanentDistrictValue(spinner: Spinner, pinCodeObj: Response.PinCodeObj) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.DistrictObj
            if (obj.districtID == pinCodeObj.districtID) {
                spinner.setSelection(index + 1)
                mDistrictId = obj.districtID.toString()
                cityPresenter.callCityApi(addressType = AppEnums.ADDRESS_TYPE.PERMANENT)
                spinner.isEnabled = false
                return
            }
        }
    }

    private fun selectPermanentCityValue(spinner: Spinner, cityId: Int?) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.CityObj
            if (obj.cityID == cityId) {
                spinner.setSelection(index + 1)
                spinner.isEnabled = false
                return
            }
        }
    }

    private fun setMaritalStatus(dropDown: AllMasterDropDown) {
        binding.basicInfoLayout.spinnerMaritalStatus.adapter = MasterSpinnerAdapter(mContext, dropDown.MaritalStatus!!)
        binding.basicInfoLayout.spinnerMaritalStatus?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    maritalStatus = parent.selectedItem as DropdownMaster
                    if (maritalStatus!!.typeDetailID == SINGLE_TYPE_DETAIL_ID) {
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

    private fun selectRelationshipValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.relationshipTypeDetailId) {
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

    private fun saveCurrentApplicant() {
        if (personalApplicantsList!!.size > 0) {
            personalApplicantsList!![currentPosition] = getCurrentApplicant()
        } else personalApplicantsList!!.add(currentPosition, getCurrentApplicant())
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

        currentApplicant.numberOfEarningMembers = binding.basicInfoLayout.etNumOfEarningMember.text.toString().toInt()
        currentApplicant.numberOfDependents = binding.basicInfoLayout.etNumOfDependent.text.toString().toInt()
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
        currentApplicant.firstName = binding.basicInfoLayout.etFirstName.text.toString()
        currentApplicant.middleName = binding.basicInfoLayout.etMiddleName.text.toString()
        currentApplicant.lastName = binding.basicInfoLayout.etLastName.text.toString()
        currentApplicant.spouseFirstName = binding.basicInfoLayout.etSpouseFirstName.text.toString()
        currentApplicant.spouseMiddleName = binding.basicInfoLayout.etSpouseMiddleName.text.toString()
        currentApplicant.spouseLastName = binding.basicInfoLayout.etSpouseLastName .text.toString()
        currentApplicant.fatherFirstName = binding.basicInfoLayout.etFatherFirstName.text.toString()
        currentApplicant.fatherMiddleName = binding.basicInfoLayout.etFatherMiddleName.text.toString()
        currentApplicant.fatherLastName = binding.basicInfoLayout.etFatherLastName.text.toString()
        currentApplicant.dateOfBirth = binding.basicInfoLayout.etDOB.text.toString()
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

        cAddressDetail.rentAmount = binding.personalAddressLayout.etCurrentRentAmount.text.toString().toInt()
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

            pAddressDetail.rentAmount = binding.personalAddressLayout.etPermanentRentAmount.text.toString().toInt()
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

    private fun setClickListeners() {
        binding.ivUploadKyc.setOnClickListener {
            UploadData(frag, mContext)
        }
        binding.basicInfoLayout.btnVerifyOTP.setOnClickListener {
        }
        binding.btnAddKYC.setOnClickListener {
            getKycData()
            clearKycData()
        }
        binding.basicInfoLayout.ivUploadDobProof.setOnClickListener { UploadData(frag, mContext) }
        binding.btnAddApplicant.setOnClickListener { onAddCoApplicantClick() }
        binding.btnSaveAndContinue.setOnClickListener {
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
    }

    private fun pinCodeListener(pinCodeField: TextInputEditText?, addressType: AppEnums.ADDRESS_TYPE? = null) {
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

    override val loanAppRequestPost: LoanApplicationRequest
        get() = requestConversion.personalInfoRequest(getPersonalInfoMaster())

    override fun getLoanAppPostSuccess(value: Response.ResponseGetLoanApplication) {
        saveDataToDB(getPersonalInfoMaster())
        gotoNextFragment()
    }

    override fun getLoanAppPostFailure(msg: String) {
        saveDataToDB(getPersonalInfoMaster())
        showToast(msg)
    }

    private fun getPersonalInfoMaster(): PersonalInfoMaster {
        pDraftData.applicantDetails = personalApplicantsList
        personalInfoMaster?.draftData = pDraftData
        personalInfoMaster!!.leadID = leadId.toInt()
        return personalInfoMaster!!
    }

    override val pinCode: String
        get() = mPinCode

    override fun getPinCodeSuccess(value: Response.ResponsePinCodeDetail, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj!!.size > 0) {
            pinCodeObj = value.responseObj[0]
            when (addressType?.type) {
                AppEnums.ADDRESS_TYPE.PERMANENT.type -> setPermanentPinCodeDetails(pinCodeObj!!)
                AppEnums.ADDRESS_TYPE.CURRENT.type -> setCurrentPinCodeDetails(pinCodeObj!!)
            }
        } else {
            clearPinCodes(addressType?.type)
        }
    }

    override fun getPinCodeFailure(msg: String) = clearPinCodes()

    private fun clearPinCodes(addressType: String? = null) {
        when (addressType) {
            AppEnums.ADDRESS_TYPE.PERMANENT.type -> clearPermanentPinCodeField()
            AppEnums.ADDRESS_TYPE.CURRENT.type -> clearCurrentPinCodeField()
        }
    }

    private fun clearPermanentPinCodeField() {
        binding.personalAddressLayout.spinnerPermanentState.isEnabled = true
        binding.personalAddressLayout.spinnerPermanentDistrict.isEnabled = true
        binding.personalAddressLayout.spinnerPermanentCity.isEnabled = true
        binding.personalAddressLayout.spinnerPermanentState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.personalAddressLayout.spinnerPermanentDistrict.adapter = DistrictSpinnerAdapter(mContext, ArrayList())
        binding.personalAddressLayout.spinnerPermanentCity.adapter = CitySpinnerAdapter(mContext,ArrayList())
    }

    private fun clearCurrentPinCodeField() {
        binding.personalAddressLayout.spinnerCurrentDistrict.isEnabled = true
        binding.personalAddressLayout.spinnerCurrentCity.isEnabled = true
        binding.personalAddressLayout.spinnerCurrentState.isEnabled = true
        binding.personalAddressLayout.spinnerCurrentState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.personalAddressLayout.spinnerCurrentDistrict.adapter = DistrictSpinnerAdapter(mContext, ArrayList())
        binding.personalAddressLayout.spinnerCurrentCity.adapter = CitySpinnerAdapter(mContext,ArrayList())
    }

    private fun setCurrentPinCodeDetails(pinCodeDetail: Response.PinCodeObj) {
        selectCurrentStateValueFromPin(binding.personalAddressLayout.spinnerCurrentState, pinCodeDetail)
    }

    private fun setPermanentPinCodeDetails(pinCodeDetail: Response.PinCodeObj) {
        selectPermanentStateValueFromPin(binding.personalAddressLayout.spinnerPermanentState, pinCodeDetail)
    }

    override val stateId: String
        get() = mStateId

    override fun getDistrictSuccess(value: Response.ResponseDistrict, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj != null && value.responseObj.size > 0) {
            when (addressType) {
                AppEnums.ADDRESS_TYPE.PERMANENT -> setPermanentDistricts(value, pinCodeObj)
                AppEnums.ADDRESS_TYPE.CURRENT -> setCurrentDistricts(value, pinCodeObj)
            }
        }
    }

    private fun setCurrentDistricts(response: Response.ResponseDistrict, pinCodeObj: Response.PinCodeObj?) {
        binding.personalAddressLayout.spinnerCurrentDistrict.adapter = DistrictSpinnerAdapter(mContext, response.responseObj!!)
        binding.personalAddressLayout.spinnerCurrentDistrict?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val district = parent.selectedItem as Response.DistrictObj
                    mDistrictId = district.districtID.toString()
                    cityPresenter.callCityApi(addressType = AppEnums.ADDRESS_TYPE.CURRENT)
                }
            }
        }
        selectCurrentDistrictValue(binding.personalAddressLayout.spinnerCurrentDistrict, pinCodeObj!!)
    }

    private fun setPermanentDistricts(response: Response.ResponseDistrict, pinCodeObj: Response.PinCodeObj?) {
        binding.personalAddressLayout.spinnerPermanentDistrict.adapter = DistrictSpinnerAdapter(mContext, response.responseObj!!)
        binding.personalAddressLayout.spinnerPermanentDistrict?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val district = parent.selectedItem as Response.DistrictObj
                    mDistrictId = district.districtID.toString()
                    cityPresenter.callCityApi(addressType = AppEnums.ADDRESS_TYPE.PERMANENT)
                }
            }
        }
        selectPermanentDistrictValue(binding.personalAddressLayout.spinnerPermanentDistrict, pinCodeObj!!)
    }

    override fun getDistrictFailure(msg: String) = showToast(msg)

    override val districtId: String
        get() = mDistrictId

    override fun getCitySuccess(value: Response.ResponseCity, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj != null && value.responseObj.size > 0) {
            when (addressType) {
                AppEnums.ADDRESS_TYPE.PERMANENT -> setPermanentCity(value)
                AppEnums.ADDRESS_TYPE.CURRENT -> setCurrentCity(value)
            }
        }
    }

    private fun setCurrentCity(response: Response.ResponseCity) {
        binding.personalAddressLayout.spinnerCurrentCity.adapter = CitySpinnerAdapter(mContext, response.responseObj!!)
        selectCurrentCityValue(binding.personalAddressLayout.spinnerCurrentCity, pinCodeObj!!.cityID)
    }

    private fun setPermanentCity(response: Response.ResponseCity) {
        binding.personalAddressLayout.spinnerPermanentCity.adapter = CitySpinnerAdapter(mContext, response.responseObj!!)
        selectPermanentCityValue(binding.personalAddressLayout.spinnerPermanentCity, pinCodeObj!!.cityID)
    }

    override fun getCityFailure(msg: String) = showToast(msg)

    private fun selectCurrentStateValueFromPin(spinner: MaterialSpinner, pinCodeDetail: Response.PinCodeObj) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as StatesMaster
            if (obj.stateID == pinCodeDetail.stateID) {
                spinner.setSelection(index + 1)
                mStateId = pinCodeDetail.stateID.toString()
                districtPresenter.callDistrictApi(addressType = AppEnums.ADDRESS_TYPE.CURRENT)
                spinner.isEnabled = false
                return
            }
        }
    }

    private fun selectPermanentStateValueFromPin(spinner: MaterialSpinner, pinCodeDetail: Response.PinCodeObj) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as StatesMaster
            if (obj.stateID == pinCodeDetail.stateID) {
                spinner.setSelection(index + 1)
                mStateId = pinCodeDetail.stateID.toString()
                districtPresenter.callDistrictApi(addressType = AppEnums.ADDRESS_TYPE.PERMANENT)
                spinner.isEnabled = false
                return
            }
        }
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, EmploymentInfoFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }

/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                        image = bitmap
                        binding.ivUploadKyc.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (requestCode == CAMERA) {
                val bitmap = data!!.extras!!.get("data") as Bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)
                val byteArray = stream.toByteArray()
                val thumbnail = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.size)
                binding.ivUploadKyc.setImageBitmap(thumbnail)
                image = thumbnail
                Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
            }
            binding.rcKYC.adapter!!.notifyDataSetChanged()
        }
    }
*/

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

    private fun showKycDetail() {
        binding.rcKYC.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        kycAdapter = AddKycAdapter(activity!!, kycList)
//        binding.rcKYC.adapter = kycAdapter
        binding.rcKYC.visibility = View.VISIBLE
    }
}
