package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentReferenceBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.connector.PinCodeDetailConnector
import com.finance.app.presenter.presenter.PinCodeDetailPresenter
import com.finance.app.presenter.presenter.UpdateReferencePresenter
import com.finance.app.utility.ClearReferenceForm
import com.finance.app.view.adapters.recycler.adapter.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ReferenceAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class ReferenceFragment : BaseFragment(), LoanApplicationConnector.UpdateReference,
        PinCodeDetailConnector.PinCode, ReferenceAdapter.ItemClickListener {

    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var allMasterDropDown: AllMasterDropDown
    private lateinit var binding: FragmentReferenceBinding
    private lateinit var mContext: Context
    private lateinit var relation: ArrayList<DropdownMaster>
    private lateinit var occupation: ArrayList<DropdownMaster>
    private var referenceAdapter: ReferenceAdapter? = null
    private val referencePresenter = UpdateReferencePresenter(this)
    private val pinCodePresenter = PinCodeDetailPresenter(this)
    private var districtId = 0
    private var cityId = 0

    companion object {
        var updateReferenceList: ArrayList<Requests.RequestUpdateReference> = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_reference)
        init()
        return binding.root
    }

    override fun init() {
        mContext = context!!
        relation = ArrayList()
        occupation = ArrayList()
        ArchitectureApp.instance.component.inject(this)
        getDropDownsFromDB()
        setClickListeners()
    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setMasterDropDownValue(allMasterDropDown)
            }
        })
    }

    private fun setMasterDropDownValue(allMasterDropDown: AllMasterDropDown) {
        relation = allMasterDropDown.Relationship!!
        occupation = allMasterDropDown.OccupationType!!
        binding.spinnerRelation.adapter = MasterSpinnerAdapter(context!!, relation)
        binding.spinnerOccupation.adapter = MasterSpinnerAdapter(context!!, occupation)
    }

    private fun setClickListeners() {
        binding.btnAddReference.setOnClickListener {
            if (formValidation.validateReference(binding = binding)) {
                updateReferenceList.add(updateReference)
                showReferenceDetail()
                ClearReferenceForm(binding)
            }
        }
        binding.btnSaveAndContinue.setOnClickListener {
            if (formValidation.validateReference(binding = binding)) {
                referencePresenter.callNetwork(ConstantsApi.CALL_UPDATE_REFERENCE)
            }
        }
        binding.referenceAddressLayout.etPinCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (binding.referenceAddressLayout.etPinCode.text!!.length == 6) {
                    pinCodePresenter.callNetwork(ConstantsApi.CALL_PIN_CODE_DETAIL)
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun showReferenceDetail() {
        binding.rcReference.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        referenceAdapter = ReferenceAdapter(activity!!, updateReferenceList)
        binding.rcReference.adapter = referenceAdapter
        referenceAdapter!!.setOnItemClickListener(this)
        binding.rcReference.visibility = View.VISIBLE
    }

    override val pinCode: String
        get() = binding.referenceAddressLayout.etPinCode.text.toString()

    override fun getPinCodeSuccess(value: Response.ResponsePinCodeDetail) {
        setPinCodeDetails(value.responseObj[0])
    }

    private fun setPinCodeDetails(pinCodeObjDetail: Response.PinCodeObj) {
        binding.referenceAddressLayout.etDistrict.setText(pinCodeObjDetail.districtName)
        districtId = pinCodeObjDetail.districtID
        binding.referenceAddressLayout.etCity.setText(pinCodeObjDetail.cityName)
        cityId = pinCodeObjDetail.cityID
        binding.referenceAddressLayout.etState.setText(pinCodeObjDetail.stateName)
    }

    override fun getPinCodeFailure(msg: String) {
        showToast(getString(R.string.failure_pin_code_api))
    }

    override val leadId: String
        get() = "1"

    override val requestUpdateReference: ArrayList<Requests.RequestUpdateReference>
        get() = updateReferenceList

    private val updateReference: Requests.RequestUpdateReference
        get() {
            val relation = binding.spinnerRelation.selectedItem as DropdownMaster?
            val occupation = binding.spinnerOccupation.selectedItem as DropdownMaster?
            return Requests.RequestUpdateReference(name = binding.etName.text.toString(),
                    contactNumber = binding.etContactNum.text.toString(), addressBean = addressBean,
                    knowSince = binding.etKnownSince.text.toString(),
                    applicantID = null, occupationTypeDetailID = occupation?.typeDetailID,
                    relationTypeDetailID = relation?.typeDetailID)
        }

    private val addressBean: Requests.AddressBean
        get() {
            return Requests.AddressBean(districtID = districtId, cityID = cityId,
                    address1 = binding.referenceAddressLayout.etAddress1.text.toString(),
                    address2 = binding.referenceAddressLayout.etAddress2.text.toString(),
                    landmark = binding.referenceAddressLayout.etLandmark.text.toString(),
                    cityName = binding.referenceAddressLayout.etCity.text.toString(),
                    zip = binding.referenceAddressLayout.etPinCode.text.toString())
        }

    override fun getUpdateReferenceSuccess(value: Response.ResponseUpdateReference) {
        gotoNextFragment()
    }

    override fun getUpdateReferenceFailure(msg: String) {
        showToast(msg)
    }

    override fun onDeleteClicked(position: Int) {
        updateReferenceList.removeAt(position)
        referenceAdapter!!.notifyDataSetChanged()
    }

    override fun onEditClicked(position: Int) {
        getReferenceDetailAtPosition(position)
        updateReferenceList.removeAt(position)
        referenceAdapter!!.notifyDataSetChanged()
    }

    private fun getReferenceDetailAtPosition(position: Int) {
        val referenceDetail = updateReferenceList[position]
        fillForm(referenceDetail)
    }

    private fun fillForm(referenceDetail: Requests.RequestUpdateReference) {
        binding.etName.setText(referenceDetail.name)
        binding.etKnownSince.setText(referenceDetail.knowSince)
        binding.etContactNum.setText(referenceDetail.contactNumber)
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, PropertyFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }
}