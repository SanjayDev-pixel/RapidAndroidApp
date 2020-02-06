package com.finance.app.view.adapters.recycler.adapter

import android.util.SparseArray
import androidx.core.util.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.finance.app.persistence.model.EmploymentApplicantsModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.view.fragment.loanApplicationFragments.EmploymentFormFragmentNew

class EmploymentPagerAdapter internal constructor(fm: FragmentManager,
                                                  val coApplicantsList: ArrayList<EmploymentApplicantsModel>,
                                                  private val pApplicantList: ArrayList<PersonalApplicantsModel>) : FragmentStatePagerAdapter(fm) {

    private val hmFragments = SparseArray<EmploymentFormFragmentNew>()

    init {
        if (coApplicantsList.isNullOrEmpty()) {
            coApplicantsList.add(EmploymentApplicantsModel())
        }
    }

    override fun getItem(position: Int): Fragment {
        val fragmentItem = EmploymentFormFragmentNew
                .newInstance(coApplicantsList[position], position, pApplicantList[position])

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