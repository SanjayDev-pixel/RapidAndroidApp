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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentEmploymentBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.DistrictCityConnector
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.connector.PinCodeDetailConnector
import com.finance.app.presenter.presenter.*
import com.finance.app.utility.*
import com.finance.app.view.adapters.recycler.Spinner.CitySpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.DistrictSpinnerAdapter
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

class EmploymentInfoFragment : BaseFragment(), LoanApplicationConnector.PostLoanApp,
        LoanApplicationConnector.GetLoanApp, PinCodeDetailConnector.PinCode,
        ApplicantsAdapter.ItemClickListener, DistrictCityConnector.District,
        DistrictCityConnector.City {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentEmploymentBinding
    private lateinit var mContext: Context
    private var mLead: AllLeadMaster? = null
    private lateinit var allMasterDropDown: AllMasterDropDown
    private val loanAppPostPresenter = LoanAppPostPresenter(this)
    private val loanAppGetPresenter = LoanAppGetPresenter(this)
    private val pinCodePresenter = PinCodeDetailPresenter(this)
    private val districtPresenter = DistrictPresenter(this)
    private val cityPresenter = CityPresenter(this)
    private var applicantAdapter: ApplicantsAdapter? = null
    private var employmentMaster: EmploymentMaster = EmploymentMaster()
    private var eDraftData = EmploymentApplicantList()
    private var eApplicantList: ArrayList<EmploymentApplicantsModel>? = ArrayList()
    private var currentApplicant: EmploymentApplicantsModel = EmploymentApplicantsModel()
    private var mPinCode: String = ""
    private var currentPosition = 0
    private var mStateId: String = ""
    private var mDistrictId: String = ""
    private var pinCodeObj: Response.PinCodeObj? = null

    companion object {
        private val responseConversion = ResponseConversion()
        private val requestConversion = RequestConversion()
        private lateinit var applicantTab: ArrayList<String>
        private lateinit var states: List<StatesMaster>

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_employment)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        SetEmploymentMandatoryField(binding)
        mContext = context!!
        getEmploymentInfo()
        setDatePicker()
        setClickListeners()
    }

    private fun getEmploymentInfo() {
        mLead = sharedPreferences.getLeadDetail()
        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
    }

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override val storageType: String
        get() = employmentMaster.storageType

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        value.responseObj?.let {
            employmentMaster = responseConversion.toEmploymentMaster(value.responseObj)
            eDraftData = employmentMaster.draftData
            eApplicantList = eDraftData.applicantDetails
        }
        setCoApplicants(eApplicantList)
        showData(eApplicantList)
    }

    private fun showData(applicantList: ArrayList<EmploymentApplicantsModel>?) {
        if (applicantList != null) {
            for (applicant in applicantList) {
                if (applicant.isMainApplicant) {
                    currentApplicant = applicant
                }
            }
        }
//        fillFormWithCurrentApplicant(currentApplicant)
        getDropDownsFromDB()
    }

    private fun setCoApplicants(applicants: ArrayList<EmploymentApplicantsModel>?) {
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

    override fun getLoanAppGetFailure(msg: String) = getDataFromDB()

    private fun setDatePicker() {
        binding.layoutSalary.etJoiningDate.setOnClickListener {
            SelectDate(binding.layoutSalary.etJoiningDate, mContext)
        }

        binding.layoutSenp.etIncorporationDate.setOnClickListener {
            DateDifference(mContext, binding.layoutSenp.etIncorporationDate, binding.layoutSenp.etBusinessVintage)
        }
    }

    override fun onApplicantClick(position: Int) {
        if (formValidation.validateSalaryEmployment(binding.layoutSalary)) {
            saveCurrentApplicant()
            ClearEmploymentForm(binding, mContext, allMasterDropDown, states)
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
        currentApplicant = if (position >= eApplicantList!!.size) {
            EmploymentApplicantsModel()
        } else {
            eApplicantList!![position]
        }
//        fillFormWithCurrentApplicant(currentApplicant)
    }

    private fun setClickListeners() {
        binding.btnSaveAndContinue.setOnClickListener {}
        binding.ivDocumentUpload.setOnClickListener {}
        pinCodeListener(binding.layoutSenp.layoutAddress.etPinCode, AppEnums.ADDRESS_TYPE.SENP)
        pinCodeListener(binding.layoutSalary.layoutAddress.etPinCode, AppEnums.ADDRESS_TYPE.SALARY)
    }

    private fun pinCodeListener(pinCodeField: TextInputEditText?, formType: AppEnums.ADDRESS_TYPE? = null) {
        pinCodeField!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (pinCodeField.text!!.length == 6) {
                    mPinCode = pinCodeField.text.toString()
                    pinCodePresenter.callPinCodeDetailApi(addressType = formType)
                }
            }
        })
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
            setStateDropDownValue()
        })
    }

    private fun setStateDropDownValue() {
        setDropDownForState(binding.layoutSalary.layoutAddress.spinnerState, AppEnums.ADDRESS_TYPE.SALARY)
        setDropDownForState(binding.layoutSenp.layoutAddress.spinnerState, AppEnums.ADDRESS_TYPE.SENP)
    }

    private fun setDropDownForState(spinner: MaterialSpinner, type: AppEnums.ADDRESS_TYPE) {
        spinner.adapter = StatesSpinnerAdapter(mContext, states)
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

    private fun setMasterDropDownValue(allMasterDropDown: AllMasterDropDown?) {

    }

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().employmentDao().getEmployment(leadId).observe(this, Observer { employmentInfo ->
            employmentInfo?.let {
                employmentMaster = employmentInfo
                eDraftData = employmentMaster.draftData
                eApplicantList = eDraftData.applicantDetails
                if (eApplicantList!!.size < 0) {
                    eApplicantList!!.add(EmploymentApplicantsModel())
                }
            }
            setCoApplicants(eApplicantList)
            showData(eApplicantList)
        })
    }

    override val pinCode: String
        get() = mPinCode

    override fun getPinCodeSuccess(value: Response.ResponsePinCodeDetail, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj!!.size > 0) {
            pinCodeObj = value.responseObj[0]
            when (addressType) {
                AppEnums.ADDRESS_TYPE.SENP -> selectStateValue(binding.layoutSenp.layoutAddress.spinnerState, AppEnums.ADDRESS_TYPE.SENP)
                AppEnums.ADDRESS_TYPE.SALARY -> selectStateValue(binding.layoutSalary.layoutAddress.spinnerState, AppEnums.ADDRESS_TYPE.SALARY)
            }
        } else {
            clearPinCodes(addressType?.type)
        }
    }

    private fun selectStateValue(spinner: MaterialSpinner, type: AppEnums.ADDRESS_TYPE) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as StatesMaster
            if (obj.stateID == pinCodeObj!!.stateID) {
                spinner.setSelection(index + 1)
                mStateId = pinCodeObj!!.stateID.toString()
                districtPresenter.callDistrictApi(addressType = type)
                spinner.isEnabled = false
                return
            }
        }
    }

    private fun clearPinCodes(formType: String? = null) {
        when (formType) {
            AppEnums.ADDRESS_TYPE.SALARY.type -> clearSalaryPinCodeField()
            AppEnums.ADDRESS_TYPE.SENP.type -> clearSenpPinCodeField()
        }
    }

    private fun clearSalaryPinCodeField() {
        binding.layoutSalary.layoutAddress.spinnerState.isEnabled = true
        binding.layoutSalary.layoutAddress.spinnerDistrict.isEnabled = true
        binding.layoutSalary.layoutAddress.spinnerCity.isEnabled = true
        binding.layoutSalary.layoutAddress.spinnerState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.layoutSalary.layoutAddress.spinnerDistrict.adapter = DistrictSpinnerAdapter(mContext, ArrayList())
        binding.layoutSalary.layoutAddress.spinnerCity.adapter = CitySpinnerAdapter(mContext, ArrayList())
    }

    private fun clearSenpPinCodeField() {
        binding.layoutSenp.layoutAddress.spinnerState.isEnabled = true
        binding.layoutSenp.layoutAddress.spinnerDistrict.isEnabled = true
        binding.layoutSenp.layoutAddress.spinnerCity.isEnabled = true
        binding.layoutSenp.layoutAddress.spinnerState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.layoutSenp.layoutAddress.spinnerDistrict.adapter = DistrictSpinnerAdapter(mContext, ArrayList())
        binding.layoutSenp.layoutAddress.spinnerCity.adapter = CitySpinnerAdapter(mContext, ArrayList())
    }

    override fun getPinCodeFailure(msg: String) = clearPinCodes()

    override val stateId: String
        get() = mStateId

    override fun getDistrictSuccess(value: Response.ResponseDistrict, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj != null && value.responseObj.size > 0) {
            when (addressType) {
                AppEnums.ADDRESS_TYPE.SENP -> setDistrict(binding.layoutSenp.layoutAddress.spinnerDistrict, value, AppEnums.ADDRESS_TYPE.SENP)
                AppEnums.ADDRESS_TYPE.SALARY -> setDistrict(binding.layoutSalary.layoutAddress.spinnerDistrict, value, AppEnums.ADDRESS_TYPE.SALARY)
            }
        }
    }

    private fun setDistrict(spinner: MaterialSpinner, response: Response.ResponseDistrict, type: AppEnums.ADDRESS_TYPE) {
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
        selectDistrictValue(spinner, type)
    }

    private fun selectDistrictValue(spinner: Spinner, type: AppEnums.ADDRESS_TYPE) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.DistrictObj
            if (obj.districtID == pinCodeObj!!.districtID) {
                spinner.setSelection(index + 1)
                mDistrictId = obj.districtID.toString()
                cityPresenter.callCityApi(addressType = type)
                spinner.isEnabled = false
                return
            }
        }
    }

    override fun getDistrictFailure(msg: String) = showToast(msg)

    override val districtId: String
        get() = mDistrictId

    override fun getCitySuccess(value: Response.ResponseCity, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj != null && value.responseObj.size > 0) {
            when (addressType) {
                AppEnums.ADDRESS_TYPE.SENP -> setCityValue(binding.layoutSenp.layoutAddress.spinnerCity, value.responseObj)
                AppEnums.ADDRESS_TYPE.SALARY -> setCityValue(binding.layoutSalary.layoutAddress.spinnerCity, value.responseObj)
            }
        }
    }

    private fun setCityValue(spinner: MaterialSpinner, responseObj: ArrayList<Response.CityObj>) {
        spinner.adapter = CitySpinnerAdapter(mContext, responseObj)
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.CityObj
            if (obj.cityID == pinCodeObj!!.cityID) {
                spinner.setSelection(index + 1)
                spinner.isEnabled = false
                return
            }
        }
    }

    override fun getCityFailure(msg: String) = showToast(msg)

    private fun getCurrentApplicant(): EmploymentApplicantsModel {
        val currentApplicant = EmploymentApplicantsModel()

        return currentApplicant
    }

    private fun saveCurrentApplicant() {
        if (eApplicantList!!.size > 0) {
            eApplicantList!![currentPosition] = getCurrentApplicant()
        } else eApplicantList!!.add(currentPosition, getCurrentApplicant())
    }

    private fun getEmploymentMaster(): EmploymentMaster {
        eDraftData.applicantDetails = eApplicantList
        employmentMaster.draftData = eDraftData
        employmentMaster.leadID = leadId.toInt()
        return employmentMaster
    }

    override val loanAppRequestPost: LoanApplicationRequest
        get() = requestConversion.employmentRequest(getEmploymentMaster())

    override fun getLoanAppPostSuccess(value: Response.ResponseGetLoanApplication) {
        saveDataToDB(getEmploymentMaster())
        gotoNextFragment()
    }

    override fun getLoanAppPostFailure(msg: String) {
        saveDataToDB(getEmploymentMaster())
        showToast(msg)
    }

    private fun saveDataToDB(employment: EmploymentMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().employmentDao().insertEmployment(employment)
        }
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, BankDetailFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }

}