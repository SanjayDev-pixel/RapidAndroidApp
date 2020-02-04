package com.finance.app.view.adapters.recycler.adapter

import android.util.SparseArray
import androidx.core.util.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.view.fragment.loanApplicationFragments.PersonalFormFragmentNew
import java.util.*

class PersonalPagerAdapter internal constructor(fm: FragmentManager, val coApplicantsList: ArrayList<PersonalApplicantsModel>) : FragmentStatePagerAdapter(fm) {

    private val hmFragments = SparseArray<PersonalFormFragmentNew>()

    init {
        if (coApplicantsList.isNullOrEmpty()) {
            coApplicantsList.add(PersonalApplicantsModel())
        }
    }

    override fun getItem(position: Int): Fragment {

        val fragmentItem = PersonalFormFragmentNew.newInstance(coApplicantsList[position], position)
        hmFragments[position] = fragmentItem
        return fragmentItem
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) "Applicant"
        else "CoApplicant $position"
    }

    override fun getCount(): Int {
        return coApplicantsList.size
    }

    fun getAllFragments() = hmFragments
}