package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.databinding.FragmentAssetLiablityBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.view.adapters.Recycler.Adapter.GenericSpinnerAdapter

class AssetLiablityFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentAssetLiablityBinding
    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAssetLiablityBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDropDownValue()
    }

    private fun setDropDownValue() {
        val lists: ArrayList<DropdownMaster> = ArrayList()
        lists.add(DropdownMaster())
        lists.add(DropdownMaster())
        lists.add(DropdownMaster())

        binding.spinnerAssetSubType.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.spinnerAssetType.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.spinnerDocumentProof.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.spinnerOwnership.adapter = GenericSpinnerAdapter(mContext, lists)

        binding.layoutObligations.spinnerLoanObservation.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.layoutObligations.spinnerLoanType.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.layoutObligations.spinnerObligate.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.layoutObligations.spinnerRepaymentBank.adapter = GenericSpinnerAdapter(mContext, lists)

        binding.layoutCreditCard.spinnerBankName.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.layoutCreditCard.spinnerObligate.adapter = GenericSpinnerAdapter(mContext, lists)
    }
}