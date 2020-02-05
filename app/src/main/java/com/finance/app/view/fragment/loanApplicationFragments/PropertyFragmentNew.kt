package com.finance.app.view.fragment.loanApplicationFragments

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
import com.finance.app.databinding.FragmentPropertyInfoBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.DistrictCityConnector
import com.finance.app.presenter.connector.PinCodeDetailConnector
import com.finance.app.presenter.connector.TransactionCategoryConnector
import com.finance.app.presenter.presenter.CityPresenter
import com.finance.app.presenter.presenter.DistrictPresenter
import com.finance.app.presenter.presenter.PinCodeDetailPresenter
import com.finance.app.presenter.presenter.TransactionCategoryPresenter
import com.finance.app.utility.CurrencyConversion
import com.finance.app.utility.DisablePropertyFields
import com.finance.app.utility.LeadMetaData
import com.finance.app.utility.SetPropertyMandatoryField
import com.finance.app.view.adapters.recycler.spinner.*
import com.finance.app.view.customViews.CustomSpinnerView
import com.google.android.material.textfield.TextInputEditText
import fr.ganfra.materialspinner.MaterialSpinner
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject


class PropertyFragmentNew : BaseFragment(), DistrictCityConnector.District,PinCodeDetailConnector.PinCode, DistrictCityConnector.City, TransactionCategoryConnector.TransactionCategory {

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    private lateinit var binding: FragmentPropertyInfoBinding
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    private lateinit var mContext: Context
    private lateinit var allMasterDropDown: AllMasterDropDown
    private lateinit var states: List<StatesMaster>
    private var mPinCode: String = ""
    private var mStateId: String = ""
    private var mDistrictId: String = ""
    private val districtPresenter = DistrictPresenter(this)
    private val cityPresenter = CityPresenter(this )
    private val transactionCategoryPresenter = TransactionCategoryPresenter(this)
    private val pinCodePresenter = PinCodeDetailPresenter(this)

    private var propertyModel: PropertyModel? = PropertyModel()
    private var mLead: AllLeadMaster? = null
    private lateinit var ownnerShipSpinner: CustomSpinnerView<DropdownMaster>


    private var leadIdForApplicant: String? = ""

    private var pinCodeObj: Response.PinCodeObj? = null
    private var mOwnershipId: String = ""
    private var mTransactionId: String = ""
    private var ownershipID: Int = 0








            companion object {
        fun newInstance(): PropertyFragmentNew {
            return PropertyFragmentNew()
        }
    }

    override fun init() {

    }


     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         ArchitectureApp.instance.component.inject(this)
         mContext = context!!
         mContext = context!!
         mLead = LeadMetaData.getLeadData()
         leadIdForApplicant = mLead?.leadID?.toString()

     }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = initBinding(inflater, container, R.layout.fragment_property_info)
        binding.lifecycleOwner = this
        initilaizeViews()
        setOnClickListeners()
        return view
    }



    fun initilaizeViews(){
        SetPropertyMandatoryField(binding)
        fetchLeadDetails()
        getDropDownsFromDB()


    }

    private fun fetchLeadDetails() {

        LeadMetaData.getLeadObservable().observe(this, androidx.lifecycle.Observer { leadDetail ->
            leadDetail?.let {
                propertyModel = it.propertyData
                showDataOnView(propertyModel)

            }
        })

    }


    private fun getDropDownsFromDB() {

        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues?.let {
                allMasterDropDown = masterDrownDownValues
                setMasterDropDown(allMasterDropDown)
            }
        })

        dataBase.provideDataBaseSource().statesDao().getAllStates().observe(viewLifecycleOwner, Observer { stateMaster ->
            stateMaster?.let {
                states = stateMaster
                setStateDropDown(states)
            }
        })

    }


    private fun showDataOnView(propertyModel: PropertyModel?) {
        binding.cbIsFirstProperty.isChecked = propertyModel!!.isFirstProperty
        binding.etDistanceFromBranch.setText(propertyModel.distanceFromBranch)
        binding.etDistanceFromResidence.setText(propertyModel.distanceFromExistingResidence)
        binding.etPropertyArea.setText(propertyModel.propertyAreaSquareFt.toString())
        binding.etPropertyAddress.setText(propertyModel.propertyAddress)
        binding.etLandmark.setText(propertyModel.landmark)
        binding.etNumOfTenants.setText(propertyModel.numberOfTenants.toString())
        binding.etCashOcr.setText(propertyModel.cashOCRValue.toString())
        binding.etOcr.setText(propertyModel.ocrValue.toString())
        binding.etPinCode.setText(propertyModel.pinCode)
        binding.etMvProperty.setText(propertyModel.mvOfProperty)
        binding.etAgreementValue.setText(propertyModel.agreementValue.toString())


        checkSubmission()
    }


    private fun fillDropDownValue(propertyModel: PropertyModel?) {
        selectMasterDropdownValue(binding.spinnerOwnedProperty, propertyModel!!.alreadyOwnedPropertyTypeDetailID)
        selectMasterDropdownValue(binding.spinnerOwnership, propertyModel!!.ownershipTypeDetailID)
        selectMasterDropdownValue(binding.spinnerUnitType, propertyModel!!.unitTypeTypeDetailID)
        selectMasterDropdownValue(binding.spinnerPropertyNature, propertyModel!!.natureOfPropertyTransactionTypeDetailID)
        selectMasterDropdownValue(binding.spinnerOccupiedBy, propertyModel!!.occupiedByTypeDetailID)
        selectMasterDropdownValue(binding.spinnerTenantNocAvailable, propertyModel!!.tenantNocAvailableTypeDetailID)
        selectPropertyNatureValue(binding.spinnerPropertyNature)




    }

    private fun checkSubmission() {
        if (mLead!!.status == AppEnums.LEAD_TYPE.SUBMITTED.type) {
            DisablePropertyFields(binding)
        }
    }


    private fun setStateDropDown(states: List<StatesMaster>) {
        binding.spinnerState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.spinnerCity.adapter = CitySpinnerAdapter(mContext, ArrayList())
        binding.spinnerDistrict.adapter = DistrictSpinnerAdapter(mContext, ArrayList())
        binding.spinnerState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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


    private fun setMasterDropDown(allMasterDropDown: AllMasterDropDown) {
        binding.spinnerUnitType.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.PropertyUnitType!!)
        binding.spinnerOwnedProperty.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.AlreadyOwnedProperty!!)
        binding.spinnerOccupiedBy.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.PropertyOccupiedBy!!)
        binding.spinnerTenantNocAvailable.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.TenantNocAvailable!!)
        binding.spinnerTransactionCategory.adapter = PropertyNatureSpinnerAdapter(mContext, ArrayList())
        setUpOwnership(binding.spinnerOwnership, allMasterDropDown)
        setUpPropertyNature(binding.spinnerPropertyNature, allMasterDropDown)
        fillDropDownValue(propertyModel)
    }

    private fun setUpPropertyNature(spinner: Spinner, allMasterDropDown: AllMasterDropDown) {
        spinner.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.NatureOfPropertyTransaction!!)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val transaction = parent.selectedItem as DropdownMaster
                    callTransactionCategoryApi(transaction.typeDetailID)
                }
            }
        }
    }

    private fun setUpOwnership(ownershipSpinner: Spinner, allMasterDropDown: AllMasterDropDown) {
        ownershipSpinner.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.PropertyOwnership!!)
        ownershipSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val ownership = parent.selectedItem as DropdownMaster?
                    ownershipID = ownership?.typeDetailID!!
                    //binding.spinnerOwnership.adapter =PropertyNatureSpinnerAdapter(mContext, ArrayList())
                }
            }
        }
    }


    override val ownershipId: String
        get() = mOwnershipId

    override val transactionId: String
        get() = mTransactionId

    private fun callTransactionCategoryApi(transactionId: Int?) {
        mOwnershipId = ownershipID.toString()
        mTransactionId = transactionId.toString()
        transactionCategoryPresenter.callNetwork(ConstantsApi.CALL_TRANSACTON_CATEGORY)
    }

    override fun getTransactionCategoryFailure(msg: String) = showToast(msg)

    override fun getTransactionCategorySuccess(value: Response.ResponsePropertyNature) {
        binding.spinnerTransactionCategory.adapter = PropertyNatureSpinnerAdapter(mContext, value.responseObj)
        selectTransactionCategory(binding.spinnerTransactionCategory)
    }




    private fun selectTransactionCategory(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.TransactionCategoryObj
            if (obj.propertyNatureTransactionCategoryID == propertyModel!!.propertyNatureOfTransactionCategoryTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }



    private fun setOnClickListeners() {
        pinCodeListener(binding.etPinCode)
        binding.btnPrevious.setOnClickListener { AppEvents.fireEventLoanAppChangeNavFragmentPrevious() }
        binding.btnNext.setOnClickListener {
            onSavePropertyData()

        }

    }

    private fun onSavePropertyData() {
        if(formValidation.validateProperty(binding)) {

            val propertyModel = PropertyModel()
            val transactionCategory = binding.spinnerTransactionCategory.selectedItem as Response.TransactionCategoryObj?
            val propertyNature = binding.spinnerPropertyNature.selectedItem as DropdownMaster?
            val state = binding.spinnerState.selectedItem as StatesMaster?
            val district = binding.spinnerDistrict.selectedItem as Response.DistrictObj?
            val city = binding.spinnerCity.selectedItem as Response.CityObj?
            val unitType = binding.spinnerUnitType.selectedItem as DropdownMaster?
            val ownership = binding.spinnerOwnership.selectedItem as DropdownMaster?
            val ownedProperty = binding.spinnerOwnedProperty.selectedItem as DropdownMaster?
            val occupiedBy = binding.spinnerOccupiedBy.selectedItem as DropdownMaster?
            val tenantNoc = binding.spinnerTenantNocAvailable.selectedItem as DropdownMaster?

            propertyModel.leadID = (leadIdForApplicant?.toInt() ?: 0)
            propertyModel.cityID = city?.cityID
            propertyModel.districtID = district?.districtID
            propertyModel.stateID = state?.stateID
            propertyModel.occupiedByTypeDetailID = occupiedBy?.typeDetailID
            propertyModel.natureOfPropertyTransactionTypeDetailID = propertyNature?.typeDetailID
            propertyModel.propertyNatureOfTransactionCategoryTypeDetailID = transactionCategory?.propertyNatureTransactionCategoryID
            propertyModel.unitTypeTypeDetailID = unitType?.typeDetailID
            propertyModel.alreadyOwnedPropertyTypeDetailID = ownedProperty?.typeDetailID
            propertyModel.ownershipTypeDetailID = ownership?.typeDetailID
            propertyModel.propertyAreaSquareFt = binding.etPropertyArea.text.toString().toInt()
            propertyModel.propertyAddress = binding.etPropertyArea.text.toString()
            propertyModel.landmark = binding.etLandmark.text.toString()
            propertyModel.pinCode = binding.etPinCode.text.toString()
            propertyModel.distanceFromBranch = binding.etDistanceFromBranch.text.toString()
            propertyModel.numberOfTenants = binding.etNumOfTenants.text.toString().toInt()
            propertyModel.cashOCRValue = CurrencyConversion().convertToNormalValue(binding.etCashOcr.text.toString()).toDouble()
            propertyModel.ocrValue = CurrencyConversion().convertToNormalValue(binding.etOcr.text.toString()).toDouble()
            propertyModel.tenantNocAvailableTypeDetailID = tenantNoc?.typeDetailID
            propertyModel.mvOfProperty = CurrencyConversion().convertToNormalValue(binding.etMvProperty.text.toString())
            propertyModel.agreementValue = CurrencyConversion().convertToNormalValue(binding.etAgreementValue.text.toString()).toDouble()
            propertyModel.leadApplicantNumber = leadIdForApplicant ?: ""
            propertyModel.isFirstProperty = binding.cbIsFirstProperty.isChecked
            propertyModel.distanceFromExistingResidence = binding.etDistanceFromResidence.text.toString()


//save data in database
            LeadMetaData().savePropertyData(propertyModel)
            // after save data go to next page
            AppEvents.fireEventLoanAppChangeNavFragmentNext()

        }else{
            showToast(getString(R.string.validation_error))
        }


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

    override val stateId: String
        get()=mStateId


    override fun getDistrictFailure(msg: String) = showToast(msg)

    override fun getDistrictSuccess(value: Response.ResponseDistrict, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj != null && value.responseObj.size > 0) {
            setDistrict(binding.spinnerDistrict, value)
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

    override val pinCode: String
        get() = mPinCode

    override fun getPinCodeFailure(msg: String) = clearPinCodeData()

    override fun getPinCodeSuccess(value: Response.ResponsePinCodeDetail, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj!!.size > 0) {
            pinCodeObj = value.responseObj[0]
            selectStateValue(binding.spinnerState)
        } else {
            clearPinCodeData()
        }
    }

    private fun clearPinCodeData() {
        binding.spinnerState.isEnabled = true
        binding.spinnerDistrict.isEnabled = true
        binding.spinnerCity.isEnabled = true
        binding.spinnerState.adapter = StatesSpinnerAdapter(mContext, states)
        binding.spinnerDistrict.adapter = DistrictSpinnerAdapter(mContext, ArrayList())
        binding.spinnerCity.adapter = CitySpinnerAdapter(mContext, ArrayList())
    }



    private fun selectStateValue(spinner: Spinner) {
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



    override val districtId: String
        get() = mDistrictId

    override fun getCitySuccess(value: Response.ResponseCity, addressType: AppEnums.ADDRESS_TYPE?) {
        if (value.responseObj != null && value.responseObj.size > 0) {
            setCityValue(binding.spinnerCity, value.responseObj)
        }
    }

    override fun getCityFailure(msg: String) = showToast(msg)


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




    private fun selectMasterDropdownValue(spinner: Spinner, id: Int?) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == id) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun selectPropertyNatureValue(spinner: Spinner) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == propertyModel!!.natureOfPropertyTransactionTypeDetailID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }


}
