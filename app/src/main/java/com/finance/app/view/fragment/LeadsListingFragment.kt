package com.finance.app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentLeadPendingBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.view.adapters.recycler.adapter.LeadListingAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import javax.inject.Inject

class LeadsListingFragment : BaseFragment() {
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding:FragmentLeadPendingBinding

    companion object {

        const val KEY_LEAD_STATUS = "leadStatus"
        fun newInstance(leadStatusEnum: AppEnums.LEAD_TYPE): LeadsListingFragment {
            val args = Bundle()
            args.putSerializable(KEY_LEAD_STATUS, leadStatusEnum)

            val fragment = LeadsListingFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private var leadStatusEnum: AppEnums.LEAD_TYPE? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_lead_pending)
        init()
        return binding.root
    }


    override fun init() {
        ArchitectureApp.instance.component.inject(this)

        val leadStatus = arguments?.getSerializable(KEY_LEAD_STATUS)
        leadStatus?.let {
            leadStatusEnum = leadStatus as AppEnums.LEAD_TYPE
        }

        getLeadsFromDB()
    }

    private fun getLeadsFromDB() {

        leadStatusEnum?.let {

            var liveDataLeads = when(leadStatusEnum) {
                AppEnums.LEAD_TYPE.ALL -> dataBase.provideDataBaseSource().allLeadsDao().getAllLeads()
                else -> dataBase.provideDataBaseSource().allLeadsDao().getLeadsByStatus(leadStatusEnum!!.type)
            }
            liveDataLeads
                    .observe(viewLifecycleOwner, Observer { leads ->
                        leads.let {
                            val allLeadList = ArrayList(it)
                            if (allLeadList.size > 0) {
                                setUpRecyclerView(allLeadList)
                            }
                        }
                    })
        }
    }

    private fun setUpRecyclerView(allLeadList: ArrayList<AllLeadMaster>) {
        binding.rcPendingLeads.layoutManager = LinearLayoutManager(this.activity)
        binding.rcPendingLeads.adapter = LeadListingAdapter(this.requireActivity(), allLeadList)
    }

}