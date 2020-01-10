package com.finance.app.view.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import com.finance.app.R
import com.finance.app.databinding.ActivityAllLeadsBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.presenter.connector.AllLeadsConnector
import com.finance.app.presenter.presenter.GetAllLeadsPresenter
import com.finance.app.view.adapters.recycler.adapter.LeadPagerAdapter
import com.finance.app.view.fragment.AllLeadsFragment
import com.finance.app.view.fragment.PendingLeadsFragment
import com.finance.app.view.fragment.RejectedLeadFragment
import com.finance.app.view.fragment.SubmittedLeadFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class AllLeadActivity : BaseAppCompatActivity(), AllLeadsConnector.AllLeads {

    private val binding: ActivityAllLeadsBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_all_leads)

    @Inject
    lateinit var dataBase: DataBaseUtil
    private var pagerAdapter: LeadPagerAdapter? = null
    private var presenter = GetAllLeadsPresenter(this)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AllLeadActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        hideSecondaryToolbar()
        presenter.callNetwork(ConstantsApi.CALL_GET_ALL_LEADS)
        refreshPage()
        binding.btnCreate.setOnClickListener {
            CreateLeadActivity.start(this)
        }
    }

    private fun refreshPage() {
        binding.refresh.setOnRefreshListener {
            presenter.callNetwork(ConstantsApi.CALL_GET_ALL_LEADS)
            binding.refresh.isRefreshing = false }
    }

    override fun getAllLeadsSuccess(value: Response.ResponseGetAllLeads){
        GlobalScope.launch {
            dataBase.provideDataBaseSource().allLeadsDao().deleteAllLeadMaster()
        }

        val progress = ProgressDialog(this)
        progress.setMessage("Getting Leads")
        progress.show()
        Handler().postDelayed({
            saveDataToDB(value.responseObj)
            if (progress.isShowing) {
                progress.dismiss()
            } else { }
        }, 1000)
    }

    override fun getAllLeadsFailure(msg: String) = setUpLeadFragments()

    private fun saveDataToDB(leads: ArrayList<AllLeadMaster>) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().allLeadsDao().insertLeadsList(leads)
        }
        setUpLeadFragments()
    }

    private fun setUpLeadFragments() {
        pagerAdapter = LeadPagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragment(PendingLeadsFragment(), "Pending")
        pagerAdapter!!.addFragment(SubmittedLeadFragment(), "Submitted")
        pagerAdapter!!.addFragment(RejectedLeadFragment(), "Rejected")
        pagerAdapter!!.addFragment(AllLeadsFragment(), "All Leads")
        binding.viewPager.adapter = pagerAdapter
        binding.tabLead.setupWithViewPager(binding.viewPager)
    }
}
