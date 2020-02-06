package com.finance.app.view.fragment.loanApplicationFragments.bank

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.BankDetailFormPagerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import javax.inject.Inject


/**
 * Created by Ajay on 28/1/2020.
 */
class BankDetailFragmentNew : BaseFragment() {
    @Inject
    lateinit var dataBase: DataBaseUtil

    private lateinit var mContext: Context
    private var leadDetails: AllLeadMaster? = null

    private var bankDetailFormPagerAdapter: BankDetailFormPagerAdapter? = null


    companion object {
        fun newInstance(): BankDetailFragmentNew {
            return BankDetailFragmentNew()
        }
    }

    private lateinit var binding: FragmentBankDetailBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        ArchitectureApp.instance.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_bank_detail)
        binding.lifecycleOwner = this

        initViews()
        setOnClickListeners()

        return view
    }

    override fun init() {
    }

    private fun initViews() {
    }

    private fun setOnClickListeners() {
        binding.btnNext.setOnClickListener {
            bankDetailFormPagerAdapter?.let {
                LeadMetaData().saveBankData(it.getBankDetailsList())
                AppEvents.fireEventLoanAppChangeNavFragmentNext()
            }
        }
        binding.btnPrevious.setOnClickListener { AppEvents.fireEventLoanAppChangeNavFragmentPrevious() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Now fetch data from where-ever you want....
        fetchLeadDetails()
    }

    private fun setApplicantTabLayout(applicantList: ArrayList<PersonalApplicantsModel>?) {
        applicantList.let { list ->
            list?.let {
                bankDetailFormPagerAdapter = BankDetailFormPagerAdapter(fragmentManager!!, it)
                binding.vpBankDetailForm.adapter = bankDetailFormPagerAdapter
                binding.tabLead.setupWithViewPager(binding.vpBankDetailForm)
            }
        }
    }

    private fun fetchLeadDetails() {
        LeadMetaData.getLeadObservable().observe(this, Observer { leadDetails ->
            leadDetails?.let {
                this@BankDetailFragmentNew.leadDetails = it
                //Set Applicant Tabs View.
                setApplicantTabLayout(it.personalData?.applicantDetails)
            }
        })
    }

}
