package com.finance.app.view.adapters.recycler.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.view.fragment.loanApplicationFragments.PersonalFormFragmentNew
import java.util.ArrayList

class PersonalPagerAdapter internal constructor(fm: FragmentManager, val coApplicantsList: ArrayList<PersonalApplicantsModel>) : FragmentStatePagerAdapter(fm) {
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    private val hmFragments = HashMap<Int, PersonalFormFragmentNew>()

    override fun getItem(position: Int): Fragment {

        val fragmentItem = PersonalFormFragmentNew.newInstance(coApplicantsList[position], position)

        hmFragments[position] = fragmentItem
        return fragmentItem
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "CoApplicant ${position + 1}"
    }

    override fun getCount(): Int {
        return coApplicantsList.size
    }

    fun getAllFragments() = hmFragments
}