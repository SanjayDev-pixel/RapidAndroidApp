package com.finance.app.view.adapters.Recycler.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.NavItemBinding
import com.finance.app.model.Modals
import com.finance.app.view.fragment.*

class NavMenuAdapter(private val c: Context, private val navItem: ArrayList<Modals.NavItems>) : RecyclerView.Adapter<NavMenuAdapter.NavDrawerViewHolder>() {

    private lateinit var binding: NavItemBinding

    companion object {
        var selectedPos = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavDrawerViewHolder {
        val layoutInflater = LayoutInflater.from(c)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.nav_item, parent, false)
        return NavDrawerViewHolder(binding, c)
    }

    override fun getItemCount() = navItem.size

    override fun onBindViewHolder(holder: NavDrawerViewHolder, position: Int) {
        holder.bindItems(navItem[position], position)
    }

    inner class NavDrawerViewHolder(val binding: NavItemBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bindItems(navItem: Modals.NavItems, position: Int) {
            binding.tvNavItem.text = navItem.title
            binding.iconNavItem.setImageResource(navItem.images)
            changeColorBasedOnSelection(position)
            binding.tvNavItem.setOnClickListener {
                navigateToAnotherFragment(navItem.title)
                selectedPos = adapterPosition
                notifyDataSetChanged()
            }
        }

        private fun navigateToAnotherFragment(title: String) {
            when (title) {
                "Loan Information" -> updateSecondaryFragment(LoanInformationFragment())
                "Personal" -> updateSecondaryFragment(PersonalInfoFragment())
                "Employment" -> updateSecondaryFragment(EmploymentFragment())
                "Income" -> updateSecondaryFragment(IncomeFragment())
                "Bank Details" -> updateSecondaryFragment(BankDetailFragment())
                "Liability & Asset" -> updateSecondaryFragment(LiabilityAndAssetFragment())
                "Reference" -> updateSecondaryFragment(ReferenceFragment())
                "Property" -> updateSecondaryFragment(PropertyFragment())
                "Login Fee" -> updateSecondaryFragment(LoginFeeFragment())
            }
        }

        private fun changeColorBasedOnSelection(position: Int) {
            if (selectedPos == position) {
                binding.iconNavItem.setColorFilter(ContextCompat.getColor(c, R.color.colorPrimary),
                        android.graphics.PorterDuff.Mode.MULTIPLY)
                binding.parent.setBackgroundColor(ContextCompat.getColor(c, R.color.white))
                binding.tvNavItem.setTextColor(ContextCompat.getColor(c, R.color.colorPrimary))

            } else {
                binding.iconNavItem.setColorFilter(ContextCompat.getColor(c, R.color.white),
                        android.graphics.PorterDuff.Mode.MULTIPLY)
                binding.parent.setBackgroundColor(ContextCompat.getColor(c, R.color.colorPrimary))
                binding.tvNavItem.setTextColor(ContextCompat.getColor(c, R.color.white))
            }
        }

        private fun updateSecondaryFragment(fragment: Fragment) {
            val ft = (c as AppCompatActivity).supportFragmentManager.beginTransaction()
            ft.add(R.id.secondaryFragmentContainer, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
    }
}
