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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.FragmentLoanInformationBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.persistence.model.LoanProductMaster
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.presenter.LoanInfoPresenter
import com.finance.app.presenter.presenter.SourceChannelPartnerNamePresenter
import com.finance.app.utility.ShowAsMandatory
import com.finance.app.utility.UploadData
import com.finance.app.view.activity.UploadedFormDataActivity
import com.finance.app.view.adapters.recycler.adapter.ChannelPartnerNameSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.LoanProductSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.LoanPurposeSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.MasterSpinnerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class LoanInformationFragment : BaseFragment(), LoanApplicationConnector.LoanInfo, LoanApplicationConnector.SourceChannelPartnerName {

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
    private val sourcePartnerPresenter = SourceChannelPartnerNamePresenter(this)
    private val loanInfoPresenter = LoanInfoPresenter(this)
    private var mContext: Context? = null

    companion object {
        private lateinit var mBranchId: String
        private lateinit var mEmployeeId: String
        private lateinit var mChannelTypeId: String
        private const val isMandatory = true
        private const val SELECT_PDF_CODE = 1
        private const val CLICK_IMAGE_CODE = 2
        private var loanProductPosition = -1
        private var loanPurposePosition = -1
        private var loanSchemePosition = -1
        private var loanInterestPosition = -1
        private var sourceChannelPartnerPosition = -1
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
        getDropDownsFromDB()
        setMandatoryField()
        setClickListeners()
        checkSavedData()
    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setMasterDropDownValue(allMasterDropDown)
            }
        })

        dataBase.provideDataBaseSource().loanProductDao().getAllLoanProduct().observe(viewLifecycleOwner, Observer { loanProduct ->
            loanProduct.let {
                loanProducts = it
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
                    setLoanPurposeDropdown(position)
                }
            }
        }
    }

    private fun setLoanPurposeDropdown(position: Int) {
        val loanPurposeList = loanProducts[position].loanPurposeList
        binding.spinnerLoanPurpose.adapter = LoanPurposeSpinnerAdapter(mContext!!, loanPurposeList)
        binding.spinnerLoanPurpose?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    loanPurposePosition = position
                }
            }
        }
        checkSavedData()
    }

    private fun setMasterDropDownValue(allMasterDropDown: AllMasterDropDown) {
        loanScheme = ArrayList()
        interestType = ArrayList()
        channelPartnerName = ArrayList()
        sourcingChannelPartner = ArrayList()
        loanScheme = allMasterDropDown.LoanScheme!!
        binding.spinnerLoanScheme.adapter = MasterSpinnerAdapter(mContext!!, loanScheme)
        binding.spinnerLoanScheme?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    loanSchemePosition = position
                }
            }
        }

        interestType = allMasterDropDown.LoanInformationInterestType!!
        binding.spinnerInterestType.adapter = MasterSpinnerAdapter(mContext!!, interestType)
        binding.spinnerInterestType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    loanInterestPosition = position
                }
            }
        }

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
                setPropertySelection()
                loanInfoPresenter.callNetwork(ConstantsApi.CALL_LOAN_INFO)
            }
        }
    }

    private fun checkSavedData() {
        loanInfo = sharedPreferences.getLoanInfoData()
        if (loanInfo != null) {
            fillFormWithSavedData(loanInfo!!)
        }
    }

    private fun fillFormWithSavedData(loanInfo: Requests.LoanInfoObj) {
        binding.etAmountRequest.setText(loanInfo.loanAmountRequest.toString())
        binding.etEmi.setText(loanInfo.affordableEMI.toString())
        binding.etTenure.setText(loanInfo.tenure.toString())
        for (loan in loanProducts) {
            if (loan.productID == loanInfo.productID) {
                val selectedLoanProduct = loanProducts.indexOf(loan)
                binding.spinnerLoanProduct.setSelection(selectedLoanProduct)
                break
            }
        }
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

    private fun setPropertySelection() {
        if (binding.cbPropertySelected.isChecked) {
            sharedPreferences.setPropertySelection("Yes")
            showToast("Property Selected")
        } else {
            sharedPreferences.setPropertySelection("No")
        }
    }

    override val loanInfoRequest: Requests.RequestLoanInfo
        get() = requestLoanInfo

    private val requestLoanInfo: Requests.RequestLoanInfo
        get() {
            return Requests.RequestLoanInfo(leadID = 5, draftDetailID = 95, loanApplicationObj = loanInfoObj, storageTypeID = null, userID = null)
        }

    private val loanInfoObj: Requests.LoanInfoObj
        get() {
            val sourcingChannelPartner = binding.spinnerSourcingChannelPartner.selectedItem as
                    DropdownMaster
            val channelPartnerName = binding.spinnerPartnerName.selectedItem as
                    Response.ChannelPartnerName?

            return Requests.LoanInfoObj(affordableEMI = binding.etEmi.text.toString().toInt(), loanApplicationID = 1,
                    leadID = 5, productID = 1, loanPurposeID = 1, loanSchemeTypeDetailID = 1, loanAmountRequest = binding.etAmountRequest.text.toString().toInt(),
                    tenure = binding.etTenure.text.toString().toInt(), interestTypeTypeDetailID = 1, sourcingChannelPartnerTypeDetailID = sourcingChannelPartner.typeDetailID,
                    channelPartnerDsaID = channelPartnerName?.dsaID, salesOfficerEmpID = 1, creditOfficerEmpName = "Vishal", creditOfficerEmpID = 2, salesOfficerEmpName = "Rathi",
                    ruleEngineResponse = "actico Json string", logginUserEntityID = 1)
        }

    override fun getLoanInfoSuccess(value: Response.ResponseLoanInfo) {
        sharedPreferences.saveLoanInfoData(loanInfoObj)
        gotoNextFragment()
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, PersonalInfoFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }

    override fun getLoanInfoFailure(msg: String) {
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
