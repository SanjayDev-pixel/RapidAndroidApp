package com.finance.app.view.fragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.databinding.FragmentAllLeadsBinding
import com.finance.app.view.adapters.Recycler.Adapter.LeadListingAdapter

class AllLeadsFragment : Fragment() {

    private lateinit var binding: FragmentAllLeadsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAllLeadsBinding.inflate(inflater, container, false)
        binding.rcAllLeads.layoutManager = LinearLayoutManager(this.activity)
        binding.rcAllLeads.adapter = LeadListingAdapter(this.requireActivity())

        return binding.root
    }
}