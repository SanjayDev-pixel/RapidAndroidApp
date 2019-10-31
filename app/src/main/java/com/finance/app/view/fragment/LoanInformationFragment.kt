package com.finance.app.view.fragment
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.FragmentLoanInformationBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.persistence.model.LoanInfoMaster
import com.finance.app.persistence.model.LoanProductMaster
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.presenter.LoanInfoGetPresenter
import com.finance.app.presenter.presenter.LoanInfoPostPresenter
import com.finance.app.presenter.presenter.SourceChannelPartnerNamePresenter
import com.finance.app.utility.ShowAsMandatory
import com.finance.app.utility.UploadData
import com.finance.app.view.activity.UploadedFormDataActivity
import com.finance.app.view.adapters.recycler.adapter.ChannelPartnerNameSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.LoanProductSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.LoanPurposeSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.MasterSpinnerAdapter
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

class LoanInformationFragment : BaseFragment(), LoanApplicationConnector.PostLoanInfo,
        LoanApplicationConnector.GetLoanInfo, LoanApplicationConnector.SourceChannelPartnerName {

    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var binding: FragmentLoanInformationBinding
    private val frag: Fragment = this
    private lateinit var loanScheme: ArrayList<DropdownMaster>
    private lateinit var interestType: ArrayList<DropdownMaster>
    private lateinit var channelPartnerName: ArrayList<Response.ChannelPartnerName>
    private lateinit var allMasterDropDown: AllMasterDropDown
    private lateinit var sourcingChannelPartner: ArrayList<DropdownMaster>
    private var loanProducts: List<LoanProductMaster> = ArrayList()
    private var loanInfo: Requests.LoanInfoObj? = null
    private var selectedLoanProduct: LoanProductMaster? = null
    private val sourcePartnerPresenter = SourceChannelPartnerNamePresenter(this)
    private val loanInfoGetPresenter = LoanInfoGetPresenter(this)
    private val loanInfoPostPresenter = LoanInfoPostPresenter(this)
    private var mContext: Context? = null
    private var mLeadId: String? = null
    private var empId: String? = null
    private var loanMaster: LoanInfoMaster? = LoanInfoMaster()
    companion object {
        private lateinit var mBranchId: String
        private lateinit var mEmployeeId: String
        private lateinit var mChannelTypeId: String
        private const val isMandatory = true
        private const val SELECT_PDF_CODE = 1
        private const val CLICK_IMAGE_CODE = 2
        private const val SELECT_IMAGE_CODE = 3
        private var image: Bitmap? = null
        private var pdf: Uri? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_loan_information)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        mContext = context!!
        getLoanInfo()
        setMandatoryField()
        setClickListeners()
    }

    private fun getLoanInfo() {
        mLeadId = sharedPreferences.getLeadId()
        empId = sharedPreferences.getUserId()
        loanInfoGetPresenter.callNetwork(ConstantsApi.CALL_LOAN_INFO_GET)
    }

    override val leadId: String
        get() = mLeadId!!

    override fun getLoanInfoGetSuccess(value: Response.ResponseGetLoanInfo) {
        value.responseObj?.let {
            saveDataToDB(value.responseObj)
            waitForData()
        }
    }

    private fun waitForData() {
        val progress = ProgressDialog(mContext)
        progress.setMessage("Fetching Data")
        progress.setCancelable(false)
        progress.show()
        val handler = Handler()
        handler.postDelayed({
            getDropDownsFromDB()
            getDataFromDB()
            progress.dismiss()
        }, 2000)
    }

    override fun getLoanInfoGetFailure(msg: String) = waitForData()

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setMasterDropDownValue(allMasterDropDown)
            }
        })

        dataBase.provideDataBaseSource().loanProductDao().getAllLoanProduct().observe(viewLifecycleOwner, Observer { loanProductValue ->
            loanProductValue.let {
                loanProducts = loanProductValue
                setProductDropDownValue(loanProducts)
            }
        })
    }

    private fun setProductDropDownValue(products: List<LoanProductMaster>) {
        loanProducts = products
        binding.spinnerLoanProduct.adapter = LoanProductSpinnerAdapter(mContext!!, loanProducts)
        binding.spinnerLoanProduct?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position>=0){
                    selectedLoanProduct = parent.selectedItem as LoanProductMaster
                    setLoanPurposeDropdown(position)
                }
            }
        }

        if (loanInfo != null) {
            selectProductValue(binding.spinnerLoanProduct, loanInfo!!)
        }
    }

    private fun selectProductValue(spinner: Spinner, value: Requests.LoanInfoObj) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as LoanProductMaster
            if (obj.productID == value.loanProductTypeDetailID) {
                spinner.setSelection(index + 1)
                break
            }
        }
    }

    private fun setLoanPurposeDropdown(position: Int) {
        val loanPurposeList = loanProducts[position].loanPurposeList
        binding.spinnerLoanPurpose.adapter = LoanPurposeSpinnerAdapter(mContext!!, loanPurposeList)
        if (loanInfo != null) {
            selectPurposeValue(binding.spinnerLoanPurpose, loanInfo!!)
        }
    }

    private fun selectPurposeValue(spinner: Spinner, value: Requests.LoanInfoObj) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.LoanPurpose
            if (obj.loanPurposeID == value.loanPurposeTypeDetailID) {
                spinner.setSelection(index)
                break
            }
        }
    }

    private fun setMasterDropDownValue(allMasterDropDown: AllMasterDropDown) {
        loanScheme = ArrayList()
        interestType = ArrayList()
        channelPartnerName = ArrayList()
        sourcingChannelPartner = ArrayList()
        loanScheme = allMasterDropDown.LoanScheme!!
        binding.spinnerLoanScheme.adapter = MasterSpinnerAdapter(mContext!!, loanScheme)

        interestType = allMasterDropDown.LoanInformationInterestType!!
        binding.spinnerInterestType.adapter = MasterSpinnerAdapter(mContext!!, interestType)

        sourcingChannelPartner = allMasterDropDown.SourcingChannelPartner!!
        binding.spinnerSourcingChannelPartner.adapter = MasterSpinnerAdapter(mContext!!, sourcingChannelPartner)
        binding.spinnerSourcingChannelPartner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val selectedValue = parent.selectedItem as DropdownMaster
                    if (selectedValue.typeDetailCode == "DSA") {
                        getChannelPartnerName(selectedValue)
                        binding.spinnerPartnerName.visibility = View.VISIBLE
                    }
                    else{
                        binding.spinnerPartnerName.visibility = View.GONE
                    }
                }
            }
        }
        if (loanInfo != null) {
            selectLoanSchemeValue(binding.spinnerLoanScheme, loanInfo!!)
            selectInterestTypeValue(binding.spinnerInterestType, loanInfo!!)
            selectSourcingChannelPartnerValue(binding.spinnerSourcingChannelPartner, loanInfo!!)
        }
    }

    private fun selectSourcingChannelPartnerValue(spinner: Spinner, value: Requests.LoanInfoObj) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == value.sourcingChannelPartnerTypeDetailID) {
                spinner.setSelection(index)
            }
        }
    }

    private fun selectInterestTypeValue(spinner: Spinner, value: Requests.LoanInfoObj) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == value.interestTypeTypeDetailID) {
                spinner.setSelection(index)
            }
        }
    }

    private fun selectLoanSchemeValue(spinner: Spinner, value: Requests.LoanInfoObj) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == value.loanSchemeTypeDetailID) {
                spinner.setSelection(index)
            }
        }
    }

    private fun setMandatoryField() {
        ShowAsMandatory(binding.inputLayoutAmountRequest)
        ShowAsMandatory(binding.inputLayoutTenure)
        ShowAsMandatory(binding.inputLayoutEmi)
    }

    private fun setClickListeners() {
        binding.ivUploadForm.setOnClickListener {
            UploadData(frag, mContext!!)
        }
        binding.ivThumbnail.setOnClickListener {
            UploadedFormDataActivity.start(mContext!!, image, pdf)
        }
        binding.btnSaveAndContinue.setOnClickListener {
            if (formValidation.validateLoanInformation(binding = binding)) {
                checkPropertySelection()
                saveDataToDB(getLoanInfoMaster())
                loanInfoPostPresenter.callNetwork(ConstantsApi.CALL_LOAN_INFO_POST)
            }
        }
    }

    private fun saveDataToDB(loanInfoMaster: LoanInfoMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().loanInfoDao().insertLoanInfo(loanInfoMaster)
        }
    }

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().loanInfoDao().getLoanInfo(mLeadId!!).observe(this, Observer { loanInfoMaster ->
            loanInfoMaster?.let {
                loanMaster = it
                loanInfo = loanMaster?.loanApplicationObj
                if (loanInfo != null) {
                    fillFormWithSavedData(loanInfo!!)
                }
            }
        })
    }

    private fun fillFormWithSavedData(loanInfo: Requests.LoanInfoObj) {
        binding.etAmountRequest.setText(loanInfo.loanAmountRequest.toString())
        binding.etEmi.setText(loanInfo.affordableEMI.toString())
        binding.etTenure.setText(loanInfo.tenure.toString())
    }

    override val branchId: String
        get() = mBranchId
    override val employeeId: String
        get() = mEmployeeId
    override val channelTypeId: String
        get() = mChannelTypeId

    private fun getChannelPartnerName(sourceChannelPartner: DropdownMaster) {
//        mEmployeeId = sharedPreferences.getLoginData()?.loginObj?.userDetails?.userBasicDetails?.userId.toString()
//        mChannelTypeId = sourceChannelPartner.typeDetailID.toString()
        mChannelTypeId = "3"
        mEmployeeId = "2"
        mBranchId = "1"
        sourcePartnerPresenter.callNetwork(ConstantsApi.CALL_SOURCE_CHANNEL_PARTNER_NAME)
    }

    override fun getSourceChannelPartnerNameSuccess(value: Response.ResponseSourceChannelPartnerName) {
        setChannelPartnerNameDropDown(value.responseObj)
    }

    private fun setChannelPartnerNameDropDown(channelPartners: ArrayList<Response.ChannelPartnerName>) {
        binding.spinnerPartnerName.visibility = View.VISIBLE
        binding.spinnerPartnerName.adapter = ChannelPartnerNameSpinnerAdapter(mContext!!, channelPartners)
    }

    override fun getSourceChannelPartnerNameFailure(msg: String) {
        showToast(msg)
    }

    private fun checkPropertySelection() {
        if (binding.cbPropertySelected.isChecked) {
            sharedPreferences.setPropertySelection("Yes")
            showToast("Property Selected")
        } else {
            sharedPreferences.setPropertySelection("No")
        }
    }

    override val loanInfoRequestPost: LoanInfoMaster
        get() = getLoanInfoMaster()

    private val loanInfoObj: Requests.LoanInfoObj
        get() {
            val sourcingChannelPartner = binding.spinnerSourcingChannelPartner.selectedItem as
                    DropdownMaster
            val channelPartnerName = binding.spinnerPartnerName.selectedItem as
                    Response.ChannelPartnerName?
            val loanProduct = binding.spinnerLoanProduct.selectedItem as
                    LoanProductMaster
            val interestType = binding.spinnerInterestType.selectedItem as DropdownMaster
            val loanPurpose = binding.spinnerLoanPurpose.selectedItem as Response.LoanPurpose
            val loanScheme = binding.spinnerLoanScheme.selectedItem as DropdownMaster
            return Requests.LoanInfoObj(affordableEMI = binding.etEmi.text.toString().toInt(),
                    leadID = mLeadId!!.toInt(), loanProductTypeDetailID = loanProduct.productID, salesOfficerEmpID = empId!!.toInt(),
                    loanPurposeTypeDetailID = loanPurpose.loanPurposeID, loanSchemeTypeDetailID = loanScheme.typeDetailID!!,
                    loanAmountRequest = binding.etAmountRequest.text.toString().toInt(), tenure = binding.etTenure.text.toString().toInt(),
                    interestTypeTypeDetailID = interestType.typeDetailID!!, sourcingChannelPartnerTypeDetailID = sourcingChannelPartner.typeDetailID!!,
                    channelPartnerNameTypeDetailID = channelPartnerName?.dsaID)
        }

    private fun getLoanInfoMaster(): LoanInfoMaster {
        loanMaster?.loanApplicationObj = loanInfoObj
        return loanMaster!!
    }

    override fun getLoanInfoPostSuccess(value: Response.ResponsePostLoanInfo) {
        gotoNextFragment()
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, PersonalInfoFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }

    override fun getLoanInfoPostFailure(msg: String) {
        showToast(msg)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val returnUri = data!!.data
            when (requestCode) {
                SELECT_PDF_CODE -> {
                    Log.i("URI: ", returnUri?.toString())
                    pdf = returnUri
                    binding.ivThumbnail.visibility = View.GONE
                    binding.ivPdf.visibility = View.VISIBLE
                }
                SELECT_IMAGE_CODE -> {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
                    image = bitmap
                    binding.ivThumbnail.setImageBitmap(bitmap)
                }
                CLICK_IMAGE_CODE -> {
                    val thumbnail = data.extras!!.get("data") as Bitmap
                    image = thumbnail
                    binding.ivThumbnail.setImageBitmap(thumbnail)
                }
            }
        }
    }
}
