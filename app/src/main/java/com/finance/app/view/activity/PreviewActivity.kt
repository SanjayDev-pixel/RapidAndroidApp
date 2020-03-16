package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.finance.app.R
import com.finance.app.databinding.ActivityPreviewBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.ApplicantionSubmitModel
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.PreviewPagerAdapter
import com.finance.app.view.fragment.loanApplicationFragments.FragmentPreview
import kotlinx.android.synthetic.main.activity_preview.view.*
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank
import javax.inject.Inject

class PreviewActivity : BaseAppCompatActivity() {

    private val binding: ActivityPreviewBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_preview
    )

    @Inject
    lateinit var dataBase: DataBaseUtil
    private var pagerAdapter: PreviewPagerAdapter? = null
    private val presenter = Presenter()

    companion object {
        var master: HashMap<AppEnums.DropdownMasterType, ArrayList<DropdownMaster>?>? = null

        fun start(context: Context) {
            val intent = Intent(context, PreviewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        hideToolbar()
        hideSecondaryToolbar()
        getLead()
        setOnClickListeners()

    }

    private fun setOnClickListeners() {
        binding.backbttn.setOnClickListener() {
            this.finish()
        }

        binding.layoutSubmitlead.btnCancel.setOnClickListener(){
            this.finish()
        }
        binding.layoutSubmitlead.btnSubmit.setOnClickListener(){
            val lead: AllLeadMaster? = LeadMetaData.getLeadData()
                checkAndStartLoanApplicationActivity(lead)
        }
    }

    private fun checkAndStartLoanApplicationActivity(lead: AllLeadMaster?) {

        val isLeadOfflineDataSync = lead!!.isDetailAlreadySync

            when (isLeadOfflineDataSync) {
                true -> presenter.callNetwork(ConstantsApi.CALL_FINAL_SUBMIT, CallFinalSubmit())
                false -> Toast.makeText(this, "Please wait lead is syncronise with server", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getLead() {
        val leadData = LeadMetaData.getLeadData()
        leadData?.let {
            fillLeadData(leadData)
            setUpLeadFragments()
        }
    }

    private fun fillLeadData(leadMaster: AllLeadMaster) {
        val leadName =
                leadMaster.applicantFirstName + " " + leadMaster.applicantMiddleName + " " + leadMaster.applicantLastName
        binding.tvLeadNumber.text = leadMaster.leadNumber
        binding.tvApplicantName.text = leadName
        binding.tvAmount.text = (leadMaster.loanData?.loanAmountRequest ?: "").toString()
    }

    private fun setUpLeadFragments() {
        pagerAdapter = PreviewPagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragment(
                FragmentPreview.newInstance(AppEnums.ScreenLoanApp.LOAN_INFORMATION),
                AppEnums.ScreenLoanApp.LOAN_INFORMATION.screenName
        )
        pagerAdapter!!.addFragment(
                FragmentPreview.newInstance(AppEnums.ScreenLoanApp.PERSONAL),
                AppEnums.ScreenLoanApp.PERSONAL.screenName
        )
        pagerAdapter!!.addFragment(
                FragmentPreview.newInstance(AppEnums.ScreenLoanApp.EMPLOYMENT),
                AppEnums.ScreenLoanApp.EMPLOYMENT.screenName
        )
        pagerAdapter!!.addFragment(
                FragmentPreview.newInstance(AppEnums.ScreenLoanApp.BANK_DETAIL),
                AppEnums.ScreenLoanApp.BANK_DETAIL.screenName
        )
        pagerAdapter!!.addFragment(
                FragmentPreview.newInstance(AppEnums.ScreenLoanApp.LIABILITY_AND_ASSET),
                AppEnums.ScreenLoanApp.LIABILITY_AND_ASSET.screenName
        )
        pagerAdapter!!.addFragment(
                FragmentPreview.newInstance(AppEnums.ScreenLoanApp.PROPERTY),
                AppEnums.ScreenLoanApp.PROPERTY.screenName
        )
        pagerAdapter!!.addFragment(
                FragmentPreview.newInstance(AppEnums.ScreenLoanApp.REFERENCE),
                AppEnums.ScreenLoanApp.REFERENCE.screenName
        )
        pagerAdapter!!.addFragment(
                FragmentPreview.newInstance(AppEnums.ScreenLoanApp.DOCUMENT_CHECKLIST),
                AppEnums.ScreenLoanApp.DOCUMENT_CHECKLIST.screenName
        )
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = 8
        binding.pageIndicator.attachTo(binding.viewPager)

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnNext.setOnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
            checkCurrentPage()
        }

        binding.btnPrevious.setOnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
        }

        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {
                binding.tvFormPage.text = pagerAdapter?.getPageTitle(position)
                checkCurrentPage()
                binding.viewPager.adapter?.let { adapter ->
                    shouldShowSubmitButton(adapter.count - 1 == position)
                }
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    private fun checkCurrentPage() {
        when (binding.viewPager.currentItem) {
            in 1..6 -> bothButtonVisible()
            0 -> binding.btnPrevious.visibility = View.GONE
            else -> binding.btnNext.visibility = View.GONE
        }
    }

    private fun shouldShowSubmitButton(visible: Boolean) {
        binding.layoutSubmitlead.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun bothButtonVisible() {
        binding.btnPrevious.visibility = View.VISIBLE
        binding.btnNext.visibility = View.VISIBLE
    }



    inner class CallFinalSubmit : ViewGeneric<Requests.RequestFinalSubmit, Response.ResponseFinalSubmit>(context = this) {
        override val apiRequest: Requests.RequestFinalSubmit?
            get() = getCallUpdateRequest()

        override fun getApiSuccess(value: Response.ResponseFinalSubmit) {

            if (value.responseCode == Constants.SUCCESS) {
                Toast.makeText(context, "Submitted Successfully.", Toast.LENGTH_SHORT).show()

                //binding.progressBar!!.visibility = View.GONE

                val submitLoanResponse: ApplicantionSubmitModel? = value.responseObj

                if (value.responseObj != null) {
                    val intent = Intent(this@PreviewActivity, LoanSubmitStatusActivity::class.java)
                    intent.putExtra("SubmitResponse", submitLoanResponse)
                    startActivity(intent)

                    this@PreviewActivity.finish()

                } else {

                    showToast(value.responseMsg)
                }


            } else {
                showToast(value.responseMsg)
//                binding.progressBar!!.visibility = View.GONE
            }

        }

        override fun getApiFailure(msg: String) {

            if (msg.exIsNotEmptyOrNullOrBlank()) {
                super.getApiFailure(msg)
              //  binding.progressBar!!.visibility = View.GONE
            } else {
                super.getApiFailure("Time out Error")
              //  binding.progressBar!!.visibility = View.GONE
                  }

        }


        private fun getCallUpdateRequest(): Requests.RequestFinalSubmit? {
            val leadId = LeadMetaData.getLeadId()
            return Requests.RequestFinalSubmit(leadID = leadId!!)

        }

    }

}
