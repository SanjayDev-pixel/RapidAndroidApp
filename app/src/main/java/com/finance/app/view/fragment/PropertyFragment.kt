package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.databinding.FragmentPropertyBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.view.adapters.Recycler.Adapter.MasterSpinnerAdapter

class PropertyFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentPropertyBinding
    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPropertyBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDropDownValue()
    }

    private fun setDropDownValue() {
        val lists: ArrayList<DropdownMaster> = ArrayList()

        binding.spinnerIsFirstProperty.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerOwnedProperty.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerOwnership.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerPropertySelected.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerState.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerUnitType.adapter = MasterSpinnerAdapter(mContext, lists)
    }
}