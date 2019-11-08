package com.finance.app.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentPersonalBinding
import com.finance.app.model.Modals.AddKyc
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.connector.PinCodeDetailConnector
import com.finance.app.presenter.presenter.PersonalInfoGetPresenter
import com.finance.app.presenter.presenter.PersonalInfoPostPresenter
import com.finance.app.presenter.presenter.PinCodeDetailPresenter
import com.finance.app.utility.*
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.StatesSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.PersonalApplicantsAdapter
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
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

class PersonalInfoFragment : BaseFragment(), LoanApplicationConnector.PostPersonalInfo,
        LoanApplicationConnector.GetPersonalInfo, PinCodeDetailConnector.PinCode,
        PersonalApplicantsAdapter.ItemClickListener {

    private lateinit var binding: FragmentPersonalBinding
    private lateinit var mContext: Context
    private var mLeadId: String? = null
    private var empId: String? = null
    private val frag = this
    private val personalPostPresenter = PersonalInfoPostPresenter(this)
    private val personalGetPresenter = PersonalInfoGetPresenter(this)
    private val pinCodePresenter = PinCodeDetailPresenter(this)
    private var personalApplicantAdapter: PersonalApplicantsAdapter? = null
    private var personalApplicantsList: ArrayList<PersonalApplicantsModel>? = ArrayList()
    private var personalInfoMaster: PersonalInfoMaster? = PersonalInfoMaster()
    private var currentApplicant: PersonalApplicantsModel = PersonalApplicantsModel()
    private var contactDetail: ContactDetail? = ContactDetail()
    private var image: Bitmap? = null
    private var maritalStatus: DropdownMaster? = null
    private var mPinCode: String = ""
    private var addressType: String = ""
    private var personalAddressDetail: ArrayList<AddressDetail>? = ArrayList()
    private var currentPosition = 0
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
        private lateinit var allMasterDropDown: AllMasterDropDown
        private lateinit var states: List<StatesMaster>
        private val leadAndLoanDetail = LeadAndLoanDetail()
        private val convertDate = ConvertDate()
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
        setDatePicker()
        checkKycDataList()
        setClickListeners()
    }

    override val leadId: String
        get() = mLeadId!!

    private fun getPersonalInfo() {
        mLeadId = sharedPreferences.getLeadId()
        empId = sharedPreferences.getUserId()
        personalGetPresenter.callNetwork(ConstantsApi.CALL_GET_PERSONAL_INFO)
    }

    override fun getPersonalGetInfoSuccess(value: Response.ResponseGetPersonalInfo) {
        value.responseObj?.let {
            personalInfoMaster = value.responseObj
            personalApplicantsList = personalInfoMaster?.applicantDetails!!
            saveDataToDB(value.responseObj)
        }
        setCoApplicants(personalApplicantsList)
        showData(personalApplicantsList)
    }

    override fun getPersonalGetInfoFailure(msg: String) = getDataFromDB()

    private fun setCoApplicants(personalApplicantsList: ArrayList<PersonalApplicantsModel>?) {
        applicantTab = ArrayList()
        applicantTab.add("Applicant")
        if (personalApplicantsList != null && personalApplicantsList.size > 1) {
            for (position in 1 until personalApplicantsList.size) {
                applicantTab.add("CoApplicant $position")
            }
        }
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        personalApplicantAdapter = PersonalApplicantsAdapter(context!!, applicantTab)
        personalApplicantAdapter!!.setOnItemClickListener(this)
        binding.rcApplicants.adapter = personalApplicantAdapter
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
            currentPosition = position
            ClearPersonalForm(binding, mContext, allMasterDropDown, states)
            waitFor2Sec(position)
        } else showToast(getString(R.string.mandatory_field_missing))
    }

    private fun waitFor2Sec(position: Int) {
        val progress = ProgressDialog(mContext)
        progress.setMessage(getString(R.string.msg_saving))
        progress.setCancelable(false)
        progress.show()
        val handler = Handler()
        handler.postDelayed({
            getParticularApplicantData(position)
            progress.dismiss()
        }, 2000)
        personalApplicantAdapter!!.notifyDataSetChanged()
    }

    private fun getParticularApplicantData(position: Int) {
        currentApplicant = personalApplicantsList!![position]
        contactDetail = currentApplicant.contactDetail
        personalAddressDetail = currentApplicant.addressDetailList
        fillFormWithCurrentApplicant(currentApplicant)
    }

    private fun showData(applicantList: ArrayList<PersonalApplicantsModel>?) {
        if (applicantList != null) {
            for (applicant in applicantList) {
                if (applicant.isMainApplicant) {
                    currentApplicant = applicant
                    contactDetail = currentApplicant.contactDetail
                    fillFormWithCurrentApplicant(currentApplicant)
                }
            }
        }
        getDropDownsFromDB()
    }

    private fun fillFormWithCurrentApplicant(currentApplicant: PersonalApplicantsModel) {
        binding.basicInfoLayout.etDOB.setText(convertDate.convertToAppFormat(currentApplicant.dateOfBirth!!))
        binding.basicInfoLayout.etFatherLastName.setText(currentApplicant.fatherLastName)
        binding.basicInfoLayout.etFatherMiddleName.setText(currentApplicant.fatherMiddleName)
        binding.basicInfoLayout.etFatherFirstName.setText(currentApplicant.fatherFirstName)
        binding.basicInfoLayout.etSpouseMiddleName.setText(currentApplicant.spouseMiddleName)
        binding.basicInfoLayout.etSpouseFirstName.setText(currentApplicant.spouseFirstName)
        binding.basicInfoLayout.etSpouseLastName.setText(currentApplicant.spouseLastName)
        binding.basicInfoLayout.etFirstName.setText(currentApplicant.firstName)
        binding.basicInfoLayout.etMiddleName.setText(currentApplicant.middleName)
        binding.basicInfoLayout.etNumOfDependent.setText(currentApplicant.numberOfDependents.toString())
        binding.basicInfoLayout.etNumOfEarningMember.setText(currentApplicant.numberOfEarningMembers.toString())
        binding.basicInfoLayout.etLastName.setText(currentApplicant.lastName)
        binding.basicInfoLayout.etAge.setText(currentApplicant.age.toString())
        binding.basicInfoLayout.etAlternateNum.setText(currentApplicant.alternateContact.toString())
        if (contactDetail != null) {
            binding.basicInfoLayout.etEmail.setText(contactDetail!!.email)
            binding.basicInfoLayout.etMobile.setText(contactDetail!!.mobile)
        }
        currentApplicant.addressDetailList?.let {
            fillAddressInfo(currentApplicant.addressDetailList!!)
        }
    }

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
        if (personalApplicantsList != null && personalApplicantsList!!.size > 0) {
            fillValueInMasterDropDown()
        }
    }

    private fun setStateDropDownValue(states: List<StatesMaster>) {
        binding.personalAddressLayout.spinnerCurrentState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.personalAddressLayout.spinnerPermanentState.adapter = StatesSpinnerAdapter(mContext, states)
//        if (personalApplicantsList != null && personalApplicantsList!!.size > 0) {
//            fillValueInStateDropDown()
//        }
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
        selectCurrentAddressProofValue(binding.personalAddressLayout.spinnerCurrentAddressProof)
        selectCurrentResidenceTypeValue(binding.personalAddressLayout.spinnerCurrentResidenceType)
        selectLivingStandardValue(binding.basicInfoLayout.spinnerLivingStandard)
        selectPermanentResidenceTypeValue(binding.personalAddressLayout.spinnerPermanentResidenceType)
        selectPermanentAddressProofValue(binding.personalAddressLayout.spinnerPermanentAddressProof)
        selectRelationshipValue(binding.personalAddressLayout.spinnerPermanentAddressProof)
    }

    private fun fillAddressInfo(addressDetailList: ArrayList<AddressDetail>) {
        fillCurrentAddressInfo(addressDetailList[0])
        fillPermanentAddressInfo(addressDetailList[1])
    }

    private fun fillCurrentAddressInfo(addressDetail: AddressDetail) {
        binding.personalAddressLayout.etCurrentAddress.setText(addressDetail.address1)
        binding.personalAddressLayout.etCurrentCity.setText(addressDetail.cityName)
        binding.personalAddressLayout.etCurrentPinCode.setText(addressDetail.zip)
        binding.personalAddressLayout.etCurrentLandmark.setText(addressDetail.landmark)
        binding.personalAddressLayout.etCurrentRentAmount.setText(addressDetail.rentAmount.toString())
        binding.personalAddressLayout.etCurrentStaying.setText(addressDetail.stayingInYears.toString())
    }

    private fun fillPermanentAddressInfo(addressDetail: AddressDetail) {
        binding.personalAddressLayout.etPermanentAddress.setText(addressDetail.address1)
        binding.personalAddressLayout.etPermanentCity.setText(addressDetail.cityName)
        binding.personalAddressLayout.etPermanentPinCode.setText(addressDetail.zip)
        binding.personalAddressLayout.etPermanentLandmark.setText(addressDetail.landmark)
        binding.personalAddressLayout.etPermanentRentAmount.setText(addressDetail.rentAmount.toString())
        binding.personalAddressLayout.etPermanentStaying.setText(addressDetail.stayingInYears.toString())
    }

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().personalInfoDao().getPersonalInfo(mLeadId!!).observe(this, Observer { personalMaster ->
            personalMaster?.let {
                personalInfoMaster = personalMaster
                personalApplicantsList = personalInfoMaster?.applicantDetails!!
            }
            setCoApplicants(personalApplicantsList)
            showData(personalApplicantsList)
        })
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
            if (obj.typeDetailID == currentApplicant.genderTypeDetailID) {
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

    private fun selectCurrentAddressProofValue(spinner: Spinner) {
        val addressDetail = currentApplicant.addressDetailList
        if (addressDetail != null) {
            for (address in addressDetail)
                if (address.addressTypeDetail == CURRENT) {
                    for (index in 0 until spinner.count - 1) {
                        val obj = spinner.getItemAtPosition(index) as DropdownMaster
                        if (obj.typeDetailID == address.addressProof) {
                            spinner.setSelection(index + 1)
                            return
                        }
                    }
                }
        }
    }

    private fun selectCurrentResidenceTypeValue(spinner: Spinner) {
        val addressDetail = currentApplicant.addressDetailList
        if (addressDetail != null) {
            for (address in addressDetail)
                if (address.addressTypeDetail == CURRENT) {
                    for (index in 0 until spinner.count - 1) {
                        val obj = spinner.getItemAtPosition(index) as DropdownMaster
                        if (obj.typeDetailID == address.residenceTypeTypeDetailID) {
                            spinner.setSelection(index + 1)
                            return
                        }
                    }
                }
        }
    }

    private fun selectPermanentResidenceTypeValue(spinner: Spinner) {
        val addressDetail = currentApplicant.addressDetailList
        if (addressDetail != null) {
            for (address in addressDetail)
                if (address.addressTypeDetail == PERMANENT) {
                    for (index in 0 until spinner.count - 1) {
                        val obj = spinner.getItemAtPosition(index) as DropdownMaster
                        if (obj.typeDetailID == address.residenceTypeTypeDetailID) {
                            spinner.setSelection(index + 1)
                            return
                        }
                    }
                }
        }
    }

    private fun selectPermanentAddressProofValue(spinner: Spinner) {
        val addressDetail = currentApplicant.addressDetailList
        if (addressDetail != null) {
            for (address in addressDetail)
                if (address.addressTypeDetail == PERMANENT) {
                    for (index in 0 until spinner.count - 1) {
                        val obj = spinner.getItemAtPosition(index) as DropdownMaster
                        if (obj.typeDetailID == address.addressProof) {
                            spinner.setSelection(index + 1)
                            return
                        }
                    }
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
        personalApplicantsList!![currentPosition] = getCurrentApplicant()
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
        currentApplicant.fatherFirstName = binding.basicInfoLayout.etFatherFirstName.text.toString()
        currentApplicant.fatherMiddleName = binding.basicInfoLayout.etFatherMiddleName.text.toString()
        currentApplicant.fatherLastName = binding.basicInfoLayout.etFatherLastName.text.toString()
        currentApplicant.dateOfBirth = convertDate.convertToApiFormat(binding.basicInfoLayout.etDOB.text.toString())
        currentApplicant.age = binding.basicInfoLayout.etAge.text.toString().toInt()
        currentApplicant.isMainApplicant = currentPosition == 0
        currentApplicant.incomeConsidered = binding.basicInfoLayout.cbIncomeConsidered.isChecked
        currentApplicant.alternateContact = binding.basicInfoLayout.etAlternateNum.text.toString()
        currentApplicant.numberOfEarningMembers = binding.basicInfoLayout.etNumOfEarningMember.text.toString().toInt()
        currentApplicant.numberOfEarningMembers = binding.basicInfoLayout.etNumOfEarningMember.text.toString().toInt()
        currentApplicant.numberOfEarningMembers = binding.basicInfoLayout.etNumOfEarningMember.text.toString().toInt()
        currentApplicant.numberOfEarningMembers = binding.basicInfoLayout.etNumOfEarningMember.text.toString().toInt()
        currentApplicant.numberOfEarningMembers = binding.basicInfoLayout.etNumOfEarningMember.text.toString().toInt()
        currentApplicant.numberOfEarningMembers = binding.basicInfoLayout.etNumOfEarningMember.text.toString().toInt()
        currentApplicant.contactDetail = getContactDetail()
        currentApplicant.addressDetailList = getAddressDetailList()
        personalApplicantsList?.removeAt(currentPosition)
        personalApplicantsList?.add(currentPosition, currentApplicant)
        return currentApplicant
    }

    private fun getContactDetail(): ContactDetail? {
        contactDetail?.email = binding.basicInfoLayout.etEmail.text.toString()
        contactDetail?.mobile = binding.basicInfoLayout.etMobile.text.toString()
        return contactDetail
    }

    private fun getAddressDetailList(): ArrayList<AddressDetail>? {
        val pAddressDetail = AddressDetail()
        pAddressDetail.rentAmount = binding.personalAddressLayout.etPermanentRentAmount.text.toString().toInt()
        pAddressDetail.address1 = binding.personalAddressLayout.etPermanentAddress.text.toString()
        pAddressDetail.landmark = binding.personalAddressLayout.etPermanentLandmark.text.toString()
        pAddressDetail.zip = binding.personalAddressLayout.etPermanentPinCode.text.toString()
        pAddressDetail.stayingInYears = binding.personalAddressLayout.etPermanentStaying.text.toString().toInt()
        pAddressDetail.addressTypeDetail = PERMANENT
        var cAddressDetail = AddressDetail()
        if (binding.personalAddressLayout.cbSameAsCurrent.isChecked) {
            cAddressDetail = pAddressDetail
            cAddressDetail.addressTypeDetail = CURRENT
        } else {
            cAddressDetail.rentAmount = binding.personalAddressLayout.etPermanentRentAmount.text.toString().toInt()
            cAddressDetail.address1 = binding.personalAddressLayout.etPermanentAddress.text.toString()
            cAddressDetail.landmark = binding.personalAddressLayout.etPermanentLandmark.text.toString()
            cAddressDetail.zip = binding.personalAddressLayout.etPermanentPinCode.text.toString()
            cAddressDetail.stayingInYears = binding.personalAddressLayout.etPermanentStaying.text.toString().toInt()
            cAddressDetail.addressTypeDetail = CURRENT
        }
        personalAddressDetail?.add(0, pAddressDetail)
        personalAddressDetail?.add(1, cAddressDetail)
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
                personalPostPresenter.callNetwork(ConstantsApi.CALL_POST_PERSONAL_INFO)
                personalApplicantsList?.add(currentPosition, currentApplicant)
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
        pinCodeListener(binding.personalAddressLayout.etCurrentPinCode, CURRENT)
        pinCodeListener(binding.personalAddressLayout.etPermanentPinCode, PERMANENT)
    }

    private fun pinCodeListener(pinCodeField: TextInputEditText?, type: String) {
        pinCodeField!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (pinCodeField.text!!.length == 6) {
                    addressType = type
                    mPinCode = pinCodeField.text.toString()
                    pinCodePresenter.callNetwork(ConstantsApi.CALL_PIN_CODE_DETAIL)
                }
            }
        })
    }

    private fun setDatePicker() {
        binding.basicInfoLayout.etDOB.setOnClickListener {
            AgeFromDOB(mContext, binding.basicInfoLayout.etDOB, binding.basicInfoLayout.etAge)
        }
        binding.etIssueDate.setOnClickListener {
            SelectDate(binding.etIssueDate, mContext)
        }
        binding.etExpiryDate.setOnClickListener {
            SelectDate(binding.etExpiryDate, mContext)
        }
    }

    override val personalInfoRequestPost: PersonalInfoMaster
        get() = getPersonalInfoMaster()

    override fun getPersonalPostInfoSuccess(value: Response.ResponseLoanApplication) {
        gotoNextFragment()
    }

    override fun getPersonalPostInfoFailure(msg: String) {
        saveDataToDB(getPersonalInfoMaster())
        showToast(msg)
    }

    private fun getPersonalInfoMaster(): PersonalInfoMaster {
        personalInfoMaster?.applicantDetails = personalApplicantsList
        personalInfoMaster!!.leadID = mLeadId!!.toInt()
        personalInfoMaster?.userID = empId!!.toInt()
        return personalInfoMaster!!
    }

    override val pinCode: String
        get() = mPinCode

    override fun getPinCodeSuccess(value: Response.ResponsePinCodeDetail) {
        val pinResponse = value.responseObj
        if (pinResponse != null && pinResponse.size > 0) {
            when (addressType) {
                PERMANENT -> setPermanentPinCodeDetails(value.responseObj[0])
                CURRENT -> setCurrentPinCodeDetails(value.responseObj[0])
            }
        }
    }

    override fun getPinCodeFailure(msg: String) {
        when (addressType) {
            PERMANENT -> clearPermanentPinCodeField()
            CURRENT -> clearCurrentPinCodeField()
        }
    }

    private fun clearPermanentPinCodeField() {
        binding.personalAddressLayout.etPermanentCity.text?.clear()
        binding.personalAddressLayout.spinnerPermanentState.adapter = null
    }

    private fun clearCurrentPinCodeField() {
        binding.personalAddressLayout.etCurrentCity.text?.clear()
        binding.personalAddressLayout.spinnerCurrentState.adapter = null
    }

    private fun setCurrentPinCodeDetails(pinCodeDetail: Response.PinCodeObj) {
        binding.personalAddressLayout.etCurrentCity.setText(pinCodeDetail.cityName)
        selectStateValueFromPin(binding.personalAddressLayout.spinnerCurrentState, pinCodeDetail)
    }

    private fun setPermanentPinCodeDetails(pinCodeDetail: Response.PinCodeObj) {
        binding.personalAddressLayout.etPermanentCity.setText(pinCodeDetail.cityName)
        selectStateValueFromPin(binding.personalAddressLayout.spinnerPermanentState, pinCodeDetail)
    }

    private fun selectStateValueFromPin(spinner: MaterialSpinner, pinCodeDetail: Response.PinCodeObj) {
//        spinner.adapter = StatesSpinnerAdapter(mContext, states)
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as StatesMaster
            if (obj.stateID == pinCodeDetail.stateID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, AssetLiabilityFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }

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
