package com.finance.app.view.fragment

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
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
import com.finance.app.databinding.FragmentEmploymentBinding
import com.finance.app.databinding.LayoutSalaryBinding
import com.finance.app.databinding.LayoutSenpBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.connector.PinCodeDetailConnector
import com.finance.app.presenter.presenter.LoanAppGetPresenter
import com.finance.app.presenter.presenter.LoanAppPostPresenter
import com.finance.app.presenter.presenter.PinCodeDetailPresenter
import com.finance.app.utility.*
import com.finance.app.view.activity.UploadDataActivity
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.StatesSpinnerAdapter
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
        private var employmentList: ArrayList<Requests.EmploymentDetail> = ArrayList()
        private lateinit var allMasterDropDown: AllMasterDropDown
        private lateinit var states: List<StatesMaster>
        private val responseConversion = ResponseConversion()
        private val requestConversion = RequestConversion()
        private val convertDate = ConvertDate()
        private var employmentDraft = EmploymentApplicantList()
        private var formSelected: Int? = 0
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
        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
    }

    override val storageType: String
        get() = employmentMaster?.storageType!!

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        value.responseObj?.let {
            employmentMaster = responseConversion.toEmploymentMaster(value.responseObj)
            employmentDraft = employmentMaster?.draftData!!
            employmentApplicantsList = employmentDraft.applicantDetails
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

    override fun getLoanAppGetFailure(msg: String) = getDataFromDB()

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().employmentDao().getEmployment(mLeadId!!).observe(this, Observer { master ->
            master?.let {
                employmentMaster = master
                employmentDraft = employmentMaster?.draftData!!
                employmentApplicantsList = employmentDraft.applicantDetails
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
        val progress = ProgressDialog(mContext)
        progress.setMessage(getString(R.string.msg_saving))
        progress.setCancelable(false)
        progress.show()
        val handler = Handler()
        handler.postDelayed({
            getParticularApplicantData(position)
            progress.dismiss()
        }, 2000)
        applicantAdapter!!.notifyDataSetChanged()

    }

    private fun saveCurrentApplicant() {
        if (employmentApplicantsList!!.size <= 0) {
            employmentApplicantsList!!.add(getCurrentApplicant())
        } else {
            employmentApplicantsList!![currentPosition] = getCurrentApplicant()
        }
    }

    private fun getCurrentApplicant(): EmploymentApplicantsModel {
        when (formSelected) {
            SALARY ->
                currentApplicant = getSalaryForm(binding.layoutSalary)
            SENP ->
                currentApplicant = getSenpForm(binding.layoutSenp)
        }
        return currentApplicant
    }

    private fun getSenpForm(binding: LayoutSenpBinding): EmploymentApplicantsModel {
        val constitution = binding.spinnerConstitution.selectedItem as DropdownMaster?
        val industry = binding.spinnerIndustry.selectedItem as DropdownMaster?
        val businessSetupType = binding.spinnerBusinessSetUpType.selectedItem as DropdownMaster?
        currentApplicant.companyName = binding.etBusinessName.toString()
        currentApplicant.constitutionTypeDetailID = constitution?.typeDetailID
        currentApplicant.businessSetupTypeDetailID = businessSetupType?.typeDetailID
        currentApplicant.industryTypeDetailID = industry?.typeDetailID
        currentApplicant.dateOfIncorporation = binding.etIncorporationDate.text.toString()
        currentApplicant.gstRegistration = binding.etGstRegistration.text.toString()
        currentApplicant.businessVinatgeInYear = binding.etBusinessVintage.text.toString().toInt()
        currentApplicant.addressBean = getSenpAddress(binding)
        return currentApplicant
    }

    private fun getSenpAddress(binding: LayoutSenpBinding): AddressDetail? {
        val address = AddressDetail()
        val state = binding.layoutAddress.spinnerState.selectedItem as StatesMaster?
        address.address1 = binding.layoutAddress.etAddress.text.toString()
        address.cityID = salaryCityId
        address.districtID = salaryDistrictId
        address.cityName = binding.layoutAddress.etCity.text.toString()
        address.zip = binding.layoutAddress.etPinCode.text.toString()
        address.landmark = binding.layoutAddress.etLandmark.text.toString()
        address.stateID = state?.stateID
        address.contactNum = binding.layoutAddress.etContactNum.text.toString()
        return address
    }

    private fun getSalaryForm(binding: LayoutSalaryBinding): EmploymentApplicantsModel {
        val sector = binding.spinnerSector.selectedItem as DropdownMaster?
        val industry = binding.spinnerIndustry.selectedItem as DropdownMaster?
        val employment = binding.spinnerEmploymentType.selectedItem as DropdownMaster?
        currentApplicant.companyName = binding.etCompanyName.toString()
        currentApplicant.sectorTypeDetailID = sector?.typeDetailID
        currentApplicant.industryTypeDetailID = industry?.typeDetailID
        currentApplicant.employmentTypeDetailID = employment?.typeDetailID
        currentApplicant.designation = binding.etDesignation.text.toString()
        currentApplicant.dateOfJoining = binding.etJoiningDate.text.toString()
        currentApplicant.totalExperience = binding.etTotalExperience.text.toString()
        currentApplicant.retirementAge = binding.etRetirementAge.text.toString().toInt()
        currentApplicant.officialMailID = binding.etOfficialMailId.text.toString()
        currentApplicant.addressBean = getSalaryAddress(binding)
        return currentApplicant
    }

    private fun getSalaryAddress(binding: LayoutSalaryBinding): AddressDetail? {
        val address = AddressDetail()
        val state = binding.layoutAddress.spinnerState.selectedItem as StatesMaster?
        address.address1 = binding.layoutAddress.etAddress.text.toString()
        address.cityID = salaryCityId
        address.districtID = salaryDistrictId
        address.cityName = binding.layoutAddress.etCity.text.toString()
        address.zip = binding.layoutAddress.etPinCode.text.toString()
        address.landmark = binding.layoutAddress.etLandmark.text.toString()
        address.stateID = state?.stateID
        address.contactNum = binding.layoutAddress.etContactNum.text.toString()
        return address
    }

    private fun getParticularApplicantData(position: Int) {
        currentApplicant = if (position >= employmentApplicantsList!!.size) {
            EmploymentApplicantsModel()
        } else {
            employmentApplicantsList!![position]
        }
        selectProfileAndSubProfile()
    }

    private fun selectProfileAndSubProfile() {
        val profileSpinner = binding.spinnerProfileSegment
        for (index in 0 until profileSpinner.count - 1) {
            val obj = profileSpinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.profileSegmentTypeDetailID) {
                profileSpinner.setSelection(index + 1)
                return
            }
        }

        val subProfileSpinner = binding.spinnerProfileSegment
        for (index in 0 until subProfileSpinner.count - 1) {
            val obj = subProfileSpinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.subProfileTypeDetailID) {
                subProfileSpinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun fillSalaryFormWithCurrentApplicant(currentApplicant: EmploymentApplicantsModel) {

    }

    private fun fillSenpFormWithCurrentApplicant(currentApplicant: EmploymentApplicantsModel) {

    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setUpProfileSegmentDropDown(allMasterDropDown)
            }
        })
        dataBase.provideDataBaseSource().statesDao().getAllStates().observe(viewLifecycleOwner,
                Observer {
                    states = it
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
            when (formSelected) {
                SALARY -> checkSalaryValidation()
                SENP -> checkSenpValidation()
            }
        }
        binding.ivDocumentUpload.setOnClickListener {
            UploadDataActivity.start(mContext, allMasterDropDown.EmploymentType!!)
        }
        setListenerForAge()
        setSalaryPinCodeListener()
        setSenpPinCodeListener()
    }

    private fun checkSenpValidation() {
        if (formValidation.validateSenpEmployment(binding.layoutSenp)) {
            saveCurrentApplicant()
            loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP)
        } else {
            showToast(getString(R.string.mandatory_field_missing))
        }
    }

    private fun checkSalaryValidation() {
        if (formValidation.validateSalaryEmployment(binding.layoutSalary)) {
            saveCurrentApplicant()
            loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP)
        } else {
            showToast(getString(R.string.mandatory_field_missing))
        }
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

    private fun setUpProfileSegmentDropDown(master: AllMasterDropDown) {
        binding.spinnerProfileSegment.adapter = MasterSpinnerAdapter(mContext, master.ProfileSegment!!)
        binding.spinnerProfileSegment?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val profile = parent.selectedItem as DropdownMaster
                    createSubProfileSegmentList(profile.typeDetailID, master.SubProfileSegment)
                }
            }
        }
        if (currentApplicant.profileSegmentTypeDetailID != null) {
            selectProfileAndSubProfile()
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
        fillSalaryForm(currentApplicant)
        getNetIncomeValue()
    }

    private fun fillSalaryForm(currentApplicant: EmploymentApplicantsModel) {
        binding.layoutSalary.etCompanyName.setText(currentApplicant.companyName)
        binding.layoutSalary.etDesignation.setText(currentApplicant.designation)
        binding.layoutSalary.etEmployeeId.setText(currentApplicant.employeeID)
        binding.layoutSalary.etTotalExperience.setText(currentApplicant.totalExperience)
        binding.layoutSalary.etRetirementAge.setText(currentApplicant.retirementAge!!.toString())
        binding.layoutSalary.etOfficialMailId.setText(currentApplicant.officialMailID!!)
        binding.layoutSalary.layoutAddress.etAddress.setText(currentApplicant.addressBean!!.address1)
        binding.layoutSalary.layoutAddress.etLandmark.setText(currentApplicant.addressBean!!.landmark)
        binding.layoutSalary.layoutAddress.etPinCode.setText(currentApplicant.addressBean!!.zip)
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
        fillSenpForm(currentApplicant)
        setUpSenpDropDownValue()
        getAverageMonthlyIncome()
    }

    private fun fillSenpForm(currentApplicant: EmploymentApplicantsModel) {
        binding.layoutSenp.etBusinessName.setText(currentApplicant.companyName)
        binding.layoutSenp.etGstRegistration.setText(currentApplicant.gstRegistration)
        binding.layoutSenp.etBusinessVintage.setText(currentApplicant.businessVinatgeInYear!!.toString())
        binding.layoutSenp.layoutAddress.etAddress.setText(currentApplicant.addressBean!!.address1)
        binding.layoutSenp.layoutAddress.etLandmark.setText(currentApplicant.addressBean!!.landmark)
        binding.layoutSenp.layoutAddress.etPinCode.setText(currentApplicant.addressBean!!.zip)
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
        binding.layoutSenp.spinnerBusinessSetUpType.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.EmploymentType!!)
        binding.layoutSenp.spinnerConstitution.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.Constitution!!)
        binding.layoutSenp.spinnerIndustry.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.Industry!!)
        binding.layoutSenp.layoutAddress.spinnerState.adapter = StatesSpinnerAdapter(mContext, states)
        fillValueInSenpMasterDropDown()
    }

    private fun fillValueInSenpMasterDropDown() {
        selectConstitutionValue(binding.layoutSenp.spinnerConstitution)
        selectIndustryValue(binding.layoutSenp.spinnerConstitution)
        selectBusinessSetupTypeValue(binding.layoutSenp.spinnerConstitution)
    }

    private fun selectConstitutionValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.constitutionTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectBusinessSetupTypeValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.businessSetupTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun setUpSalaryDropDownValue() {
        binding.layoutSalary.spinnerEmploymentType.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.EmploymentType!!)
        binding.layoutSalary.spinnerSector.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.Sector!!)
        binding.layoutSalary.spinnerIndustry.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.Industry!!)
        binding.layoutSalary.layoutAddress.spinnerState.adapter = StatesSpinnerAdapter(mContext, states)
        fillValueInSalaryMasterDropDown()
    }

    private fun fillValueInSalaryMasterDropDown() {
        selectSectorValue(binding.layoutSalary.spinnerSector)
        selectIndustryValue(binding.layoutSalary.spinnerIndustry)
        selectEmploymentTypeValue(binding.layoutSalary.spinnerIndustry)
    }

    private fun selectIndustryValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.industryTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectEmploymentTypeValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.employmentTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectSectorValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == currentApplicant.sectorTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    override val pinCode: String
        get() = pinCodeFromForm

    override fun getPinCodeSuccess(value: Response.ResponsePinCodeDetail, addressType: AppEnums.ADDRESS_TYPE?) {
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
        selectStateValue(binding.layoutSenp.layoutAddress.spinnerState, pinCodeObj.stateID)
    }

    private fun setPinDetailInSalary(pinCodeObj: Response.PinCodeObj) {
        binding.layoutSalary.layoutAddress.etCity.setText(pinCodeObj.cityName)
        salaryCityId = pinCodeObj.cityID
        selectStateValue(binding.layoutSalary.layoutAddress.spinnerState, pinCodeObj.stateID)
    }

    private fun selectStateValue(spinner: Spinner, stateId: Int) {
        for (index in 0 until spinner.count - 1) {
            val state = spinner.getItemAtPosition(index) as StatesMaster
            if (state.stateID == stateId) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun getEmploymentMaster(): EmploymentMaster {
        employmentDraft.applicantDetails = employmentApplicantsList
        employmentMaster?.draftData = employmentDraft
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