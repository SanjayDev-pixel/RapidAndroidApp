package com.finance.app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentLeadSubmittedBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.view.adapters.recycler.adapter.LeadListingAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import javax.inject.Inject

class SubmittedLeadFragment : BaseFragment() {
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentLeadSubmittedBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_lead_submitted)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        getLeadsFromDB()
    }

    private fun getLeadsFromDB() {
        dataBase.provideDataBaseSource().allLeadsDao().getSubmittedLeads()
                .observe(viewLifecycleOwner, Observer { leads ->
                    leads.let {
                        val allLeadList = ArrayList(it)
                        if (allLeadList.size > 0) {
                            setUpRecyclerView(allLeadList)
                        }
                    }
                })
    }

    private fun setUpRecyclerView(allLeadList: ArrayList<AllLeadMaster>) {
        binding.rcSubmittedLeads.layoutManager = LinearLayoutManager(this.activity)
        binding.rcSubmittedLeads.adapter = LeadListingAdapter(this.requireActivity(), allLeadList)
    }
}