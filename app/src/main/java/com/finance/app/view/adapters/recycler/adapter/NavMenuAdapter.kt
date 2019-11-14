package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemNavBinding
import com.finance.app.others.AppEnums
import com.finance.app.view.fragment.*
import motobeans.architecture.util.exGone
import motobeans.architecture.util.exVisible

class NavMenuAdapter(private val mContext: Context, private val navListItem: List<AppEnums.ScreenLoanInfo>) : RecyclerView.Adapter<NavDrawerViewHolder>() {

    private lateinit var binding: ItemNavBinding
    private var isExpanded = true
    private var selectedNav: AppEnums.ScreenLoanInfo = AppEnums.ScreenLoanInfo.DEFAULT

    init {
        when (navListItem.size > 0) {
            true -> selectedNav = navListItem[0]

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavDrawerViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_nav, parent, false)
        return NavDrawerViewHolder(binding, mContext)
    }

    override fun getItemCount() = navListItem.size

    override fun onBindViewHolder(holder: NavDrawerViewHolder, position: Int) {
        val navItem = navListItem[position]
        holder.bindItems(position = position, navItem = navItem, selectedNavItem = selectedNav)

        binding.root.setOnClickListener {
            val navItemNew = navListItem[position]
            selectedNav = navItem
            println("Munish Thakur -> NavItem -> SELECTED at position: $position-> $navItemNew ($navItem)")
            //navigateToAnotherFragmentOnIconCLick(navItem)
            toggleMenu()
        }
    }

    fun isMenuExpanded(): Boolean {
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

class NavDrawerViewHolder(val binding: ItemNavBinding, val mContext: Context) : RecyclerView.ViewHolder(binding.root) {

    fun bindItems(position: Int, navItem: AppEnums.ScreenLoanInfo, selectedNavItem: AppEnums.ScreenLoanInfo) {
        val navIcon = navItem.icon
        //var title = if (isExpanded) navItem.screenName else ""
        var title = navItem.screenName

        binding.tvNavItem.text = title
        binding.iconNavItem.setImageResource(navIcon)

        changeColorBasedOnSelection(navItem = navItem, selectedNavItem = selectedNavItem)
    }

    private fun changeColorBasedOnSelection(navItem: AppEnums.ScreenLoanInfo, selectedNavItem: AppEnums.ScreenLoanInfo) {
        if (navItem.isMatch(selectedNavItem)) {
            println("-------------")
            println("Munish Thakur -> NavItem -> COLORED -> $navItem, Selected: $selectedNavItem")
            println("-------------")

            binding.tvTempHighlight.exVisible()
            binding.iconNavItem.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary),
                    android.graphics.PorterDuff.Mode.MULTIPLY)
            binding.tvNavItem.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
            binding.parent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))

        } else {
            binding.tvTempHighlight.exGone()
            println("Munish Thakur -> NavItem -> NOT-COLORED -> $navItem")
            binding.iconNavItem.setColorFilter(ContextCompat.getColor(mContext, R.color.white),
                    android.graphics.PorterDuff.Mode.MULTIPLY)
            binding.tvNavItem.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            binding.parent.setBackgroundResource(R.drawable.drawer_gradient_color)
        }
    }

    private fun navigateToAnotherFragmentOnIconCLick(navData: AppEnums.ScreenLoanInfo) {
        when (navData) {
            AppEnums.ScreenLoanInfo.LOAN_INFORMATION -> updateSecondaryFragment(LoanInfoFragment())
            AppEnums.ScreenLoanInfo.PERSONAL -> updateSecondaryFragment(PersonalInfoFragment())
            AppEnums.ScreenLoanInfo.EMPLOYMENT -> updateSecondaryFragment(EmploymentFragment())
            AppEnums.ScreenLoanInfo.BANK_DETAIL -> updateSecondaryFragment(BankDetailFragment())
            AppEnums.ScreenLoanInfo.LIABILITY_AND_ASSET -> updateSecondaryFragment(AssetLiabilityFragment())
            AppEnums.ScreenLoanInfo.REFERENCE -> updateSecondaryFragment(ReferenceFragment())
            AppEnums.ScreenLoanInfo.PROPERTY -> updateSecondaryFragment(PropertyFragment())
            AppEnums.ScreenLoanInfo.DOCUMENT_CHECKLIST -> updateSecondaryFragment(DocumentCheckListFragment())
        }
    }

    private fun updateSecondaryFragment(fragment: Fragment) {
        /*val ft = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
            replace(R.id.secondaryFragmentContainer, fragment)
            addToBackStack(null)
        }
        ft.commit()*/
    }
}
