package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemNavBinding
import com.finance.app.others.AppEnums
import com.finance.app.view.adapters.recycler.Holder.NavDrawerViewHolder
import com.finance.app.view.fragment.*

interface NavMenuConnector {
    fun isMenuExpanded(): Boolean
    fun getSelectionScreenName(): String
    fun rootViewClicked(position: Int, navItem: AppEnums.ScreenLoanInfo)
}

class NavMenuAdapter(private val mContext: Context, private val navListItem: List<AppEnums.ScreenLoanInfo>) : RecyclerView.Adapter<NavDrawerViewHolder>(), NavMenuConnector {

    private lateinit var binding: ItemNavBinding
    private var isExpanded = true

    private var selectedNavString = ""

    init {
        when (navListItem.isNotEmpty()) {
            true -> selectedNavString = navListItem[0].screenName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavDrawerViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_nav, parent, false)
        return NavDrawerViewHolder(binding, mContext, this)
    }

    override fun getItemCount() = navListItem.size

    override fun onBindViewHolder(holder: NavDrawerViewHolder, position: Int) {
        val navItem = navListItem[position]
        holder.bindItems(position = position, navItem = navItem)
    }

    override fun rootViewClicked(position: Int, navItem: AppEnums.ScreenLoanInfo) {
        selectedNavString = navItem.screenName
        navigateToAnotherFragmentOnIconCLick(navItem)
        if(isMenuExpanded()) {
            toggleMenu()
        }
        notifyDataSetChanged()
    }

    private fun navigateToAnotherFragmentOnIconCLick(navData: AppEnums.ScreenLoanInfo) {
        when (navData) {
            AppEnums.ScreenLoanInfo.LOAN_INFORMATION -> updateSecondaryFragment(LoanInfoFragment())
            AppEnums.ScreenLoanInfo.PERSONAL -> updateSecondaryFragment(PersonalInfoFragment())
            AppEnums.ScreenLoanInfo.EMPLOYMENT -> updateSecondaryFragment(EmploymentInfoFragment())
            AppEnums.ScreenLoanInfo.BANK_DETAIL -> updateSecondaryFragment(BankDetailFragment())
            AppEnums.ScreenLoanInfo.LIABILITY_AND_ASSET -> updateSecondaryFragment(AssetLiabilityFragment())
            AppEnums.ScreenLoanInfo.REFERENCE -> updateSecondaryFragment(ReferenceFragment())
            AppEnums.ScreenLoanInfo.PROPERTY -> updateSecondaryFragment(PropertyFragment())
            AppEnums.ScreenLoanInfo.DOCUMENT_CHECKLIST -> updateSecondaryFragment(DocumentCheckListFragment())
        }
    }

    private fun updateSecondaryFragment(fragment: Fragment) {
        val ft = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
            replace(R.id.secondaryFragmentContainer, fragment)
            addToBackStack(null)
        }
        ft.commit()
    }

    override fun getSelectionScreenName(): String {
        return selectedNavString
    }

    override fun isMenuExpanded(): Boolean {
        return isExpanded
    }

    fun setMenuExpanded() {
        toggleMenu()
    }

    private fun toggleMenu() {
        isExpanded = !isExpanded
        notifyDataSetChanged()
    }
}
