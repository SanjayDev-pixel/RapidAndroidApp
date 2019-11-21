package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.FragmentPropertyBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment

class PropertyFragment : BaseFragment() {
    private lateinit var binding: FragmentPropertyBinding
    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_property)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        mContext = context!!
        setDropDownValue()
    }


    private fun setDropDownValue() {
        val lists: ArrayList<DropdownMaster> = ArrayList()

        binding.spinnerIsFirstProperty.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerOwnedProperty.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerOwnership.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerState.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerUnitType.adapter = MasterSpinnerAdapter(mContext, lists)
    }
}