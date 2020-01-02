package com.finance.app.view.fragment
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
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
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.connector.SourceChannelPartnerNameConnector
import com.finance.app.presenter.presenter.*
import com.finance.app.utility.*
import com.finance.app.view.activity.UploadedFormDataActivity
import com.finance.app.view.adapters.recycler.Spinner.ChannelPartnerNameSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.LoanProductSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.LoanPurposeSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.customViews.CustomSpinnerViewTest
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

class LoanInfoFragment : BaseFragment(), LoanApplicationConnector.PostLoanApp,
        LoanApplicationConnector.GetLoanApp, SourceChannelPartnerNameConnector.SourceChannelPartnerName {

    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var binding: FragmentLoanInformationBinding
    private val frag: Fragment = this
    private lateinit var allMasterDropDown: AllMasterDropDown
    private var loanProducts: ArrayList<LoanProductMaster> = ArrayList()
    private var selectedLoanProduct: LoanProductMaster? = null
    private val sourcePartnerPresenter = SourceChannelPartnerNamePresenter(this)
    private val loanAppGetPresenter = LoanAppGetPresenter(this)
    private val loanAppPostPresenter = LoanAppPostPresenter(this)
    private var mContext: Context? = null
    private val presenter = Presenter()
    private var mLead: AllLeadMaster? = null
    private var empId: String? = null
    private var loanMaster: LoanInfoMaster? = LoanInfoMaster()
    private var loanInfo: LoanInfoModel? = null
    private var channelPartner: DropdownMaster? = DropdownMaster()
    private var mChannelTypeId: Int = 0

    companion object {
        private lateinit var mBranchId: String
        private const val SELECT_PDF_CODE = 1
        private const val DIRECT = 53
        private const val CLICK_IMAGE_CODE = 2
        private const val SELECT_IMAGE_CODE = 3
        private var image: Bitmap? = null
        private var pdf: Uri? = null
        private val responseConversion = ResponseConversion()
        private val requestConversion = RequestConversion()
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
        SetLoanInfoMandatoryField(binding)
        setClickListeners()
    }

    private fun getLoanInfo() {
        mLead = sharedPreferences.getLeadDetail()
        empId = sharedPreferences.getLoginData()!!.responseObj.userDetails.userBasicDetails.tablePrimaryID.toString()
        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
    }

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override val storageType: String
        get() = loanMaster?.storageType!!

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        value.responseObj?.let {
            loanMaster = responseConversion.toLoanMaster(value.responseObj)
            loanInfo = loanMaster?.draftData
        }
        showData(loanInfo)
    }

    private fun showData(loanInfo: LoanInfoModel?) {
        getDropDownsFromDB()
        if (loanInfo != null) {
            fillFormWithLoanData(loanInfo)
        }
    }

    private fun saveDataToDB(loanInfoMaster: LoanInfoMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().loanInfoDao().insertLoanInfo(loanInfoMaster)
        }
    }

    override fun getLoanAppGetFailure(msg: String) = getDataFromDB()

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setMasterDropDownValue(allMasterDropDown)
            }
        })

        dataBase.provideDataBaseSource().loanProductDao().getAllLoanProduct().observe(viewLifecycleOwner, Observer { loanProductValue ->
            loanProductValue.let {
                val arrayListOfLoanProducts = ArrayList<LoanProductMaster>()
                arrayListOfLoanProducts.addAll(loanProductValue)
                loanProducts = arrayListOfLoanProducts
                setProductDropDownValue(loanProducts)
            }
        })
    }

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().loanInfoDao().getLoanInfo(leadId).observe(this, Observer { loanInfoMaster ->
            loanInfoMaster?.let {
                loanMaster = it
                loanInfo = loanMaster?.draftData
            }
            showData(loanInfo)
        })
    }

    private fun setProductDropDownValue(products: ArrayList<LoanProductMaster>) {
        binding.spinnerLoanProduct.adapter = LoanProductSpinnerAdapter(mContext!!, products)
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
            selectProductValue(binding.spinnerLoanProduct, loanInfo?.productID!!)
        }else{
            selectProductValue(binding.spinnerLoanProduct, mLead?.loanProductID!!)
        }
    }

    private fun selectProductValue(spinner: Spinner, id: Int) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as LoanProductMaster
            if (obj.productID == id) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun setLoanPurposeDropdown(position: Int) {
        val loanPurposeList = loanProducts[position].loanPurposeList
        binding.spinnerLoanPurpose.adapter = LoanPurposeSpinnerAdapter(mContext!!, loanPurposeList)
        if (loanInfo != null) {
            selectLoanPurposeValue(binding.spinnerLoanPurpose, loanInfo!!)
        }
    }

    private fun selectLoanPurposeValue(spinner: Spinner, value: LoanInfoModel) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as Response.LoanPurpose
            if (obj.loanPurposeID == value.loanPurposeID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private lateinit var interestType: CustomSpinnerViewTest<DropdownMaster>
    private fun setMasterDropDownValue(allMasterDropDown: AllMasterDropDown) {
        binding.spinnerLoanScheme.attachActivity(mContext = mContext!!, dropdownValue = allMasterDropDown.LoanScheme!!, label = "Loan Scheme")
        interestType = CustomSpinnerViewTest(context = mContext!!, dropDowns = allMasterDropDown.LoanInformationInterestType!!, label = "Interest Type")
        binding.layoutInterestType.addView(interestType)

//        binding.spinnerInterestType.adapter = MasterSpinnerAdapter(mContext!!, allMasterDropDown.LoanInformationInterestType)
        binding.spinnerSourcingChannelPartner.adapter = MasterSpinnerAdapter(mContext!!, allMasterDropDown.SourcingChannelPartner)
        binding.spinnerSourcingChannelPartner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    channelPartner = parent.selectedItem as DropdownMaster
                    getChannelPartnerName(channelPartner!!)
                }
            }
        }
        if (loanInfo != null) {
            binding.spinnerLoanScheme.selectValue(loanInfo!!.loanSchemeTypeDetailID)
            selectMasterValue(binding.spinnerSourcingChannelPartner, loanInfo!!.sourcingChannelPartnerTypeDetailID!!)
//            selectMasterValue(binding.spinnerInterestType, loanInfo!!.interestTypeTypeDetailID!!)
            interestType.selectValue(loanInfo!!.interestTypeTypeDetailID!!)
        }
    }

    private fun selectMasterValue(spinner: Spinner, id: Int) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == id) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun setClickListeners() {
        binding.ivUploadForm.setOnClickListener {
            UploadData(frag, mContext!!)
        }
        binding.ivThumbnail.setOnClickListener {
            UploadedFormDataActivity.start(mContext!!, image, pdf)
        }
        binding.btnNext.setOnClickListener {
            if (formValidation.validateLoanInformation(binding, selectedLoanProduct)) {
                checkPropertySelection()
                presenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP, dmiConnector = CallPostLoanApp())
                loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP)
            } else showToast(getString(R.string.validation_error))
        }
        CurrencyConversion().convertToCurrencyType(binding.etAmountRequest)
    }

    inner class CallPostLoanApp : ViewGeneric<LoanApplicationRequest, Response.ResponseGetLoanApplication>(context = mContext!!) {
        override val apiRequest: LoanApplicationRequest
            get() = requestConversion.loanInfoRequest(getLoanInfoMaster())

        override fun getApiSuccess(value: Response.ResponseGetLoanApplication) {
            value.responseObj?.let {
                loanMaster = responseConversion.toLoanMaster(value.responseObj)
                loanInfo = loanMaster?.draftData
            }
            showData(loanInfo)
        }
    }

    private fun checkPropertySelection() {
        if (binding.cbPropertySelected.isChecked) {
            sharedPreferences.setPropertySelection("Yes")
        } else {
            sharedPreferences.setPropertySelection("No")
        }
    }

    private fun fillFormWithLoanData(loanInfo: LoanInfoModel) {
        binding.etAmountRequest.setText(loanInfo.loanAmountRequest.toString())
        binding.etEmi.setText(loanInfo.affordableEMI!!.toInt().toString())
        binding.etTenure.setText(loanInfo.tenure!!.toInt().toString())
        binding.cbPropertySelected.isChecked = loanInfo.isPropertySelected!!
    }

    override val branchId: String
        get() = mBranchId
    override val employeeId: String
        get() = empId!!
    override val channelTypeId: String
        get() = mChannelTypeId.toString()

    private fun getChannelPartnerName(sourceChannelPartner: DropdownMaster) {
        mChannelTypeId = sourceChannelPartner.typeDetailID.toString().toInt()
        mBranchId = sharedPreferences.getLeadDetail().branchID!!
        empId = sharedPreferences.getEmpId()
        if (mChannelTypeId != DIRECT) {
            sourcePartnerPresenter.callNetwork(ConstantsApi.CALL_SOURCE_CHANNEL_PARTNER_NAME)
            binding.spinnerPartnerName.visibility = View.VISIBLE
        } else {
            binding.spinnerPartnerName.adapter = ChannelPartnerNameSpinnerAdapter(mContext!!, ArrayList())
            binding.spinnerPartnerName.visibility = View.GONE
        }
    }

    override fun getSourceChannelPartnerNameSuccess(value: Response.ResponseSourceChannelPartnerName) {
        if (value.responseObj.size > 0) {
            setChannelPartnerNameDropDown(value.responseObj)
        } else {
            binding.spinnerPartnerName.adapter = ChannelPartnerNameSpinnerAdapter(mContext!!, ArrayList())
        }
    }

    private fun setChannelPartnerNameDropDown(channelPartners: ArrayList<Response.ChannelPartnerName>) {
        binding.spinnerPartnerName.visibility = View.VISIBLE
        binding.spinnerPartnerName.adapter = ChannelPartnerNameSpinnerAdapter(mContext!!, channelPartners)
        if (loanInfo != null) {
            selectPartnerNameValue(binding.spinnerPartnerName, channelPartners)
        }
    }

    private fun selectPartnerNameValue(spinner: Spinner, channelPartners: ArrayList<Response.ChannelPartnerName>) {
        for (index in 0 until channelPartners.size) {
            val obj = spinner.getItemAtPosition(index) as Response.ChannelPartnerName
            if (obj.dsaID == loanInfo!!.channelPartnerDsaID) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    override fun getSourceChannelPartnerNameFailure(msg: String)=showToast(msg)

    override val loanAppRequestPost: LoanApplicationRequest
        get() = requestConversion.loanInfoRequest(getLoanInfoMaster())

    override fun getLoanAppPostSuccess(value: Response.ResponseGetLoanApplication) {
        saveDataToDB(getLoanInfoMaster())
        AppEvents.fireEventLoanAppChangeNavFragmentNext()
    }

    override fun getLoanAppPostFailure(msg: String) {
        saveDataToDB(getLoanInfoMaster())
        showToast(msg)
    }

    private fun getLoanInfoObj(): LoanInfoModel {
        val loanInfoObj = LoanInfoModel()
        val sourcingChannelPartner = binding.spinnerSourcingChannelPartner.selectedItem as
                DropdownMaster?
        val channelPartnerName = binding.spinnerPartnerName.selectedItem as
                Response.ChannelPartnerName?
        val loanProduct = binding.spinnerLoanProduct.selectedItem as
                LoanProductMaster?
        val interestType = interestType.getSelectedType()
        val loanPurpose = binding.spinnerLoanPurpose.selectedItem as Response.LoanPurpose?
        val loanScheme = binding.spinnerLoanScheme.getSelectedType() as DropdownMaster?

        loanInfoObj.leadID = leadId.toInt()
        loanInfoObj.productID = loanProduct?.productID
        loanInfoObj.salesOfficerEmpID = empId!!.toInt()
        loanInfoObj.loanPurposeID = loanPurpose?.loanPurposeID
        loanInfoObj.loanSchemeTypeDetailID = loanScheme?.typeDetailID
        loanInfoObj.interestTypeTypeDetailID = interestType?.typeDetailID
        loanInfoObj.sourcingChannelPartnerTypeDetailID = sourcingChannelPartner?.typeDetailID
        loanInfoObj.isPropertySelected = binding.cbPropertySelected.isChecked
        loanInfoObj.loanAmountRequest = CurrencyConversion().convertToNormalValue(binding.etAmountRequest.text.toString()).toInt()
        loanInfoObj.tenure = binding.etTenure.text.toString().toInt()
        loanInfoObj.channelPartnerDsaID = channelPartnerName?.dsaID
        loanInfoObj.affordableEMI = binding.etEmi.text.toString().toDouble()
        return loanInfoObj
    }

    private fun getLoanInfoMaster(): LoanInfoMaster {
        loanMaster?.draftData = getLoanInfoObj()
        loanMaster?.leadID = leadId.toInt()
        return loanMaster!!
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
