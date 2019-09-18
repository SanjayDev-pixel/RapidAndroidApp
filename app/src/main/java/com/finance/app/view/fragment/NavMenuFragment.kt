package com.finance.app.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.FragmentNavMenuBinding
import com.finance.app.model.Modals
import com.finance.app.view.adapters.Recycler.Adapter.NavMenuAdapter

class NavMenuFragment : Fragment() {
    private lateinit var binding: FragmentNavMenuBinding
    private lateinit var navMenuAdapter: NavMenuAdapter

    companion object {
        private lateinit var navItem: ArrayList<Modals.NavItems>
        private lateinit var navItemCopy: ArrayList<Modals.NavItems>
        private lateinit var emptyNavItem: ArrayList<Modals.NavItems>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNavMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navItem = arrayListOf(
                Modals.NavItems(R.drawable.loan_info_white, "Loan Information"),
                Modals.NavItems(R.drawable.personal_info_white, "Personal"),
                Modals.NavItems(R.drawable.employment_icon_white, "Employment"),
                Modals.NavItems(R.drawable.income_icon_white, "Income"),
                Modals.NavItems(R.drawable.bank_icon_white, "Bank Details"),
                Modals.NavItems(R.drawable.assest_details_white, "Liability & Asset"),
                Modals.NavItems(R.drawable.reffrence_white, "Reference"),
                Modals.NavItems(R.drawable.property_icon_white, "Property")
        )
        emptyNavItem = arrayListOf(
                Modals.NavItems(R.drawable.loan_info_white, ""),
                Modals.NavItems(R.drawable.personal_info_white, ""),
                Modals.NavItems(R.drawable.employment_icon_white, ""),
                Modals.NavItems(R.drawable.income_icon_white, ""),
                Modals.NavItems(R.drawable.bank_icon_white, ""),
                Modals.NavItems(R.drawable.assest_details_white, ""),
                Modals.NavItems(R.drawable.reffrence_white, ""),
                Modals.NavItems(R.drawable.property_icon_white, "")
        )
        navItemCopy = ArrayList()
        navItemCopy.addAll(navItem)
        navMenuAdapter = NavMenuAdapter(requireContext(), navItem)
        binding.rcNavMenu.layoutManager = LinearLayoutManager(requireContext())
        binding.rcNavMenu.adapter = navMenuAdapter
    }

    fun notifyMenu(isExpand: Boolean) {
        if (isExpand) {
            navItem.clear()
            navItem.addAll(navItemCopy)
            navMenuAdapter.notifyDataSetChanged()
        } else {
            navItem.clear()
            navItem.addAll(emptyNavItem)
            navMenuAdapter.notifyDataSetChanged()
        }
    }
}