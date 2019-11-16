package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentAssetLiablityBinding
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.presenter.LoanAppGetPresenter
import com.finance.app.presenter.presenter.LoanAppPostPresenter
import com.finance.app.utility.SetAssetLiabilityMandatoryFiled
import com.finance.app.utility.ShowAsMandatory
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
import com.finance.app.view.adapters.recycler.adapter.AssetDetailAdapter
import com.finance.app.view.adapters.recycler.adapter.CreditCardAdapter
import com.finance.app.view.adapters.recycler.adapter.ObligationAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class AssetLiabilityFragment : BaseFragment(), LoanApplicationConnector.PostLoanApp,
        LoanApplicationConnector.GetLoanApp, ApplicantsAdapter.ItemClickListener {

    private lateinit var binding: FragmentAssetLiablityBinding
    private lateinit var mContext: Context
    private var mLead: AllLeadMaster? = null
    private var mLeadId: String? = null
    private var empId: String? = null
    private val loanAppPostPresenter = LoanAppPostPresenter(this)
    private val loanAppGetPresenter = LoanAppGetPresenter(this)
    private var applicantAdapter: ApplicantsAdapter? = null
    private var assetLiabilityMaster: AssetLiabilityMaster? = AssetLiabilityMaster()
    private var currentApplicant: AssetLiabilityModel = AssetLiabilityModel()
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil

    companion object {
        private lateinit var applicantTab: ArrayList<String>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_asset_liablity)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        mContext = context!!
        getAssetLiabilityInfo()
        SetAssetLiabilityMandatoryFiled(binding)
        setDropDownValue()
        applicantTab = ArrayList()
        setDropDownValue()
        setCoApplicants()
        setClickListeners()
        checkIncomeConsideration()
    }


    private fun getAssetLiabilityInfo() {
        mLead = sharedPreferences.getLeadDetail()
        empId = sharedPreferences.getUserId()
//        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
    }

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override val storageType: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLoanAppGetFailure(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun checkIncomeConsideration() {
        val selected = sharedPreferences.getIncomeConsideration()
        if (!selected) {
            Toast.makeText(context, "Income not considered in Loan Information",
                    Toast.LENGTH_SHORT).show()
//            formValidation.disableAssetLiabilityFields(binding)
        }
    }

    private fun setCoApplicants() {
        applicantTab.add("Applicant")
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapter = ApplicantsAdapter(context!!, applicantTab)
        applicantAdapter!!.setOnItemClickListener(this)
        binding.rcApplicants.adapter = applicantAdapter
    }

    override fun onApplicantClick(position: Int) {
//        saveCurrentApplicant(position)
//        ClearPersonalForm(binding)
        changeCurrentApplicant()
        setDropDownValue()
    }

    private fun setClickListeners() {
        binding.btnAddAsset.setOnClickListener {
            showAssetDetails()
        }
        binding.layoutCreditCard.btnAddCreditCard.setOnClickListener {
            showCreditCardDetails()
        }
        binding.layoutObligations.btnAddObligation.setOnClickListener {
            showObligationDetail()
        }
    }

    private fun showObligationDetail() {
        binding.layoutObligations.rcObligation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.layoutObligations.rcObligation.adapter = ObligationAdapter(context!!)
        binding.layoutObligations.pageIndicatorObligation.attachTo(binding.layoutObligations.rcObligation)
        binding.layoutObligations.pageIndicatorObligation.visibility = View.VISIBLE
        binding.layoutObligations.rcObligation.visibility = View.VISIBLE
    }

    private fun showCreditCardDetails() {
        binding.layoutCreditCard.rcCreditCard.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.layoutCreditCard.rcCreditCard.adapter = CreditCardAdapter(context!!)
        binding.layoutCreditCard.pageIndicatorCreditCard.attachTo(binding.layoutCreditCard.rcCreditCard)
        binding.layoutCreditCard.pageIndicatorCreditCard.visibility = View.VISIBLE
        binding.layoutCreditCard.rcCreditCard.visibility = View.VISIBLE
    }

    private fun showAssetDetails() {
        binding.rcAsset.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rcAsset.adapter = AssetDetailAdapter(context!!)
        binding.pageIndicatorAsset.attachTo(binding.rcAsset)
        binding.pageIndicatorAsset.visibility = View.VISIBLE
        binding.rcAsset.visibility = View.VISIBLE
    }

    private fun setDropDownValue() {
        val lists: ArrayList<DropdownMaster> = ArrayList()

        binding.spinnerAssetSubType.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerAssetType.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerDocumentProof.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerOwnership.adapter = MasterSpinnerAdapter(mContext, lists)

        binding.layoutObligations.spinnerLoanOwnership.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.layoutObligations.spinnerLoanType.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.layoutObligations.spinnerObligate.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.layoutObligations.spinnerRepaymentBank.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.layoutObligations.spinnerEmiPaidInSameMonth.adapter = MasterSpinnerAdapter(mContext, lists)

        binding.layoutCreditCard.spinnerBankName.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.layoutCreditCard.spinnerObligate.adapter = MasterSpinnerAdapter(mContext, lists)
    }

    private fun changeCurrentApplicant() {
    }

    override val loanAppRequestPost: LoanApplicationRequest
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getLoanAppPostSuccess(value: Response.ResponseGetLoanApplication) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLoanAppPostFailure(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, BankDetailFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }
}