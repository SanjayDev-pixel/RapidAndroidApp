package com.finance.app.view.adapters.recycler.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.finance.app.persistence.model.AssetLiabilityModel
import com.finance.app.persistence.model.DocumentCheckListModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.view.fragment.loanApplicationFragments.document_checklist.DocumentChecklistForm

class DocumentCheckLIstPagerAdapter  internal constructor(fm: FragmentManager, val applicantsList: ArrayList<PersonalApplicantsModel>) : FragmentStatePagerAdapter(fm) {

    private val fragmentList = ArrayList<DocumentChecklistForm>()

    override fun getPageTitle(position: Int): CharSequence? = if (applicantsList[position].isMainApplicant) "Applicant" else "CoApplicant ${position}"

    override fun getCount() = applicantsList.size

    override fun getItem(position: Int): DocumentChecklistForm {
        if (fragmentList.isNotEmpty() && fragmentList.size > position)
            return fragmentList[position]

        val fragmentItem = DocumentChecklistForm.newInstance(applicantsList[position])
        fragmentList.add(fragmentItem)
        return fragmentItem
    }

//model change to Documentchecklist model
    fun getALlChecklistDetail(): ArrayList<DocumentCheckListModel> {
        val list = ArrayList<DocumentCheckListModel>()
        fragmentList.forEach { it ->
            list.add(it.getDocumentChecklist())
        }

        return list
    }

    fun getAllFragments() = fragmentList
}