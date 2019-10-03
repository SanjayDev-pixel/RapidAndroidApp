package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.databinding.FragmentReferenceBinding
import com.finance.app.model.Modals
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.view.adapters.Recycler.Adapter.GenericSpinnerAdapter

class ReferenceFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentReferenceBinding
    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReferenceBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDropDownValue()
    }

    private fun setDropDownValue() {
        val lists: ArrayList<DropdownMaster> = ArrayList()

        binding.spinnerOccupation.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.spinnerRelation.adapter = GenericSpinnerAdapter(mContext, lists)
    }
}