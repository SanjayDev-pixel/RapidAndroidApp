package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.databinding.FragmentEmploymentBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.utility.SelectDate
import com.finance.app.view.adapters.Recycler.Adapter.GenericSpinnerAdapter

class EmploymentFragment : androidx.fragment.app.Fragment(){
    private lateinit var binding: FragmentEmploymentBinding
    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEmploymentBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDatePicker()
        setDropDownValue()
    }

    private fun setDatePicker() {
        binding.layoutSalary.etJoiningDate.setOnClickListener {
            SelectDate(binding.layoutSalary.etJoiningDate, mContext)
        }
    }

    private fun setDropDownValue() {
        val lists: ArrayList<DropdownMaster> = ArrayList()
        lists.add(DropdownMaster())
        lists.add(DropdownMaster())
        lists.add(DropdownMaster())

        binding.spinnerProfileSegment.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.spinnerSubProfile.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.layoutSalary.spinnerIndustry.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.layoutSalary.spinnerSector.adapter = GenericSpinnerAdapter(mContext, lists)
    }
}