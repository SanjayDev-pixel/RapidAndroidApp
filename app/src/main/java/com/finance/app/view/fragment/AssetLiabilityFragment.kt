package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentAssetLiablityBinding
import com.finance.app.databinding.LayoutCreditCardDetailsBinding
import com.finance.app.databinding.LayoutObligationBinding
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.presenter.LoanAppGetPresenter
import com.finance.app.presenter.presenter.LoanAppPostPresenter
import com.finance.app.utility.*
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
import com.finance.app.view.adapters.recycler.adapter.AssetDetailAdapter
import com.finance.app.view.adapters.recycler.adapter.CreditCardAdapter
import com.finance.app.view.adapters.recycler.adapter.ObligationAdapter
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

class AssetLiabilityFragment : BaseFragment(), LoanApplicationConnector.PostLoanApp,
        LoanApplicationConnector.GetLoanApp, ApplicantsAdapter.ItemClickListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentAssetLiablityBinding
    private lateinit var mContext: Context
    private var mLead: AllLeadMaster? = null
    private lateinit var allMasterDropDown: AllMasterDropDown
    private val loanAppPostPresenter = LoanAppPostPresenter(this)
    private val loanAppGetPresenter = LoanAppGetPresenter(this)
    private var applicantAdapter: ApplicantsAdapter? = null
    private var applicantTab: ArrayList<Response.CoApplicantsObj>? = ArrayList()
    private var assetLiabilityMaster: AssetLiabilityMaster = AssetLiabilityMaster()
    private var aApplicantList: ArrayList<AssetLiabilityModel>? = ArrayList()
    private var currentApplicant: AssetLiabilityModel = AssetLiabilityModel()
    private var aDraftData = AssetLiabilityList()
    private var assetsList: ArrayList<AssetLiability>? = ArrayList()
    private var currentAsset: AssetLiability? = AssetLiability()
    private var cardDetailList: ArrayList<CardDetail>? = ArrayList()
    private var currentCardDetail: CardDetail? = CardDetail()
    private var obligationsList: ArrayList<ObligationDetail>? = ArrayList()
    private var currentObligation: ObligationDetail? = ObligationDetail()
    private var currentPosition = 0

    companion object {
        private val leadAndLoanDetail = LeadAndLoanDetail()
        private val responseConversion = ResponseConversion()
        private val requestConversion = RequestConversion()
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
        SetAssetLiabilityMandatoryField(binding)
        setDatePicker()
        setClickListeners()
//        checkIncomeConsideration()
    }

    private fun checkIncomeConsideration() {
    }

    private fun getAssetLiabilityInfo() {
        mLead = sharedPreferences.getLeadDetail()
        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
    }

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override val storageType: String
        get() = assetLiabilityMaster.storageType

    override fun getLoanAppGetFailure(msg: String) = getDataFromDB()

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        value.responseObj?.let {
            assetLiabilityMaster = responseConversion.toAssetLiabilityMaster(value.responseObj)
            aDraftData = assetLiabilityMaster.draftData!!
            aApplicantList = aDraftData.applicantDetails
        }
        setCoApplicants()
        showData(aApplicantList)
    }

    private fun setCoApplicants() {
        val applicantsList = sharedPreferences.getCoApplicantsList()
        if (applicantsList == null || applicantsList.size <= 0) {
            applicantTab?.add(getDefaultCoApplicant())
        }else applicantTab = applicantsList
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapter = ApplicantsAdapter(context!!, applicantTab!!)
        applicantAdapter!!.setOnItemClickListener(this)
        binding.rcApplicants.adapter = applicantAdapter
    }

    private fun getDefaultCoApplicant(): Response.CoApplicantsObj {
        return Response.CoApplicantsObj(firstName = "Applicant",
                isMainApplicant = true, leadApplicantNumber = leadAndLoanDetail.getLeadApplicantNum(currentPosition + 1))
    }

    override fun onApplicantClick(position: Int, coApplicant: Response.CoApplicantsObj) {
    }

    private fun showData(applicantList: ArrayList<AssetLiabilityModel>?) {
        if (applicantList != null) {
            for (applicant in applicantList) {
                if (applicant.isMainApplicant) {
                    currentApplicant = applicant
                    setUpCurrentApplicantDetails(currentApplicant)
                }
            }
        }
        fillFromWithCurrentApplicant()
        getDropDownsFromDB()
    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = masterDrownDownValues
                setMasterDropDownValue(allMasterDropDown)
            }
        })
    }

    private fun setMasterDropDownValue(allMasterDropDown: AllMasterDropDown?) {
        binding.spinnerDocumentProof.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown!!.DocumentProof!!)
        binding.spinnerAssetSubType.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.AssetSubType!!)
        binding.spinnerAssetType.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.AssetDetail!!)
        binding.spinnerOwnership.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.Ownership!!)
        binding.layoutCreditCard.spinnerBankName.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.BankName!!)
        binding.layoutCreditCard.spinnerObligate.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.Obligate!!)
        binding.layoutObligations.spinnerObligate.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.Obligate!!)
        binding.layoutObligations.spinnerLoanOwnership.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.LoanOwnership!!)
        binding.layoutObligations.spinnerLoanType.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.LoanType!!)
        binding.layoutObligations.spinnerRepaymentBank.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.RepaymentBank!!)
        binding.layoutObligations.spinnerEmiPaidInSameMonth.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown.BounceEmiPaidInSameMonth!!)
    }

    private fun setUpCurrentApplicantDetails(currentApplicant: AssetLiabilityModel) {
        assetsList = currentApplicant.applicantAssetLiabilityList
        cardDetailList = currentApplicant.applicantCreditCardDetailList
        obligationsList = currentApplicant.applicantExistingObligationList
        currentAsset = if (assetsList!!.size > 0) {
            assetsList!![0]
        } else AssetLiability()
        currentCardDetail = if (cardDetailList!!.size > 0) {
            cardDetailList!![0]
        } else CardDetail()
        currentObligation = if (obligationsList!!.size > 0) {
            obligationsList!![0]
        } else ObligationDetail()
    }

    private fun fillFromWithCurrentApplicant() {
        binding.etValue.setText(currentAsset?.assetValue.toString())
        binding.layoutCreditCard.etCurrentUtilization.setText(currentCardDetail?.currentUtilization.toString())
        binding.layoutCreditCard.etLastPaymentDate.setText(currentCardDetail?.lastPaymentDate!!)
        fillMasterDropdownValueInAssetForm()
    }

    private fun fillMasterDropdownValueInAssetForm() {
        selectMasterDropdownValue(binding.spinnerAssetType, currentAsset?.assetDetailsTypeDetailID)
        selectMasterDropdownValue(binding.spinnerAssetSubType, currentAsset?.subTypeOfAssetTypeDetailID)
        selectMasterDropdownValue(binding.spinnerOwnership, currentAsset?.ownershipTypeDetailID)
        selectMasterDropdownValue(binding.spinnerAssetType, currentAsset?.assetDetailsTypeDetailID)
        fillMasterDropdownValueInCardForm(binding.layoutCreditCard)
        fillMasterDropdownValueInObligationForm(binding.layoutObligations)
    }

    private fun fillMasterDropdownValueInCardForm(binding: LayoutCreditCardDetailsBinding) {
        selectMasterDropdownValue(binding.spinnerBankName, currentCardDetail?.bankNameTypeDetailID)
        selectMasterDropdownValue(binding.spinnerObligate, currentCardDetail?.obligateTypeDetail)
    }

    private fun fillMasterDropdownValueInObligationForm(binding: LayoutObligationBinding) {
        selectMasterDropdownValue(binding.spinnerLoanOwnership, currentObligation?.loanOwnershipTypeDetailID)
        selectMasterDropdownValue(binding.spinnerObligate, currentObligation?.obligateTypeDetailID)
        selectMasterDropdownValue(binding.spinnerLoanType, currentObligation?.loanTypeTypeDetailID)
        selectMasterDropdownValue(binding.spinnerRepaymentBank, currentObligation?.repaymentBankTypeDetailID)
        selectMasterDropdownValue(binding.spinnerEmiPaidInSameMonth, currentObligation?.bounseEmiPaidInSameMonth)
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

    private fun setDatePicker() {
        binding.layoutCreditCard.etLastPaymentDate.setOnClickListener {
            SelectDate(binding.layoutCreditCard.etLastPaymentDate, mContext)
        }
        binding.layoutObligations.etDisbursementDate.setOnClickListener {
            SelectDate(binding.layoutObligations.etDisbursementDate, mContext)
        }
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

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().assetLiabilityDao().getAssetLiability(leadId).observe(this, Observer { assetInfo ->
            assetInfo?.let {
                assetLiabilityMaster = assetInfo
                aDraftData = assetLiabilityMaster.draftData!!
                aApplicantList = aDraftData.applicantDetails
            }
            setCoApplicants()
            showData(aApplicantList)
        })
    }

    private fun getAssetLiabilityMaster(): AssetLiabilityMaster {
        aDraftData.applicantDetails = aApplicantList
        assetLiabilityMaster.draftData = aDraftData
        assetLiabilityMaster.leadID = leadId.toInt()
        return assetLiabilityMaster
    }

    override val loanAppRequestPost: LoanApplicationRequest
        get() = requestConversion.assetLiabilityRequest(getAssetLiabilityMaster())

    override fun getLoanAppPostSuccess(value: Response.ResponseGetLoanApplication) {
        saveDataToDB(getAssetLiabilityMaster())
        gotoNextFragment()
    }

    override fun getLoanAppPostFailure(msg: String) {
        saveDataToDB(getAssetLiabilityMaster())
        showToast(msg)
    }

    private fun saveDataToDB(assetLiability: AssetLiabilityMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().assetLiabilityDao().insertAssetLiability(assetLiability)
        }
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, ReferenceFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }
}