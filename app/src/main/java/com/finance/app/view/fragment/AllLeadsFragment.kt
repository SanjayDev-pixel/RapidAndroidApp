package com.finance.app.view.fragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.databinding.FragmentAllLeadsBinding
import com.finance.app.view.activity.AddNewLeadActivity
import com.finance.app.view.adapters.Recycler.Adapter.LeadListingAdapter

class AllLeadsFragment : androidx.fragment.app.Fragment() {

    private lateinit var binding: FragmentAllLeadsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAllLeadsBinding.inflate(inflater, container, false)
        binding.rcAllLeads.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.activity)
        binding.rcAllLeads.adapter = LeadListingAdapter(this.requireActivity())

        binding.fabAddLead.setOnClickListener {
            AddNewLeadActivity.start(requireContext())
        }
        return binding.root
    }
}