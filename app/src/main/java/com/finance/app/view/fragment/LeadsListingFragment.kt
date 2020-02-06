package com.finance.app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentLeadListingBinding
import com.finance.app.others.AppEnums
import com.finance.app.others.Injection
import com.finance.app.view.adapters.recycler.adapter.LeadListingAdapter
import com.finance.app.viewModel.AppDataViewModel
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import javax.inject.Inject

class LeadsListingFragment : BaseFragment() {
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentLeadListingBinding
    private lateinit var appDataViewModel: AppDataViewModel

    companion object {
        const val KEY_LEAD_STATUS = "leadStatus"

        fun newInstance(leadStatusEnum: AppEnums.LEAD_TYPE?): LeadsListingFragment {
            val fragment = LeadsListingFragment()
            leadStatusEnum?.let {
                val args = Bundle()
                args.putSerializable(KEY_LEAD_STATUS, leadStatusEnum)
                fragment.arguments = args
            }
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_lead_listing)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        val viewModelFactory: ViewModelProvider.Factory = Injection.provideViewModelFactory(activity!!)
        appDataViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AppDataViewModel::class.java)
        arguments?.getSerializable(KEY_LEAD_STATUS)?.let { leadStatus ->
            val leadStatusEnum = leadStatus as AppEnums.LEAD_TYPE
            setUpRecyclerView(appDataViewModel, leadStatusEnum)
        }
    }

    private fun setUpRecyclerView(appDataViewModel: AppDataViewModel, leadStatusEnum: AppEnums.LEAD_TYPE) {
        binding.rcLeads.layoutManager = LinearLayoutManager(this.activity)
        binding.rcLeads.adapter = LeadListingAdapter(this.requireActivity(), appDataViewModel, leadStatusEnum)
    }
}