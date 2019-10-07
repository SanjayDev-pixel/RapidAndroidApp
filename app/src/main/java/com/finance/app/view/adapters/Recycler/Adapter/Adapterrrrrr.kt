package com.finance.app.view.adapters.Recycler.Adapter
import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.GridCellBinding
import com.finance.app.model.Modals
import com.finance.app.view.activity.LoanApplicationActivity
import com.finance.app.view.fragment.*

class Adapterrrrrr(private val mContext: Context, private val navItem: HashMap<String, Int>,
                     private val expanded: Boolean) : RecyclerView.Adapter<Adapterrrrrr.NavDrawerViewHolder>() {

    private lateinit var binding: GridCellBinding
    private val mKeys = ArrayList<String>(navItem.keys)

    companion object {
        var selectedPos = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavDrawerViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.grid_cell, parent, false)
        return NavDrawerViewHolder(binding, mContext)
    }

    override fun getItemCount() = navItem.size

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

    override fun onBindViewHolder(holder: NavDrawerViewHolder, position: Int) {
        if (expanded) {
            holder.bindItems(convertToNavObject(position), position)
        } else {
            holder.bindItems(convertToIconObject(position), position)
        }
    }

    inner class NavDrawerViewHolder(val binding: GridCellBinding, val mContext: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bindItems(navItem: Modals.NavItems, position: Int) {
            binding.title.text = navItem.title
            if (selectedPos == position) {
                binding.title.setCompoundDrawablesRelativeWithIntrinsicBounds(navItem.image, 0, 0, 0)
                val leftDrawable = binding.title.compoundDrawablesRelative[0]
                leftDrawable.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
                binding.title.setCompoundDrawablesRelativeWithIntrinsicBounds(leftDrawable, null, null, null)
                binding.parent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
                binding.title.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

            } else {
                binding.title.setCompoundDrawablesRelativeWithIntrinsicBounds(navItem.image, 0, 0, 0)
                binding.parent.setBackgroundResource(R.drawable.drawer_gradient_color)
                binding.title.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            }
            binding.title.setOnClickListener {
                changeFragment(navItem.title)
                notifyDataSetChanged()
            }
        }

        private fun changeFragment(title: String) {
            navigateToAnotherFragment(title)
            (mContext as LoanApplicationActivity).handleCollapseScreen(false)
            selectedPos = adapterPosition
        }

        private fun navigateToAnotherFragment(title: String) {
            when (title) {
                "Loan Information" -> updateSecondaryFragment(LoanInformationFragment())
                "Personal" -> updateSecondaryFragment(PersonalInfoFragment())
                "Employment" -> updateSecondaryFragment(EmploymentFragment())
//                "Income" -> updateSecondaryFragment(IncomeFragment())
                "Bank Details" -> updateSecondaryFragment(BankDetailFragment())
                "Liability & Asset" -> updateSecondaryFragment(AssetLiabilityFragment())
                "Reference" -> updateSecondaryFragment(ReferenceFragment())
                "Property" -> updateSecondaryFragment(PropertyFragment())
                "Document Checklist" -> updateSecondaryFragment(DocumentCheckListFragment())
            }
        }

        private fun updateSecondaryFragment(fragment: Fragment) {
            val ft = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
            ft.replace(R.id.secondaryFragmentContainer, fragment)
            ft.commit()
        }
    }
}
