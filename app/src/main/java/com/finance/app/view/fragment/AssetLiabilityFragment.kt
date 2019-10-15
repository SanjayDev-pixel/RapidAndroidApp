package com.finance.app.view.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.databinding.FragmentAssetLiablityBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.view.adapters.recycler.adapter.*
import com.finance.app.model.Modals.*
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class AssetLiabilityFragment : Fragment(), PersonalApplicantsAdapter.ItemClickListener {
    private lateinit var binding: FragmentAssetLiablityBinding
    private lateinit var mContext: Context
    private var applicantAdapterPersonal: PersonalApplicantsAdapter? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation

    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private var coApplicant = 1
        private lateinit var applicantMenu: ArrayList<String>
        private var image: Bitmap? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAssetLiablityBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ArchitectureApp.instance.component.inject(this)
        applicantMenu = ArrayList()
        setDropDownValue()
        setCoApplicants()
        setClickListeners()
        checkIncomeConsideration()
    }

    private fun checkIncomeConsideration() {
        val selected = sharedPreferences.getIncomeConsideration()
        if (!selected) {
            Toast.makeText(context, "Income not considered in Loan Information",
                    Toast.LENGTH_SHORT).show()
            disableAllFields()
        }
    }

    private fun disableAllFields() {
        formValidation.disableAssetLiabilityFields(binding)
    }

    private fun setCoApplicants() {
        applicantMenu.add("Applicant")
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapterPersonal = PersonalApplicantsAdapter(context!!, applicantMenu)
        applicantAdapterPersonal!!.setOnItemClickListener(this)
        binding.rcApplicants.adapter = applicantAdapterPersonal
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
        binding.btnAddApplicant.setOnClickListener {
            onAddApplicantClick()
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

    private fun onAddApplicantClick() {
//        checkMandatoryField()
        applicantMenu.add("Co- Applicant $coApplicant")
        binding.rcApplicants.adapter!!.notifyDataSetChanged()
        coApplicant++
    }

    private fun changeCurrentApplicant() {
    }

}