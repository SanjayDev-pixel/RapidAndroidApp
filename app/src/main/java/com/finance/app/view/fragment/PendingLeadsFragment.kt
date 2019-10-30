package com.finance.app.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentAllLeadsBinding
import com.finance.app.databinding.FragmentLeadPendingBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.view.activity.AddLeadActivity
import com.finance.app.view.adapters.recycler.adapter.LeadListingAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class PendingLeadsFragment : BaseFragment() {
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding:FragmentLeadPendingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_lead_pending)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        getLeadsFromDB()
    }

    private fun getLeadsFromDB() {
        dataBase.provideDataBaseSource().allLeadsDao().getPendingLeads()
                .observe(viewLifecycleOwner, Observer { leads ->
                    leads.let {
                        val allLeadList = ArrayList(it)
                        if (allLeadList.size > 0) {
                            setUpRecyclerView(allLeadList)
                        } else {
                            showToast(getString(R.string.failure_get_all_leads))
                        }
                    }
                })
    }

    private fun setUpRecyclerView(allLeadList: ArrayList<AllLeadMaster>) {
        binding.rcPendingLeads.layoutManager = LinearLayoutManager(this.activity)
        binding.rcPendingLeads.adapter = LeadListingAdapter(this.requireActivity(), allLeadList)
    }

}