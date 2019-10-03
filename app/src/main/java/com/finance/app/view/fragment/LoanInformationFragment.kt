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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.finance.app.databinding.FragmentLoanInformationBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.presenter.LoanInfoPresenter
import com.finance.app.utility.ShowAsMandatory
import com.finance.app.utility.UploadData
import com.finance.app.view.activity.DashboardActivity
import com.finance.app.view.activity.UploadedFormDataActivity
import com.finance.app.view.adapters.Recycler.Adapter.GenericSpinnerAdapter
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

class LoanInformationFragment : Fragment(), LoanApplicationConnector.LoanInfo {
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
    private lateinit var channelPartnerName: ArrayList<DropdownMaster>
    private lateinit var sourcingChannelPartner: ArrayList<DropdownMaster>
    private val loanInfoPresenter = LoanInfoPresenter(this)

    companion object {
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
        setDropDownValue()
        setMandatoryField()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.ivUploadForm.setOnClickListener {
            UploadData(frag, context!!)
        }
        binding.ivThumbnail.setOnClickListener {
            UploadedFormDataActivity.start(context!!,image, pdf)
        }
        binding.btnSaveAndContinue.setOnClickListener {
            if (formValidation.validateLoanInformation(binding = binding)) {
                Toast.makeText(context, "Successfully Validated", Toast.LENGTH_SHORT).show()
                loanInfoPresenter.callNetwork(ConstantsApi.CALL_LOAN_INFO)
            }
        }
    }

    private fun setDropDownValue() {
        val loanProducts = arrayOf("Home Loan", "LAP")
        val loanPurpose = arrayOf("House Purchase", "Flat Purchase", "Plot Purchase",
                "Improvement", "Plot Purchase + Construction", "HL- BT", "Top up", "HL-BT + Top Up",
                "LAP", "LAP BT+ Top Up", "BT+ Extension", "Seller BT")
        getDropDownsFromDB()
        val adapterLoanProduct = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, loanProducts)
        adapterLoanProduct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterLoanPurpose = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, loanPurpose)
        adapterLoanPurpose.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLoanProduct.adapter = adapterLoanProduct
        binding.spinnerLoanPurpose.adapter = adapterLoanPurpose
    }

    private fun getDropDownsFromDB() {
        loanScheme = ArrayList()
        interestType = ArrayList()
        channelPartnerName = ArrayList()
        sourcingChannelPartner = ArrayList()
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues?.let {
                masterDrownDownValues.LoanScheme?.let { genderDropDown ->
                    loanScheme = genderDropDown
                    binding.spinnerLoanScheme.adapter = GenericSpinnerAdapter(context!!, loanScheme)
                }
            }
            masterDrownDownValues?.let {
                masterDrownDownValues.LoanInformationInterestType?.let { interestTypeDropDown ->
                    interestType = interestTypeDropDown
                    binding.spinnerInterestType.adapter = GenericSpinnerAdapter(context!!, interestType)
                }
            }
            masterDrownDownValues?.let {
                masterDrownDownValues.ChannelType?.let { sourceChannelPartnerDropDown ->
                    sourcingChannelPartner = sourceChannelPartnerDropDown
                    binding.spinnerPartnerName.adapter = GenericSpinnerAdapter(context!!, channelPartnerName)
                }
            }
            masterDrownDownValues?.let {
                masterDrownDownValues.SourcingChannelPartner?.let { channelPartnerNameDropDown ->
                    channelPartnerName = channelPartnerNameDropDown
                    binding.spinnerSourcingChannelPartner.adapter = GenericSpinnerAdapter(context!!, sourcingChannelPartner)
                }
            }
        })
    }

    private fun setMandatoryField() {
        ShowAsMandatory(binding.inputLayoutAmountRequest)
        ShowAsMandatory(binding.inputLayoutTenure)
        ShowAsMandatory(binding.inputLayoutEmi)
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
            return Requests.LoanInfoObj(affordableEMI = binding.etEmi.text.toString().toInt(), loanApplicationID = 1,
                    leadID = 5, productID = 1, loanPurposeID = 1, loanSchemeTypeDetailID = 1, loanAmountRequest = binding.etAmountRequest.text.toString().toInt(),
                    tenure = binding.etTenure.text.toString().toInt(), interestTypeTypeDetailID = 1, sourcingChannelPartnerTypeDetailID = sourcingChannelPartner.typeDetailID.toString().toInt(),
                    channelPartnerDsaID = 2, salesOfficerEmpID = 1, creditOfficerEmpName = "Vishal", creditOfficerEmpID = 2, salesOfficerEmpName = "Rathi",
                    ruleEngineResponse = "actico Json string", logginUserEntityID = 1)
        }

    override fun getLoanInfoSuccess(value: Response.ResponseLoanInfo) {
        showToast("Success")
        DashboardActivity.start(context!!)
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