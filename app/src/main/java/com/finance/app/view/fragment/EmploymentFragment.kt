package com.finance.app.view.fragment
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentEmploymentBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.connector.PinCodeDetailConnector
import com.finance.app.presenter.presenter.EmploymentPostPresenter
import com.finance.app.presenter.presenter.PinCodeDetailPresenter
import com.finance.app.utility.ClearEmploymentForm
import com.finance.app.utility.ConvertDateToApiFormat
import com.finance.app.utility.IntFromDateString
import com.finance.app.utility.SelectDate
import com.finance.app.view.activity.UploadDataActivity
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.PersonalApplicantsAdapter
import com.finance.app.view.adapters.recycler.Spinner.YesNoSpinnerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class EmploymentFragment : BaseFragment(), PinCodeDetailConnector.PinCode,
        LoanApplicationConnector.PostEmployment, PersonalApplicantsAdapter.ItemClickListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentEmploymentBinding
    private lateinit var mContext: Context
    private val pinCodePresenter = PinCodeDetailPresenter(this)
    private val employmentPresenter = EmploymentPostPresenter(this)
    private lateinit var allMasterDropDown: AllMasterDropDown
    private lateinit var profileSegment: ArrayList<DropdownMaster>
    private lateinit var subProfileSegment: ArrayList<DropdownMaster>
    private lateinit var pinCodeFromForm: String
    private var applicantAdapterPersonal: PersonalApplicantsAdapter? = null
    private var salaryDistrictId = 0
    private var salaryCityId = 0
    private var senpDistrictId = 0
    private var senpCityId = 0

    companion object {
        private const val SELECT_PDF_CODE = 1
        private const val SALARY = 1
        private const val SENP = 2
        private const val CLICK_IMAGE_CODE = 2
        private const val SELECT_IMAGE_CODE = 3
        private lateinit var applicantTab: ArrayList<String>
        var employmentList: ArrayList<Requests.EmploymentDetail> = ArrayList()
        private var image: Bitmap? = null
        private var pdf: Uri? = null
        private var formSelected: Int? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_employment)
        init()
        return binding.root
    }

    override fun init() {
        applicantTab = ArrayList()
        mContext = requireContext()
        ArchitectureApp.instance.component.inject(this)
        checkIncomeConsideration()
        setDatePicker()
        setCoApplicants()
        getDropDownsFromDB()
        setClickListeners()
    }

    private fun setCoApplicants() {
        applicantTab.add("Applicant")
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapterPersonal = PersonalApplicantsAdapter(context!!, applicantTab)
        binding.rcApplicants.adapter = applicantAdapterPersonal
        applicantAdapterPersonal!!.setOnItemClickListener(this)
    }

    override fun onApplicantClick(position: Int) {
        saveCurrentApplicant()
        ClearEmploymentForm(binding)
        getParticularApplicantData(position)
    }

    private fun saveCurrentApplicant() {
    }

    private fun getParticularApplicantData(position: Int) {

    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setUpProfileSegmentDropDown(allMasterDropDown)
            }
        })
    }

    private fun checkIncomeConsideration() {
        val selected = sharedPreferences.getIncomeConsideration()
        if (!selected) {
            Toast.makeText(context, "Income not considered in Loan Information",
                    Toast.LENGTH_SHORT).show()
//            disableAllFields()
        }
    }

    private fun disableAllFields() {
        formValidation.disableEmploymentFields(binding)
    }

    private fun setClickListeners() {
        binding.btnSaveAndContinue.setOnClickListener {
            callApi()
        }
        binding.ivDocumentUpload.setOnClickListener {
            UploadDataActivity.start(mContext, allMasterDropDown.EmploymentType!!)
        }
        setListenerForAge()
        setSalaryPinCodeListener()
        setSenpPinCodeListener()
    }

    private fun setListenerForAge() {
        binding.layoutSenp.etIncorporationDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val incorporationDate: String? = binding.layoutSenp.etIncorporationDate.text.toString()
                if (incorporationDate != null) {
                    IntFromDateString(incorporationDate, binding.layoutSenp.etBusinessVintage)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun callApi() {
        when (formSelected) {
            SALARY -> {
                if (formValidation.validateSalaryEmployment(binding = binding.layoutSalary)) {
                    employmentPresenter.callNetwork(ConstantsApi.CALL_POST_EMPLOYMENT)
                }
            }
            SENP -> {
                if (formValidation.validateSenpEmployment(binding = binding.layoutSenp)) {
                    employmentPresenter.callNetwork(ConstantsApi.CALL_POST_EMPLOYMENT)
                }
            }
        }
    }

    private fun setSalaryPinCodeListener() {
        binding.layoutSalary.etPinCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (binding.layoutSalary.etPinCode.text!!.length == 6) {
                    formSelected = SALARY
                    pinCodeFromForm = binding.layoutSalary.etPinCode.text.toString()
                    pinCodePresenter.callNetwork(ConstantsApi.CALL_PIN_CODE_DETAIL)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setSenpPinCodeListener() {
        binding.layoutSenp.etPinCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (binding.layoutSenp.etPinCode.text!!.length == 6) {
                    formSelected = SENP
                    pinCodeFromForm = binding.layoutSenp.etPinCode.text.toString()
                    pinCodePresenter.callNetwork(ConstantsApi.CALL_PIN_CODE_DETAIL)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setDatePicker() {
        binding.layoutSalary.etJoiningDate.setOnClickListener {
            SelectDate(binding.layoutSalary.etJoiningDate, mContext)
        }
        binding.layoutSenp.etIncorporationDate.setOnClickListener{
            SelectDate(binding.layoutSenp.etIncorporationDate, mContext)
        }
    }

    private fun setUpProfileSegmentDropDown(products: AllMasterDropDown) {
        profileSegment = ArrayList()
        profileSegment = products.ProfileSegment!!
        binding.spinnerProfileSegment.adapter = MasterSpinnerAdapter(mContext, profileSegment)
        binding.spinnerProfileSegment?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val profile = parent.selectedItem as DropdownMaster
                    createSubProfileSegmentList(profile.typeDetailID, products.SubProfileSegment)
                }
            }
        }
    }

    private fun createSubProfileSegmentList(profileId: Int?, subProfile: ArrayList<DropdownMaster>?) {
        subProfileSegment = ArrayList()
        for (sub in subProfile!!) {
            if (sub.refTypeDetailID == profileId) {
                subProfileSegment.add(sub)
            }
        }
        setUpSubProfileSegmentDropDown(subProfileSegment)
    }

    private fun setUpSubProfileSegmentDropDown(subProfile: ArrayList<DropdownMaster>) {
        binding.spinnerSubProfile.adapter = MasterSpinnerAdapter(mContext, subProfile)
        binding.spinnerSubProfile?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val profileSub = parent.selectedItem as DropdownMaster
                    when (profileSub.typeDetailID) {
                        118, 119 -> showSalaryForm(profileSub.typeDetailID!!)
                        116, 117 -> showSenpForm(profileSub.typeDetailID!!)
                    }
                }
            }
        }
    }

    private fun showSalaryForm(typeDetailID: Int) {
        if (typeDetailID == 119) {
            binding.layoutSalary.cbIsPensioner.visibility = View.VISIBLE
        } else {
            binding.layoutSalary.cbIsPensioner.visibility = View.GONE
        }
        binding.layoutSalary.llSalary.visibility = View.VISIBLE
        binding.layoutSenp.llSenp.visibility = View.GONE
        binding.spinnerAllEarningMember.visibility = View.GONE
        setUpSalaryDropDownValue()
        getNetIncomeValue()
    }

    private fun getNetIncomeValue() {
        binding.layoutSalary.etDeduction.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.layoutSalary.etDeduction.text.toString() == "" && binding.layoutSalary.etGrossIncome.text.toString() != "") {
                    binding.layoutSalary.etNetIncome.setText((Integer.valueOf(binding.layoutSalary.etGrossIncome.text.toString()) -
                            Integer.valueOf(binding.layoutSalary.etDeduction.text.toString())).toString())
                }
            }
        })
    }

    private fun showSenpForm(typeDetailID: Int) {
        if (typeDetailID == 116) {
            setUpAllEarningMemberDropDownValue()
            binding.layoutSenp.inputMonthlyIncome.visibility = View.VISIBLE
        } else {
            binding.spinnerAllEarningMember.visibility = View.GONE
            binding.layoutSenp.inputMonthlyIncome.visibility = View.GONE
        }
        binding.layoutSenp.llSenp.visibility = View.VISIBLE
        binding.layoutSalary.llSalary.visibility = View.GONE
        setUpSenpDropDownValue()
        getAverageMonthlyIncome()
    }

    private fun getAverageMonthlyIncome() {
        binding.layoutSenp.etLastYearIncome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.layoutSenp.etLastYearIncome.text.toString() == "" && binding.layoutSenp.etCurrentYearIncome.text.toString() != "") {
                    binding.layoutSenp.etAverageMonthlyIncome.setText((Integer.valueOf(binding.layoutSenp.etLastYearIncome.text.toString()) +
                            Integer.valueOf(binding.layoutSenp.etLastYearIncome.text.toString()) / 24).toString())
                }
            }
        })
    }

    private fun setUpAllEarningMemberDropDownValue() {
        binding.spinnerAllEarningMember.visibility = View.VISIBLE
        binding.spinnerAllEarningMember.adapter = YesNoSpinnerAdapter(mContext)
    }

    private fun setUpSenpDropDownValue() {
        val businessSetupTypeList = allMasterDropDown.EmploymentType
        val constitutionList = allMasterDropDown.Constitution
        val industryList = allMasterDropDown.Industry
        binding.layoutSenp.spinnerBusinessSetUpType.adapter = MasterSpinnerAdapter(mContext, businessSetupTypeList!!)
        binding.layoutSenp.spinnerConstitution.adapter = MasterSpinnerAdapter(mContext, constitutionList!!)
        binding.layoutSenp.spinnerIndustry.adapter = MasterSpinnerAdapter(mContext, industryList!!)
    }

    private fun setUpSalaryDropDownValue() {
        val employmentTypeList = allMasterDropDown.EmploymentType
        val sectorList = allMasterDropDown.Sector
        val industryList = allMasterDropDown.Industry
        binding.layoutSalary.spinnerEmploymentType.adapter = MasterSpinnerAdapter(mContext, employmentTypeList!!)
        binding.layoutSalary.spinnerSector.adapter = MasterSpinnerAdapter(mContext, sectorList!!)
        binding.layoutSalary.spinnerIndustry.adapter = MasterSpinnerAdapter(mContext, industryList!!)
    }

    override val pinCode: String
        get() = pinCodeFromForm

    override fun getPinCodeSuccess(value: Response.ResponsePinCodeDetail) {
        when (formSelected) {
            SENP -> setPinDetailInSenp(value.responseObj[0])
            SALARY -> setPinDetailInSalary(value.responseObj[0])
        }
    }

    override fun getPinCodeFailure(msg: String) {
        showToast(getString(R.string.failure_pin_code_api))
    }

    private fun setPinDetailInSenp(pinCodeObj: Response.PinCodeObj) {
        binding.layoutSenp.etCity.setText(pinCodeObj.cityName)
        senpCityId = pinCodeObj.cityID
        binding.layoutSenp.etDistrict.setText(pinCodeObj.districtName)
        senpDistrictId = pinCodeObj.districtID
        binding.layoutSenp.etState.setText(pinCodeObj.stateName)
    }

    private fun setPinDetailInSalary(pinCodeObj: Response.PinCodeObj) {
        binding.layoutSalary.etCity.setText(pinCodeObj.cityName)
        salaryCityId = pinCodeObj.cityID
        binding.layoutSalary.etDistrict.setText(pinCodeObj.districtName)
        salaryDistrictId = pinCodeObj.districtID
        binding.layoutSalary.etState.setText(pinCodeObj.stateName)
    }

    override val employmentRequestPost: Requests.RequestPostEmployment
        get() = mEmploymentRequestPost

    private val mEmploymentRequestPost: Requests.RequestPostEmployment
        get() {
            employmentList.add(mEmploymentDetail)
            return Requests.RequestPostEmployment(leadID = 1, applicantDetails = employmentList)
        }

    private val mEmploymentDetail: Requests.EmploymentDetail
        get() {
            val convertDate = ConvertDateToApiFormat()
            var retirementAge: Int? = null
            var totalExp: String? = null
            val company: String
            var vintageYear: Int? = null
            val industry: DropdownMaster
            var dateOfJoining: String? = null
            var incorporationDate: String? = null
            val address: Requests.EmploymentAddressBean = if (formSelected == SALARY) {
                company = binding.layoutSalary.etCompanyName.text.toString()
                industry = binding.layoutSalary.spinnerIndustry.selectedItem as DropdownMaster
                retirementAge = binding.layoutSalary.etRetirementAge.text.toString().toInt()
                totalExp = binding.layoutSalary.etTotalExperience.text.toString()
                dateOfJoining = convertDate.convertToDesirableFormat(binding.layoutSalary.etJoiningDate.text.toString())
                employeeSalaryAddress

            } else {
                company = binding.layoutSenp.etBusinessName.text.toString()
                industry = binding.layoutSenp.spinnerIndustry.selectedItem as DropdownMaster
                vintageYear = binding.layoutSenp.etBusinessVintage.text.toString().toInt()
                incorporationDate = convertDate.convertToDesirableFormat(binding.layoutSenp.etIncorporationDate.text.toString())
                employeeSenpAddress
            }
            val mProfileSegment = binding.spinnerProfileSegment.selectedItem as DropdownMaster?
            val mSubProfileSegment = binding.spinnerSubProfile.selectedItem as DropdownMaster?
            val mEmploymentType = binding.layoutSalary.spinnerEmploymentType.selectedItem as DropdownMaster?
            val mBusinessType = binding.layoutSenp.spinnerBusinessSetUpType.selectedItem as DropdownMaster?
            val mSector = binding.layoutSalary.spinnerSector.selectedItem as DropdownMaster?
            val mConstitution = binding.layoutSenp.spinnerConstitution.selectedItem as DropdownMaster?
            return Requests.EmploymentDetail(allEarningMembers = binding.spinnerAllEarningMember.selectedItemPosition,
                    companyName = company, employeeID = binding.layoutSalary.etEmployeeId.text.toString(),
                    loanApplicationID = 1, profileSegmentTypeDetailID = mProfileSegment?.typeDetailID,
                    subProfileTypeDetailID = mSubProfileSegment?.typeDetailID, applicantID = 1,
                    employmentTypeDetailID = mEmploymentType?.typeDetailID, addressBean = address,
                    designation = binding.layoutSalary.etDesignation.text.toString(),
                    dateOfJoining = dateOfJoining, dateOfIncorporation = incorporationDate,
                    officialMailID = binding.layoutSalary.etOfficialMailId.text.toString(),
                    businessSetupTypeDetailID = mBusinessType?.typeDetailID, totalExperience = totalExp,
                    industryTypeDetailID = industry.typeDetailID, businessVinatgeInYear = vintageYear,
                    sectorTypeDetailID = mSector?.typeDetailID, retirementAge = retirementAge,
                    constitutionTypeDetailID = mConstitution?.typeDetailID,
                    gstRegistration = binding.layoutSenp.etGstRegistration.text.toString()
            )
        }

    private val employeeSalaryAddress: Requests.EmploymentAddressBean
        get() {
            return Requests.EmploymentAddressBean(address1 = binding.layoutSalary.etOfficeAddress.text.toString(),
                    cityID = salaryCityId, districtID = salaryDistrictId, cityName = binding.layoutSalary.etCity.text.toString(),
                    zip = binding.layoutSalary.etPinCode.text.toString(), landmark = binding.layoutSalary.etLandmark.text.toString())
        }

    private val employeeSenpAddress: Requests.EmploymentAddressBean
        get() {
            return Requests.EmploymentAddressBean(address1 = binding.layoutSenp.etBusinessAddress.text.toString(),
                    cityID = senpCityId, districtID = senpDistrictId, cityName = binding.layoutSenp.etCity.text.toString(),
                    zip = binding.layoutSenp.etPinCode.text.toString(), landmark = binding.layoutSenp.etLandmark.text.toString())
        }

    override fun getEmploymentPostSuccess(value: Response.ResponseLoanApplication) {
        gotoNextFragment()
    }

    override fun getEmploymentPostFailure(msg: String) {
        showToast(msg)
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, BankDetailFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val returnUri = data!!.data
            when (requestCode) {
                SELECT_PDF_CODE -> {
                    Log.i("URI: ", returnUri?.toString())
                    pdf = returnUri
                    binding.layoutSalary.tvDocumentUpload.visibility = View.GONE
                    binding.layoutSalary.ivThumbnail.visibility = View.GONE
                    binding.layoutSalary.ivPdf.visibility = View.VISIBLE
                }
                SELECT_IMAGE_CODE -> {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
                    image = bitmap
                    binding.layoutSalary.tvDocumentUpload.visibility = View.GONE
                    binding.layoutSalary.ivThumbnail.setImageBitmap(bitmap)
                }
                CLICK_IMAGE_CODE -> {
                    val thumbnail = data.extras!!.get("data") as Bitmap
                    image = thumbnail
                    binding.layoutSalary.tvDocumentUpload.visibility = View.GONE
                    binding.layoutSalary.ivThumbnail.setImageBitmap(thumbnail)
                }
            }
        }
    }
}