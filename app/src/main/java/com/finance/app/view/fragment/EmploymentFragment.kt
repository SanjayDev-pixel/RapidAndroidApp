package com.finance.app.view.fragment
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentEmploymentBinding
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.connector.PinCodeDetailConnector
import com.finance.app.presenter.presenter.LoanAppGetPresenter
import com.finance.app.presenter.presenter.LoanAppPostPresenter
import com.finance.app.presenter.presenter.PinCodeDetailPresenter
import com.finance.app.utility.*
import com.finance.app.view.activity.UploadDataActivity
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.YesNoSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
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
import javax.inject.Inject

class EmploymentFragment : BaseFragment(),  LoanApplicationConnector.PostLoanApp,
        LoanApplicationConnector.GetLoanApp, PinCodeDetailConnector.PinCode,
        ApplicantsAdapter.ItemClickListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentEmploymentBinding
    private lateinit var mContext: Context
    private val pinCodePresenter = PinCodeDetailPresenter(this)
    private val loanAppPostPresenter = LoanAppPostPresenter(this)
    private val loanAppGetPresenter = LoanAppGetPresenter(this)
    private lateinit var profileSegment: ArrayList<DropdownMaster>
    private lateinit var subProfileSegment: ArrayList<DropdownMaster>
    private lateinit var pinCodeFromForm: String
    private var mLead: AllLeadMaster? = null
    private var mLeadId: String? = null
    private var empId: String? = null
    private val frag = this
    private var applicantAdapter: ApplicantsAdapter? = null
    private var employmentApplicantsList: ArrayList<EmploymentApplicantsModel>? = ArrayList()
    private var employmentMaster: EmploymentMaster? = EmploymentMaster()
    private var currentApplicant: EmploymentApplicantsModel = EmploymentApplicantsModel()
    private var currentPosition = 0
    private var salaryDistrictId = 0
    private var salaryCityId = 0
    private var senpDistrictId = 0
    private var senpCityId = 0

    companion object {
        private const val SALARY = 1
        private const val SENP = 2
        private lateinit var applicantTab: ArrayList<String>
        var employmentList: ArrayList<Requests.EmploymentDetail> = ArrayList()
        private lateinit var allMasterDropDown: AllMasterDropDown
        private lateinit var states: List<StatesMaster>
        private val responseConversion = ResponseConversion()
        private val requestConversion = RequestConversion()
        private val convertDate = ConvertDate()
        private var eApplicantList = EmploymentApplicantList()
        private var formSelected: Int? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_employment)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        applicantTab = ArrayList()
        mContext = requireContext()
        getEmploymentInfo()
        checkIncomeConsideration()
        setDatePicker()
        setCoApplicants(employmentApplicantsList)
        getDropDownsFromDB()
        setClickListeners()
    }

    private fun getEmploymentInfo() {
        mLead = sharedPreferences.getLeadDetail()
        mLeadId = mLead!!.leadID.toString()
        empId = sharedPreferences.getUserId()
        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
    }

    override val storageType: String
        get() = employmentMaster?.storageType!!

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        value.responseObj?.let {
            employmentMaster = responseConversion.toEmploymentMaster(value.responseObj)
            eApplicantList = employmentMaster?.draftData!!
            employmentApplicantsList = eApplicantList.applicantDetails
            saveDataToDB(employmentMaster!!)
        }
        setCoApplicants(employmentApplicantsList)
        showData(employmentApplicantsList)
    }

    private fun showData(applicantList: ArrayList<EmploymentApplicantsModel>?) {
        if (applicantList != null) {
            for (applicant in applicantList) {
                if (applicant.isMainApplicant) {
                    currentApplicant = applicant
                    fillFormWithCurrentApplicant(currentApplicant)
                }
            }
        }
        getDropDownsFromDB()
    }

    private fun saveDataToDB(employmentMaster: EmploymentMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().employmentDao().insertEmployment(employmentMaster)
        }
    }

    private fun fillFormWithCurrentApplicant(currentApplicant: EmploymentApplicantsModel) {
    }

    override fun getLoanAppGetFailure(msg: String) = getDataFromDB()

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().employmentDao().getEmployment(mLeadId!!).observe(this, Observer { master ->
            master?.let {
                employmentMaster = master
                eApplicantList = employmentMaster?.draftData!!
                employmentApplicantsList = eApplicantList.applicantDetails
            }
            setCoApplicants(employmentApplicantsList)
            showData(employmentApplicantsList)
        })
    }

    private fun setCoApplicants(employmentApplicantsList: ArrayList<EmploymentApplicantsModel>?) {
        applicantTab = ArrayList()
        applicantTab.add("Applicant")
        if (employmentApplicantsList != null && employmentApplicantsList.size > 1) {
            for (position in 1 until employmentApplicantsList.size) {
                applicantTab.add("CoApplicant $position")
            }
        }
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapter = ApplicantsAdapter(context!!, applicantTab)
        applicantAdapter!!.setOnItemClickListener(this)
        binding.rcApplicants.adapter = applicantAdapter
    }

    override fun onApplicantClick(position: Int) {
        if (formValidation.validateSenpEmployment(binding.layoutSenp)) {
            saveCurrentApplicant()
            currentPosition = position
            ClearEmploymentForm(binding, mContext, allMasterDropDown, states)
            waitFor2Sec(position)
        } else showToast(getString(R.string.mandatory_field_missing))
    }

    private fun waitFor2Sec(position: Int) {

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
//                    IntFromDateString(incorporationDate, binding.layoutSenp.etBusinessVintage)
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
                    loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_EMPLOYMENT)
                }
            }
            SENP -> {
                if (formValidation.validateSenpEmployment(binding = binding.layoutSenp)) {
                    loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_EMPLOYMENT)
                }
            }
        }
    }

    private fun setSalaryPinCodeListener() {
        binding.layoutSalary.layoutAddress.etPinCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (binding.layoutSalary.layoutAddress.etPinCode.text!!.length == 6) {
                    formSelected = SALARY
                    pinCodeFromForm = binding.layoutSalary.layoutAddress.etPinCode.text.toString()
                    pinCodePresenter.callNetwork(ConstantsApi.CALL_PIN_CODE_DETAIL)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setSenpPinCodeListener() {
        binding.layoutSenp.layoutAddress.etPinCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (binding.layoutSenp.layoutAddress.etPinCode.text!!.length == 6) {
                    formSelected = SENP
                    pinCodeFromForm = binding.layoutSenp.layoutAddress.etPinCode.text.toString()
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
        val pinResponse = value.responseObj
        if (pinResponse != null && pinResponse.size > 0) {
            when (formSelected) {
                SENP -> setPinDetailInSenp(pinResponse[0])
                SALARY -> setPinDetailInSalary(pinResponse[0])
            }
        }
    }

    override fun getPinCodeFailure(msg: String) {
        showToast(getString(R.string.failure_pin_code_api))
    }

    private fun setPinDetailInSenp(pinCodeObj: Response.PinCodeObj) {
        binding.layoutSenp.layoutAddress.etCity.setText(pinCodeObj.cityName)
        senpCityId = pinCodeObj.cityID
    }

    private fun setPinDetailInSalary(pinCodeObj: Response.PinCodeObj) {
        binding.layoutSalary.layoutAddress.etCity.setText(pinCodeObj.cityName)
        salaryCityId = pinCodeObj.cityID
    }

    private val employeeSalaryAddress: Requests.EmploymentAddressBean
        get() {
            return Requests.EmploymentAddressBean(address1 = binding.layoutSalary.layoutAddress.etAddress.text.toString(),
                    cityID = salaryCityId, districtID = salaryDistrictId, cityName = binding.layoutSalary.layoutAddress.etCity.text.toString(),
                    zip = binding.layoutSalary.layoutAddress.etPinCode.text.toString(), landmark = binding.layoutSalary.layoutAddress.etLandmark.text.toString())
        }

    private val employeeSenpAddress: Requests.EmploymentAddressBean
        get() {
            return Requests.EmploymentAddressBean(address1 = binding.layoutSenp.layoutAddress.etAddress.text.toString(),
                    cityID = senpCityId, districtID = senpDistrictId, cityName = binding.layoutSenp.layoutAddress.etCity.text.toString(),
                    zip = binding.layoutSenp.layoutAddress.etPinCode.text.toString(), landmark = binding.layoutSenp.layoutAddress.etLandmark.text.toString())
        }

    private fun getEmploymentMaster(): EmploymentMaster {
        eApplicantList.applicantDetails = employmentApplicantsList
        employmentMaster?.draftData = eApplicantList
        employmentMaster!!.leadID = mLeadId!!.toInt()
        return employmentMaster!!
    }

    override val loanAppRequestPost: LoanApplicationRequest
        get() = requestConversion.employmentRequest(getEmploymentMaster())

    override fun getLoanAppPostSuccess(value: Response.ResponseGetLoanApplication) {
        gotoNextFragment()
    }

    override fun getLoanAppPostFailure(msg: String) {
        saveDataToDB(getEmploymentMaster())
        showToast(msg)
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, BankDetailFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }
}