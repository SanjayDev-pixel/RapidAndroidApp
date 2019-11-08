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
import com.finance.app.model.Modals
import com.finance.app.view.activity.LoanApplicationActivity
import com.finance.app.view.fragment.*

class NavMenuAdapter(private val mContext: Context, private val navItem: HashMap<String, Int>,
                     private val expanded: Boolean) : RecyclerView.Adapter<NavMenuAdapter.NavDrawerViewHolder>() {

    private lateinit var binding: ItemNavBinding
    private val mKeys = ArrayList<String>(navItem.keys)

    companion object{
        private var selectedPos = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavDrawerViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_nav, parent, false)
        return NavDrawerViewHolder(binding, mContext)
    }

    override fun getItemCount() = navItem.size

    override fun onBindViewHolder(holder: NavDrawerViewHolder, position: Int) {
        changeColorBasedOnSelection(position)
        if (expanded) {
            holder.bindItems(convertToNavObject(position))
        } else {
            holder.bindItems(convertToIconObject(position))
        }
    }

    private fun convertToIconObject(position: Int): Modals.NavItems {
        val key = navItem[mKeys[position]]
        val value = ""
        return Modals.NavItems(key!!, value)
    }

    private fun convertToNavObject(position: Int): Modals.NavItems {
        val key = navItem[mKeys[position]]
        val value = mKeys[position]
        return Modals.NavItems(key!!, value)
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

    inner class NavDrawerViewHolder(val binding: ItemNavBinding, val mContext: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bindItems(navItem: Modals.NavItems) {
            binding.tvNavItem.text = navItem.title
            binding.iconNavItem.setImageResource(navItem.image)
            binding.iconNavItem.setOnClickListener {
                selectedPos = adapterPosition
                changeFragmentOnIconClick(navItem.image)
                notifyDataSetChanged()
            }

            binding.tvNavItem.setOnClickListener {
                selectedPos = adapterPosition
                changeFragment(navItem.title)
                notifyDataSetChanged()
            }
        }

        private fun changeFragment(title: String) {
            navigateToAnotherFragment(title)
            (mContext as LoanApplicationActivity).handleCollapseScreen(false)
        }

        private fun changeFragmentOnIconClick(icon: Int) {
            navigateToAnotherFragmentOnIconCLick(icon)
            (mContext as LoanApplicationActivity).handleCollapseScreen(false)
        }

        private fun navigateToAnotherFragmentOnIconCLick(icon: Int) {
            when (icon) {
                R.drawable.loan_info_white -> updateSecondaryFragment(LoanInfoFragment())
                R.drawable.personal_info_white -> updateSecondaryFragment(PersonalInfoFragment())
                R.drawable.employment_icon_white -> updateSecondaryFragment(EmploymentFragment())
//                R.drawable.income_icon_white -> updateSecondaryFragment(IncomeFragment())
                R.drawable.bank_icon_white -> updateSecondaryFragment(BankDetailFragment())
                R.drawable.assest_details_white -> updateSecondaryFragment(AssetLiabilityFragment())
                R.drawable.reffrence_white -> updateSecondaryFragment(ReferenceFragment())
                R.drawable.property_icon_white -> updateSecondaryFragment(PropertyFragment())
                R.drawable.checklist -> updateSecondaryFragment(DocumentCheckListFragment())
            }
        }

        private fun navigateToAnotherFragment(title: String) {
            when (title) {
                "Loan Information" -> updateSecondaryFragment(LoanInfoFragment())
                "Personal" -> updateSecondaryFragment(PersonalInfoFragment())
                "PostEmployment" -> updateSecondaryFragment(EmploymentFragment())
//                "Income" -> updateSecondaryFragment(IncomeFragment())
                "Bank Details" -> updateSecondaryFragment(BankDetailFragment())
                "Liability & Asset" -> updateSecondaryFragment(AssetLiabilityFragment())
                "Reference" -> updateSecondaryFragment(ReferenceFragment())
                "Property" -> updateSecondaryFragment(PropertyFragment())
                "Document Checklist" -> updateSecondaryFragment(DocumentCheckListFragment())
            }
        }

        private fun updateSecondaryFragment(fragment: Fragment) {
            val ft = (mContext as AppCompatActivity).supportFragmentManager.
                    beginTransaction().apply {
                replace(R.id.secondaryFragmentContainer, fragment)
                addToBackStack(null)
            }
            ft.commit()
        }
    }
}
