package com.finance.app.view.adapters.recycler.adapter

import android.util.SparseArray
import androidx.core.util.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.finance.app.persistence.model.AssetLiabilityModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.view.fragment.loanApplicationFragments.AssetLiabilityFragmentForm
import com.finance.app.view.fragment.loanApplicationFragments.AssetLiabilityFragmentNew

class AssetLiabilityPagerAdapter internal constructor(fm: FragmentManager, val coApplicantsList: ArrayList<AssetLiabilityModel>) : FragmentStatePagerAdapter(fm) {

    private val hmFragments = SparseArray<AssetLiabilityFragmentForm>()

    override fun getItem(position: Int): Fragment {

        val fragmentItem = AssetLiabilityFragmentForm.newInstance(coApplicantsList[position], position)

        hmFragments[position] = fragmentItem
        return fragmentItem
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "CoApplicant ${position + 1}"
    }

    override fun getCount(): Int {
        return coApplicantsList.size
    }

    fun getAllFragments() = hmFragments
}