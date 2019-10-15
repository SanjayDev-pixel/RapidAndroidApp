package com.finance.app.view.fragment
import android.app.Activity.RESULT_OK
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
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.DialogFactory
import motobeans.architecture.util.exShowToast
import javax.inject.Inject

class LoanInformationFragment : Fragment(), LoanApplicationConnector.LoanInfo, LoanApplicationConnector.SourceChannelPartnerName {
    private lateinit var binding: FragmentLoanInformationBinding
    private val frag: Fragment = this
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var loanScheme: ArrayList<DropdownMaster>
    private lateinit var interestType: ArrayList<DropdownMaster>
    private lateinit var channelPartnerName: ArrayList<Response.ChannelPartnerName>
    private lateinit var allMasterDropDown: AllMasterDropDown
    private lateinit var loanProducts: List<LoanProductMaster>
    private val sourcePartnerPresenter = SourceChannelPartnerNamePresenter(this)
    private lateinit var sourcingChannelPartner: ArrayList<DropdownMaster>
    private val loanInfoPresenter = LoanInfoPresenter(this)

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
        binding = FragmentLoanInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ArchitectureApp.instance.component.inject(this)
        getDropDownsFromDB()
        setMandatoryField()
        setClickListeners()
    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().loanProductDao().getAllLoanProduct().observe(viewLifecycleOwner, Observer { loanProduct ->
            loanProduct.let {
                loanProducts = it
                setProductDropDownValue(loanProducts)
            }
        })
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setMasterDropDownValue(allMasterDropDown)
            }
        })
    }

    private fun setProductDropDownValue(products: List<LoanProductMaster>) {
        loanProducts = products
        binding.spinnerLoanProduct.adapter = LoanProductSpinnerAdapter(context!!, loanProducts)
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
        binding.spinnerLoanPurpose.adapter = LoanPurposeSpinnerAdapter(context!!, loanPurposeList)
    }

    private fun setMasterDropDownValue(allMasterDropDown: AllMasterDropDown) {
        loanScheme = ArrayList()
        interestType = ArrayList()
        channelPartnerName = ArrayList()
        sourcingChannelPartner = ArrayList()

        loanScheme = allMasterDropDown.LoanScheme!!
        binding.spinnerLoanScheme.adapter = MasterSpinnerAdapter(context!!, loanScheme)

        interestType = allMasterDropDown.LoanInformationInterestType!!
        binding.spinnerInterestType.adapter = MasterSpinnerAdapter(context!!, interestType)

        sourcingChannelPartner = allMasterDropDown.SourcingChannelPartner!!
        binding.spinnerSourcingChannelPartner.adapter = MasterSpinnerAdapter(context!!, sourcingChannelPartner)

    }

    private fun setMandatoryField() {
        ShowAsMandatory(binding.inputLayoutAmountRequest)
        ShowAsMandatory(binding.inputLayoutTenure)
        ShowAsMandatory(binding.inputLayoutEmi)
    }

    private fun setClickListeners() {
        binding.ivUploadForm.setOnClickListener {
            UploadData(frag, context!!)
        }
        binding.ivThumbnail.setOnClickListener {
            UploadedFormDataActivity.start(context!!, image, pdf)
        }
        binding.btnSaveAndContinue.setOnClickListener {
            if (formValidation.validateLoanInformation(binding = binding)) {
                setPropertySelection()
                loanInfoPresenter.callNetwork(ConstantsApi.CALL_LOAN_INFO)
            }
        }
        binding.spinnerSourcingChannelPartner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val selectedValue = parent.selectedItem as DropdownMaster
                    if (selectedValue.typeDetailCode != "DIRECT") {
                        getChannelPartnerName(selectedValue)
                    }
                }
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
//        mEmployeeId = sharedPreferences.getLoginData()?.responseObj?.userDetails?.userBasicDetails?.userId.toString()
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
        binding.spinnerPartnerName.adapter = ChannelPartnerNameSpinnerAdapter(context!!, channelPartners)
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
                    Response.ChannelPartnerName

            return Requests.LoanInfoObj(affordableEMI = binding.etEmi.text.toString().toInt(), loanApplicationID = 1,
                    leadID = 5, productID = 1, loanPurposeID = 1, loanSchemeTypeDetailID = 1, loanAmountRequest = binding.etAmountRequest.text.toString().toInt(),
                    tenure = binding.etTenure.text.toString().toInt(), interestTypeTypeDetailID = 1, sourcingChannelPartnerTypeDetailID = sourcingChannelPartner.typeDetailID,
                    channelPartnerDsaID = channelPartnerName.dsaID, salesOfficerEmpID = 1, creditOfficerEmpName = "Vishal", creditOfficerEmpID = 2, salesOfficerEmpName = "Rathi",
                    ruleEngineResponse = "actico Json string", logginUserEntityID = 1)
        }

    override fun getLoanInfoSuccess(value: Response.ResponseLoanInfo) {
        showToast("Success")
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

    override fun showToast(msg: String) {
        msg.exShowToast(context)
    }

    override fun showProgressDialog() {
        when (BaseAppCompatActivity.progressDialog == null) {
            true -> BaseAppCompatActivity.progressDialog = DialogFactory.getInstance(context = context!!)
        }
        BaseAppCompatActivity.progressDialog?.show()
    }

    override fun hideProgressDialog() {
        BaseAppCompatActivity.progressDialog?.hide()
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
