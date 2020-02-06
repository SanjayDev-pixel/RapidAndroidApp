package com.finance.app.view.adapters.recycler.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.finance.app.persistence.model.BankDetailModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.view.fragment.loanApplicationFragments.bank.BankDetailFormFragment

class BankDetailFormPagerAdapter(fm: FragmentManager, private val applicantsList: ArrayList<PersonalApplicantsModel>) : FragmentStatePagerAdapter(fm) {

    private val fragmentList = ArrayList<BankDetailFormFragment>()

    override fun getCount() = applicantsList.size

    override fun getPageTitle(position: Int) = if (applicantsList[position].isMainApplicant) "Applicant" else "CoApplicant $position"

    override fun getItem(position: Int): BankDetailFormFragment {
        if (fragmentList.isNotEmpty() && fragmentList.size > position)
            return fragmentList[position]

        val fragment = BankDetailFormFragment.newInstance(applicantsList[position])
        fragmentList.add(fragment)
        return fragment //        return BankDetailFormFragment.newInstance(applicantsList[position])
    }

    fun getBankDetailsList(): ArrayList<BankDetailModel> {
        val list = ArrayList<BankDetailModel>()
        fragmentList.forEachIndexed { index, _ ->
            list.add(getItem(index).getApplicantBankDetails())
        }
        return list
    }


}