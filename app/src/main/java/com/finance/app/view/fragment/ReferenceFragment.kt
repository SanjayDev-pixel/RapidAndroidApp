package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.FragmentReferenceBinding
import com.finance.app.databinding.LayoutEmploymentAddressBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.DistrictCityConnector
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.connector.PinCodeDetailConnector
import com.finance.app.presenter.presenter.*
import com.finance.app.utility.ClearReferenceForm
import com.finance.app.utility.RequestConversion
import com.finance.app.utility.ResponseConversion
import com.finance.app.utility.SetReferenceMandatoryField
import com.finance.app.view.adapters.recycler.Spinner.CitySpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.DistrictSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.StatesSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ReferenceAdapter
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

class ReferenceFragment : BaseFragment(),LoanApplicationConnector.PostLoanApp,
        LoanApplicationConnector.GetLoanApp,PinCodeDetailConnector.PinCode,
        ReferenceAdapter.ItemClickListener, DistrictCityConnector.District,
        DistrictCityConnector.City {

    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var binding: FragmentReferenceBinding
    private lateinit var mContext: Context
    private var mLead: AllLeadMaster? = null
    private val loanAppPostPresenter = LoanAppPostPresenter(this)
    private val loanAppGetPresenter = LoanAppGetPresenter(this)
    private val pinCodePresenter = PinCodeDetailPresenter(this)
    private val districtPresenter = DistrictPresenter(this)
    private val cityPresenter = CityPresenter(this)
    private lateinit var allMasterDropDown: AllMasterDropDown
    private lateinit var states: List<StatesMaster>
    private var referenceMaster: ReferenceMaster = ReferenceMaster()
    private var rDraftData = ReferencesList()
    private var referencesList: ArrayList<ReferenceModel>? = ArrayList()
    private var currentReference: ReferenceModel = ReferenceModel()
    private var rAddressDetail: ReferenceAddressDetail = ReferenceAddressDetail()
    private var pinCodeObj: Response.PinCodeObj? = null
    private var mPinCode: String = ""
    private var mStateId: String = ""
    private var mDistrictId: String = ""

    companion object {
        private val responseConversion = ResponseConversion()
        private val requestConversion = RequestConversion()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_reference)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        mContext = context!!
        SetReferenceMandatoryField(binding)
        getReferenceInfo()
        setClickListeners()
    }

    private fun getReferenceInfo() {
        mLead = sharedPreferences.getLeadDetail()
        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
    }

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override val storageType: String
        get() = referenceMaster.storageType

    override fun getLoanAppGetFailure(msg: String) = getDataFromDB()

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().referenceDao().getReference(leadId).observe(this, Observer { referenceInfo ->
            referenceInfo?.let {
                referenceMaster = referenceInfo
                rDraftData = referenceMaster.draftData
                referencesList = rDraftData.referenceDetails
                if (referencesList!!.size < 0) {
                    referencesList!!.add(ReferenceModel())
                }
            }
            showData(referencesList)
        })
    }

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        value.responseObj?.let {
            referenceMaster = responseConversion.toReferenceMaster(value.responseObj)
            rDraftData = referenceMaster.draftData
            referencesList = rDraftData.referenceDetails
        }
        showData(referencesList)
    }

    private fun showData(referenceList: ArrayList<ReferenceModel>?) {
        if (referenceList != null && referenceList.size > 0) {
            currentReference = referenceList[0]
            rAddressDetail = currentReference.addressBean!!
        }
        getDropDownsFromDB()
        fillFormWithCurrentApplicant(currentReference)
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

    private fun fillFormWithCurrentApplicant(currentReference: ReferenceModel) {
        binding.etName.setText(currentReference.name)
        binding.etKnownSince.setText(currentReference.knowSince)
        binding.referenceAddressLayout.etContactNum.setText(currentReference.contactNumber)
        fillAddressFields(binding.referenceAddressLayout, rAddressDetail)
    }

    private fun fillAddressFields(binding: LayoutEmploymentAddressBinding, address: ReferenceAddressDetail?) {
        binding.etAddress.setText(address!!.address1)
        binding.etLandmark.setText(address.landmark)
        binding.etPinCode.setText(address.zip)
    }

    private fun setMasterDropDownValue(allMasterDropDown: AllMasterDropDown) {
        binding.spinnerRelation.adapter = MasterSpinnerAdapter(context!!, allMasterDropDown.ReferenceRelationship!!)
        binding.spinnerOccupation.adapter = MasterSpinnerAdapter(context!!, allMasterDropDown.OccupationType!!)
        selectMasterDropdownValue(binding.spinnerRelation, currentReference.relationTypeDetailID)
        selectMasterDropdownValue(binding.spinnerOccupation, currentReference.occupationTypeDetailID)
    }

    private fun selectMasterDropdownValue(spinner: Spinner, id: Int?) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == id) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun setStateDropDownValue() {
        binding.referenceAddressLayout.spinnerState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.referenceAddressLayout.spinnerCity.adapter = CitySpinnerAdapter(mContext, ArrayList())
        binding.referenceAddressLayout.spinnerDistrict.adapter = DistrictSpinnerAdapter(mContext, ArrayList())
        binding.referenceAddressLayout.spinnerState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.referenceAddressLayout.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val state = parent.selectedItem as StatesMaster
                    mStateId = state.stateID.toString()
                    districtPresenter.callDistrictApi()
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.btnAddReference.setOnClickListener {
            if (formValidation.validateReference(binding = binding)) {
                referencesList?.add(getReferenceModel())
                ClearReferenceForm(binding, mContext, allMasterDropDown, states)
            }
        }
        binding.btnSaveAndContinue.setOnClickListener {
            if (formValidation.validateReference(binding = binding)) {
                referencesList?.add(getReferenceModel())
                loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP)
            }
        }
        pinCodeListener(binding.referenceAddressLayout.etPinCode)
    }

    private fun pinCodeListener(pinCodeField: TextInputEditText?) {
        pinCodeField!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (pinCodeField.text!!.length == 6) {
                    mPinCode = pinCodeField.text.toString()
                    pinCodePresenter.callPinCodeDetailApi()
                }
            }
        })
    }

    override val pinCode: String
        get() = mPinCode

    override fun getPinCodeFailure(msg: String) = clearPinCodeData()

    private fun clearPinCodeData() {
        binding.referenceAddressLayout.spinnerState.isEnabled = true
        binding.referenceAddressLayout.spinnerDistrict.isEnabled = true
        binding.referenceAddressLayout.spinnerCity.isEnabled = true
    }

    override fun getPinCodeSuccess(value: Response.ResponsePinCodeDetail, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj!!.size > 0) {
            pinCodeObj = value.responseObj[0]
            selectStateValue(binding.referenceAddressLayout.spinnerState)
        } else {
            clearPinCodeData()
        }
    }

    private fun selectStateValue(spinner: MaterialSpinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as StatesMaster
            if (obj.stateID == pinCodeObj!!.stateID) {
                spinner.setSelection(index + 1)
                mStateId = pinCodeObj!!.stateID.toString()
                districtPresenter.callDistrictApi()
                spinner.isEnabled = false
                return
            }
        }
    }

    override val stateId: String
        get() = mStateId

    override fun getDistrictFailure(msg: String) = showToast(msg)

    override fun getDistrictSuccess(value: Response.ResponseDistrict, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj != null && value.responseObj.size > 0) {
            setDistrict(binding.referenceAddressLayout.spinnerDistrict, value)
        }
    }

    private fun setDistrict(spinner: MaterialSpinner, response: Response.ResponseDistrict) {
        spinner.adapter = DistrictSpinnerAdapter(mContext, response.responseObj!!)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val district = parent.selectedItem as Response.DistrictObj
                    mDistrictId = district.districtID.toString()
                    cityPresenter.callCityApi()
                }
            }
        }
        selectDistrictValue(spinner)
    }

    private fun selectDistrictValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.DistrictObj
            if (obj.districtID == pinCodeObj!!.districtID) {
                spinner.setSelection(index + 1)
                mDistrictId = obj.districtID.toString()
                cityPresenter.callCityApi()
                spinner.isEnabled = false
                return
            }
        }
    }

    override val districtId: String
        get() = mDistrictId

    override fun getCityFailure(msg: String) = showToast(msg)

    override fun getCitySuccess(value: Response.ResponseCity, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj != null && value.responseObj.size > 0) {
            setCityValue(binding.referenceAddressLayout.spinnerCity, value.responseObj)
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

    private fun getReferenceModel(): ReferenceModel {
        val referenceModel = ReferenceModel()
        val relation = binding.spinnerRelation.selectedItem as DropdownMaster?
        val occupation = binding.spinnerOccupation.selectedItem as DropdownMaster?
        val cCity = binding.referenceAddressLayout.spinnerCity.selectedItem as Response.CityObj?
        val cDistrict = binding.referenceAddressLayout.spinnerDistrict.selectedItem as Response.DistrictObj?

        referenceModel.addressBean?.cityID = cCity?.cityID
        referenceModel.addressBean?.districtID = cDistrict?.districtID
        referenceModel.addressBean?.address1 = binding.referenceAddressLayout.etAddress.text.toString()
        referenceModel.addressBean?.landmark = binding.referenceAddressLayout.etLandmark.text.toString()
        referenceModel.addressBean?.zip = binding.referenceAddressLayout.etPinCode.text.toString()
        referenceModel.name = binding.etName.text.toString()
        referenceModel.contactNumber = binding.referenceAddressLayout.etContactNum.text.toString()
        referenceModel.knowSince = binding.etKnownSince.text.toString()
        referenceModel.relationTypeDetailID = relation?.typeDetailID
        referenceModel.occupationTypeDetailID = occupation?.typeDetailID
        referencesList?.add(referenceModel)
        return referenceModel
    }

    override fun onDeleteClicked(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEditClicked(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getReferenceMaster(): ReferenceMaster {
        rDraftData.referenceDetails = referencesList
        referenceMaster.draftData = rDraftData
        referenceMaster.leadID = leadId.toInt()
        return referenceMaster
    }

    override val loanAppRequestPost: LoanApplicationRequest
        get() = requestConversion.referenceRequest(getReferenceMaster())

    override fun getLoanAppPostFailure(msg: String) {
        saveDataToDB(getReferenceMaster())
        showToast(msg)
    }

    override fun getLoanAppPostSuccess(value: Response.ResponseGetLoanApplication) {
        saveDataToDB(getReferenceMaster())
        gotoNextFragment()
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, DocumentCheckListFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }

    private fun saveDataToDB(reference: ReferenceMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().referenceDao().insertReference(reference)
        }
    }
}