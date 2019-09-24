package com.finance.app.view.adapters.Recycler.Adapter
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
import com.finance.app.model.Modals
import com.finance.app.view.activity.LoanApplicationActivity
import com.finance.app.view.fragment.*

class NavMenuAdapter(private val mContext: Context, private val navItem: ArrayList<Modals.NavItems>) : RecyclerView.Adapter<NavMenuAdapter.NavDrawerViewHolder>() {

    private lateinit var binding: ItemNavBinding
    companion object {
        var selectedPos = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavDrawerViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_nav, parent, false)
        return NavDrawerViewHolder(binding, mContext)
    }

    override fun getItemCount() = navItem.size

    override fun onBindViewHolder(holder: NavDrawerViewHolder, position: Int) {
        changeColorBasedOnSelection(position)
        holder.bindItems(navItem[position])
    }

    private fun changeColorBasedOnSelection(position: Int) {
        if (selectedPos == position) {
            binding.iconNavItem.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary),
                    android.graphics.PorterDuff.Mode.MULTIPLY)
            binding.parent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            binding.tvNavItem.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

        } else {
            binding.iconNavItem.setColorFilter(ContextCompat.getColor(mContext, R.color.white),
                    android.graphics.PorterDuff.Mode.MULTIPLY)
            binding.parent.setBackgroundResource(R.drawable.drawer_gradient_color)
            binding.tvNavItem.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        }
    }

    inner class NavDrawerViewHolder(val binding: ItemNavBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bindItems(navItem: Modals.NavItems) {
            binding.tvNavItem.text = navItem.title
            binding.iconNavItem.setImageResource(navItem.images)
            binding.iconNavItem.setOnClickListener {
                changeFragmentOnIconClick(navItem.images)
            }

            binding.tvNavItem.setOnClickListener {
                changeFragment(navItem.title)
            }
        }

        private fun changeFragment(title: String) {
            navigateToAnotherFragment(title)
            (c as LoanApplicationActivity).handleCollapseScreen(false)
            selectedPos = adapterPosition
            notifyDataSetChanged()
        }

        private fun changeFragmentOnIconClick(icon: Int) {
            navigateToAnotherFragmentOnIconCLick(icon)
            (c as LoanApplicationActivity).handleCollapseScreen(false)
            selectedPos = adapterPosition
            notifyDataSetChanged()
        }

        private fun navigateToAnotherFragmentOnIconCLick(icon: Int) {
            when (icon) {
                R.drawable.loan_info_white -> updateSecondaryFragment(LoanInformationFragment())
                R.drawable.personal_info_white -> updateSecondaryFragment(PersonalInfoFragment())
                R.drawable.employment_icon_white -> updateSecondaryFragment(EmploymentFragment())
                R.drawable.income_icon_white -> updateSecondaryFragment(IncomeFragment())
                R.drawable.bank_icon_white -> updateSecondaryFragment(BankDetailFragment())
                R.drawable.assest_details_white -> updateSecondaryFragment(AssetLiabilityFragment())
                R.drawable.reffrence_white -> updateSecondaryFragment(ReferenceFragment())
                R.drawable.property_icon_white -> updateSecondaryFragment(PropertyFragment())
                R.drawable.asigned_lead_black -> updateSecondaryFragment(DocumentCheckListFragment())
            }
        }

        private fun navigateToAnotherFragment(title: String) {
            when (title) {
                "Loan Information" -> updateSecondaryFragment(LoanInformationFragment())
                "Personal" -> updateSecondaryFragment(PersonalInfoFragment())
                "Employment" -> updateSecondaryFragment(EmploymentFragment())
                "Income" -> updateSecondaryFragment(IncomeFragment())
                "Bank Details" -> updateSecondaryFragment(BankDetailFragment())
                "Liability & Asset" -> updateSecondaryFragment(AssetLiabilityFragment())
                "Reference" -> updateSecondaryFragment(ReferenceFragment())
                "Property" -> updateSecondaryFragment(PropertyFragment())
                "Document Checklist" -> updateSecondaryFragment(DocumentCheckListFragment())
            }
        }

        private fun updateSecondaryFragment(fragment: Fragment) {
            val ft = (c as AppCompatActivity).supportFragmentManager.beginTransaction()
            ft.replace(R.id.secondaryFragmentContainer, fragment)
            ft.commit()
        }
    }
}
