package com.finance.app.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentPersonalBinding
import com.finance.app.model.Modals.AddKyc
import com.finance.app.model.Modals.ApplicantPersonal
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.presenter.PersonalInfoGetPresenter
import com.finance.app.presenter.presenter.PersonalInfoPostPresenter
import com.finance.app.utility.AgeFromDOB
import com.finance.app.utility.ClearPersonalForm
import com.finance.app.utility.SelectDate
import com.finance.app.utility.UploadData
import com.finance.app.view.adapters.recycler.adapter.AddKycAdapter
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.PersonalApplicantsAdapter
import com.finance.app.view.adapters.recycler.Spinner.YesNoSpinnerAdapter
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
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

class PersonalInfoFragment : BaseFragment(), LoanApplicationConnector.PostPersonalInfo,
        LoanApplicationConnector.GetPersonalInfo, PersonalApplicantsAdapter.ItemClickListener {

    private lateinit var binding: FragmentPersonalBinding
    private lateinit var mContext: Context
    private lateinit var allMasterDropDown: AllMasterDropDown
    private var kycAdapter: AddKycAdapter? = null
    private var personalApplicantAdapter: PersonalApplicantsAdapter? = null
    private var personalAddressDetail: ArrayList<AddressDetail>? = null
    private var mLeadId: String? = null
    private var empId: String? = null
    private val frag = this
    private val personalPostPresenter = PersonalInfoPostPresenter(this)
    private val personalGetPresenter = PersonalInfoGetPresenter(this)
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil

    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private var coApplicant = 1
        private lateinit var kycList: ArrayList<AddKyc>
        private lateinit var applicantTab: ArrayList<String>
        private var personalInfoMaster: PersonalInfoMaster? = PersonalInfoMaster()
        private var personalApplicantsList: ArrayList<PersonalApplicantsModel>? = null
        private var image: Bitmap? = null
        private var currentApplicant: PersonalApplicantsModel? = null
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
        applicantTab = ArrayList()
        getPersonalInfo()
        checkPropertySelection()
        setDatePicker()
        checkKycDataList()
        setCoApplicants()
        setClickListeners()
    }

    private fun getPersonalInfo() {
        mLeadId = sharedPreferences.getLeadId()
        empId = sharedPreferences.getUserId()
        personalGetPresenter.callNetwork(ConstantsApi.CALL_GET_PERSONAL_INFO)
    }

    override val leadId: String
        get() = mLeadId!!

    override fun getPersonalGetInfoSuccess(value: Response.ResponseGetPersonalInfo) {
        value.responseObj.let {
            saveDataToDB(value.responseObj)
            personalInfoMaster = value.responseObj
            personalApplicantsList = personalInfoMaster?.applicantDetails
        }
        showData(personalApplicantsList)
    }

    override fun getPersonalGetInfoFailure(msg: String) = getDataFromDB()

    private fun saveDataToDB(personalInfo: PersonalInfoMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().personalInfoDao().insertPersonalInfo(personalInfo)
        }
    }

    private fun showData(applicants: ArrayList<PersonalApplicantsModel>?) {
        if (personalApplicantsList != null) {
            applicants?.let {
                currentApplicant = applicants[0]
                fillFormWithApplicantData(currentApplicant!!)
            }
        }
        getDropDownsFromDB()
    }

    private fun fillFormWithApplicantData(personalInfo: PersonalApplicantsModel) {
        binding.basicInfoLayout.etDOB.setText(personalInfo.dateOfBirth)
        binding.basicInfoLayout.etFatherLastName.setText(personalInfo.fatherLastName)
        binding.basicInfoLayout.etFatherMiddleName.setText(personalInfo.fatherMiddleName)
        binding.basicInfoLayout.etFatherFirstName.setText(personalInfo.fatherFirstName)
        binding.basicInfoLayout.etSpouseMiddleName.setText(personalInfo.spouseMiddleName)
        binding.basicInfoLayout.etSpouseFirstName.setText(personalInfo.spouseFirstName)
        binding.basicInfoLayout.etSpouseLastName.setText(personalInfo.spouseLastName)
        binding.basicInfoLayout.etFirstName.setText(personalInfo.firstName)
        binding.basicInfoLayout.etMiddleName.setText(personalInfo.middleName)
//        binding.basicInfoLayout.etNumOfDependent.setText(personalInfo.numberOfDependents)
//        binding.basicInfoLayout.etNumOfEarningMember.setText(personalInfo.numberOfEarningMembers)
        binding.basicInfoLayout.etLastName.setText(personalInfo.lastName)
//        binding.basicInfoLayout.etAge.setText(personalInfo.age)
        personalInfo.addressDetailList?.let {
            fillAddressInfo(personalInfo.addressDetailList!!)
        }
    }

    private fun fillAddressInfo(addressDetailList: java.util.ArrayList<AddressDetail>) {
        fillCurrentAddressInfo(addressDetailList[0])
        fillPermanentAddressInfo(addressDetailList[1])
    }

    private fun fillCurrentAddressInfo(addressDetail: AddressDetail) {
        binding.personalAddressLayout.etCurrentAddress.setText(addressDetail.address1)
        binding.personalAddressLayout.etCurrentCity.setText(addressDetail.cityName)
        binding.personalAddressLayout.etCurrentPinCode.setText(addressDetail.zip.toString())
        binding.personalAddressLayout.etCurrentLandmark.setText(addressDetail.landmark)
        binding.personalAddressLayout.etCurrentRentAmount.setText(addressDetail.rentAmount.toString())
//        binding.personalAddressLayout.etCurrentStaying.setText(addressDetail.)
        binding.personalAddressLayout.etCurrentDistrict.setText(addressDetail.districtID.toString())
    }

    private fun fillPermanentAddressInfo(addressDetail: AddressDetail) {
        binding.personalAddressLayout.etPermanentAddress.setText(addressDetail.address1)
        binding.personalAddressLayout.etPermanentCity.setText(addressDetail.cityName)
        binding.personalAddressLayout.etPermanentPinCode.setText(addressDetail.zip.toString())
        binding.personalAddressLayout.etPermanentLandmark.setText(addressDetail.landmark)
        binding.personalAddressLayout.etPermanentRentAmount.setText(addressDetail.rentAmount.toString())
//        binding.personalAddressLayout.etPermanentStaying.setText(addressDetail.)
        binding.personalAddressLayout.etPermanentDistrict.setText(addressDetail.districtID.toString())

    }

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().personalInfoDao().getPersonalInfo(mLeadId!!).observe(this, Observer { personalMaster ->
            personalMaster?.let {
                personalInfoMaster = personalMaster
                personalApplicantsList = personalInfoMaster?.applicantDetails
            }
            showData(personalApplicantsList)
        })
    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setMasterDropDownValue(allMasterDropDown)
            }
        })
    }

    private fun setMasterDropDownValue(dropDown: AllMasterDropDown) {
        binding.spinnerIdentificationType.adapter = MasterSpinnerAdapter(mContext, dropDown.IdentificationType!!)
        binding.spinnerVerifiedStatus.adapter = MasterSpinnerAdapter(mContext, dropDown.VerifiedStatus!!)
        binding.basicInfoLayout.spinnerGender.adapter = MasterSpinnerAdapter(mContext, dropDown.Gender!!)
        binding.basicInfoLayout.spinnerNationality.adapter = MasterSpinnerAdapter(mContext, dropDown.Nationality!!)
        binding.basicInfoLayout.spinnerReligion.adapter = MasterSpinnerAdapter(mContext, dropDown.Religion!!)
        binding.basicInfoLayout.spinnerCaste.adapter = MasterSpinnerAdapter(mContext, dropDown.Caste!!)
        binding.basicInfoLayout.spinnerQualification.adapter = MasterSpinnerAdapter(mContext, dropDown.Qualification!!)
        binding.basicInfoLayout.spinnerDetailQualification.adapter = MasterSpinnerAdapter(mContext, dropDown.DetailQualification!!)
        binding.basicInfoLayout.spinnerMaritalStatus.adapter = MasterSpinnerAdapter(mContext, dropDown.MaritalStatus!!)
        binding.basicInfoLayout.spinnerRelationship.adapter = MasterSpinnerAdapter(mContext, dropDown.Relationship!!)
        binding.personalAddressLayout.spinnerCurrentAddressProof.adapter = MasterSpinnerAdapter(mContext, dropDown.AddressProof!!)
        binding.personalAddressLayout.spinnerPermanentAddressProof.adapter = MasterSpinnerAdapter(mContext, dropDown.AddressProof!!)
        binding.personalAddressLayout.spinnerCurrentResidenceType.adapter = MasterSpinnerAdapter(mContext, dropDown.ResidenceType!!)
        binding.personalAddressLayout.spinnerPermanentResidenceType.adapter = MasterSpinnerAdapter(mContext, dropDown.ResidenceType!!)
        binding.basicInfoLayout.spinnerLivingStandard.adapter = YesNoSpinnerAdapter(mContext)

        if (personalApplicantsList != null && personalApplicantsList!!.size > 0) {
            fillValueInDropDown()
        }
    }

    private fun fillValueInDropDown() {
        selectSpinnerGenderValue(binding.basicInfoLayout.spinnerGender)
        selectNationalityValue(binding.basicInfoLayout.spinnerNationality)
        selectReligionValue(binding.basicInfoLayout.spinnerReligion)
        selectCasteValue(binding.basicInfoLayout.spinnerCaste)
        selectQualificationValue(binding.basicInfoLayout.spinnerQualification)
        selectDetailQualificationValue(binding.basicInfoLayout.spinnerDetailQualification)
        selectMaritalStatusValue(binding.basicInfoLayout.spinnerMaritalStatus)
//        selectRelationshipValue(binding.basicInfoLayout.spinnerRelationship)
        selectCurrentAddressProofValue(binding.personalAddressLayout.spinnerCurrentAddressProof)
        selectCurrentResidenceTypeValue(binding.personalAddressLayout.spinnerCurrentResidenceType)
//        selectCurrentLivingStandardTypeValue(binding.personalAddressLayout.spinner)
        selectPermanentResidenceTypeValue(binding.personalAddressLayout.spinnerPermanentResidenceType)
        selectPermanentAddressProofValue(binding.personalAddressLayout.spinnerPermanentAddressProof)

    }

    private fun selectSpinnerGenderValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant!!.genderTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectNationalityValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant!!.nationalityTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectReligionValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant!!.religionTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectCasteValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant!!.casteTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectCurrentAddressProofValue(spinner: Spinner) {
        val addressDetail = currentApplicant!!.addressDetailList
        if (addressDetail!!.size > 0) {
            for (index in 0 until spinner.count - 1) {
                val obj = spinner.getItemAtPosition(index) as DropdownMaster
                if (obj.typeDetailID == addressDetail[0].addressTypeDetailID) {
                    spinner.setSelection(index + 1)
                    return
                }
            }
        }
    }

    private fun selectCurrentResidenceTypeValue(spinner: Spinner) {
        val addressDetail = currentApplicant!!.addressDetailList
        if (addressDetail!!.size > 0) {
            for (index in 0 until spinner.count - 1) {
                val obj = spinner.getItemAtPosition(index) as DropdownMaster
                if (obj.typeDetailID == addressDetail[0].residenceTypeTypeDetailID) {
                    spinner.setSelection(index + 1)
                    return
                }
            }
        }
    }

    private fun selectPermanentResidenceTypeValue(spinner: Spinner) {
        val addressDetail = currentApplicant!!.addressDetailList
        if (addressDetail!!.size > 0) {
            for (index in 0 until spinner.count - 1) {
                val obj = spinner.getItemAtPosition(index) as DropdownMaster
                if (obj.typeDetailID == addressDetail[1].residenceTypeTypeDetailID) {
                    spinner.setSelection(index + 1)
                    return
                }
            }
        }
    }

    private fun selectPermanentAddressProofValue(spinner: Spinner) {
        val addressDetail = currentApplicant!!.addressDetailList
        if (addressDetail!!.size > 0) {
            for (index in 0 until spinner.count - 1) {
                val obj = spinner.getItemAtPosition(index) as DropdownMaster
                if (obj.typeDetailID == addressDetail[1].addressTypeDetailID) {
                    spinner.setSelection(index + 1)
                    return
                }
            }
        }
    }

    private fun selectMaritalStatusValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant!!.maritialStatusTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectQualificationValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant!!.qualificationTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectDetailQualificationValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant!!.detailQualificationTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    override fun onApplicantClick(position: Int) {
        saveCurrentApplicant()
        ClearPersonalForm(binding)
//        getApplicantData(position)
        getParticularApplicantData()
    }

    private fun saveCurrentApplicant() {
        val position = 1
//        applicantsList!![position] = applicant
        sharedPreferences.savePersonalInfoForApplicants(allApplicants)
    }

    private fun onAddCoApplicantClick() {
        if (formValidation.validatePersonalInfo(binding)) {
            applicantTab.add("Co- Applicant $coApplicant")
            binding.rcApplicants.adapter!!.notifyDataSetChanged()
            ClearPersonalForm(binding)
            coApplicant++
        }
    }

    private fun checkPropertySelection() {
        val selected = sharedPreferences.getPropertySelection()
        if (!selected) {
            Toast.makeText(context, "Property not selected in Loan Information",
                    Toast.LENGTH_SHORT).show()
            disableAllFields()
        }
    }

    private fun disableAllFields() {
        formValidation.disablePersonalFields(binding)
    }

    private fun setCoApplicants() {
        applicantTab.add("Applicant")
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        personalApplicantAdapter = PersonalApplicantsAdapter(context!!, applicantTab)
        personalApplicantAdapter!!.setOnItemClickListener(this)
        binding.rcApplicants.adapter = personalApplicantAdapter
    }

    private fun checkKycDataList() {
        if (kycList.isNullOrEmpty()) {
            binding.rcKYC.visibility = View.GONE
        }
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

        binding.basicInfoLayout.ivUploadDobProof.setOnClickListener {
            UploadData(frag, mContext)
        }

        binding.btnAddApplicant.setOnClickListener {
            onAddCoApplicantClick()
        }

        binding.btnSaveAndContinue.setOnClickListener{
            if (formValidation.validatePersonalInfo(binding)) {
                setIncomeConsidered()
                sharedPreferences.savePersonalInfoForApplicants(allApplicants)
                gotoNextFragment()
            }
        }

        binding.personalAddressLayout.cbSameAsCurrent.setOnClickListener {
            if (binding.personalAddressLayout.cbSameAsCurrent.isChecked) {
                binding.personalAddressLayout.llPermanentAddress.visibility = View.GONE
            } else {
                binding.personalAddressLayout.llPermanentAddress.visibility = View.VISIBLE
            }
        }
    }

    private val allApplicants: ApplicantPersonal
        get() {
            return ApplicantPersonal(personalApplicantsList!!)
        }

    private fun setIncomeConsidered() {
        if (binding.cbIncomeConsidered.isChecked) {
            sharedPreferences.setIncomeConsideration("Yes")
        } else {
            sharedPreferences.setIncomeConsideration("No")
        }
    }

    private fun clearKycData() {
        binding.spinnerIdentificationType.isSelected = false
        binding.etIdNum.text?.clear()
        binding.etIssueDate.text?.clear()
        binding.etExpiryDate.text?.clear()
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
        kycAdapter = AddKycAdapter(activity!!, kycList)
        binding.rcKYC.adapter = kycAdapter
        binding.rcKYC.visibility = View.VISIBLE
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

    private fun getParticularApplicantData() {
        currentApplicant!!.addressDetailList = personalAddressDetail!!
        currentApplicant!!.dateOfBirth = binding.basicInfoLayout.etAge.text.toString()
        currentApplicant!!.spouseMiddleName = binding.basicInfoLayout.etSpouseMiddleName.text.toString()
        currentApplicant!!.spouseLastName = binding.basicInfoLayout.etSpouseLastName.text.toString()
        currentApplicant!!.spouseFirstName = binding.basicInfoLayout.etSpouseFirstName.text.toString()
        currentApplicant!!.firstName = binding.basicInfoLayout.etFirstName.text.toString()
        currentApplicant!!.lastName = binding.basicInfoLayout.etLastName.text.toString()
        currentApplicant!!.middleName = binding.basicInfoLayout.etMiddleName.text.toString()
        currentApplicant!!.fatherFirstName = binding.basicInfoLayout.etFatherFirstName.text.toString()
        currentApplicant!!.fatherMiddleName = binding.basicInfoLayout.etFatherMiddleName.text.toString()
        currentApplicant!!.fatherLastName = binding.basicInfoLayout.etFatherLastName.text.toString()
        val casteType = binding.basicInfoLayout.spinnerCaste.selectedItem as DropdownMaster
        currentApplicant!!.casteTypeDetailID = casteType.typeDetailID!!
        val detailQualification = binding.basicInfoLayout.spinnerDetailQualification.selectedItem as DropdownMaster
        currentApplicant!!.detailQualificationTypeDetailID = detailQualification.typeDetailID!!
        currentApplicant!!.contactDetail
        currentApplicant!!.age = binding.basicInfoLayout.etAge.text.toString().toInt()
        currentApplicant!!.contactDetail = ContactDetail()
    }

    override val personalInfoRequestPost: Requests.RequestPostPersonalInfo
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getPersonalPostInfoSuccess(value: Response.ResponseLoanApplication) {
        gotoNextFragment()
    }

    override fun getPersonalPostInfoFailure(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
}
