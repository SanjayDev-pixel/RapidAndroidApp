package com.finance.app.view.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.databinding.FragmentAssetLiablityBinding
import com.finance.app.model.Modals
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.persistence.model.PersonalApplicants
import com.finance.app.view.adapters.Recycler.Adapter.*

class AssetLiabilityFragment : Fragment(), ApplicantsAdapter.ItemClickListener {
    private lateinit var binding: FragmentAssetLiablityBinding
    private lateinit var mContext: Context
    private var applicantAdapter: ApplicantsAdapter? = null
    private var applicantsList: ArrayList<PersonalApplicants>? = null

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
        applicantMenu = ArrayList()
        setDropDownValue()
        setCoApplicants()
        setClickListeners()
    }

    private fun setCoApplicants() {
        applicantMenu.add("Applicant")
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapter = ApplicantsAdapter(context!!, applicantMenu)
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

        binding.spinnerAssetSubType.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.spinnerAssetType.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.spinnerDocumentProof.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.spinnerOwnership.adapter = GenericSpinnerAdapter(mContext, lists)

        binding.layoutObligations.spinnerLoanOwnership.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.layoutObligations.spinnerLoanType.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.layoutObligations.spinnerObligate.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.layoutObligations.spinnerRepaymentBank.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.layoutObligations.spinnerEmiPaidInSameMonth.adapter = GenericSpinnerAdapter(mContext, lists)

        binding.layoutCreditCard.spinnerBankName.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.layoutCreditCard.spinnerObligate.adapter = GenericSpinnerAdapter(mContext, lists)
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