package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.finance.app.R
import com.finance.app.databinding.ActivityPreviewBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.PreviewPagerAdapter
import com.finance.app.view.fragment.loanApplicationFragments.FragmentPreview
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class PreviewActivity : BaseAppCompatActivity() {

    private val binding: ActivityPreviewBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_preview)

    @Inject
    lateinit var dataBase: DataBaseUtil
    private var pagerAdapter: PreviewPagerAdapter? = null

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

    }

    private fun getLead() {
        val leadData = LeadMetaData.getLeadData()
        leadData?.let {
            fillLeadData(leadData)
            setUpLeadFragments()
        }
    }

    private fun fillLeadData(leadMaster: AllLeadMaster) {
        val leadName = leadMaster.applicantFirstName + " " + leadMaster.applicantMiddleName + " " + leadMaster.applicantLastName
        binding.tvLeadNumber.text = leadMaster.leadNumber
        binding.tvApplicantName.text = leadName
        binding.tvAmount.text = (leadMaster.loanData?.loanAmountRequest ?: "").toString()
    }

    private fun setUpLeadFragments() {
        pagerAdapter = PreviewPagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragment(FragmentPreview.newInstance(AppEnums.ScreenLoanApp.LOAN_INFORMATION), AppEnums.ScreenLoanApp.LOAN_INFORMATION.screenName)
        pagerAdapter!!.addFragment(FragmentPreview.newInstance(AppEnums.ScreenLoanApp.PERSONAL), AppEnums.ScreenLoanApp.PERSONAL.screenName)
        pagerAdapter!!.addFragment(FragmentPreview.newInstance(AppEnums.ScreenLoanApp.EMPLOYMENT), AppEnums.ScreenLoanApp.EMPLOYMENT.screenName)
        pagerAdapter!!.addFragment(FragmentPreview.newInstance(AppEnums.ScreenLoanApp.BANK_DETAIL), AppEnums.ScreenLoanApp.BANK_DETAIL.screenName)
        pagerAdapter!!.addFragment(FragmentPreview.newInstance(AppEnums.ScreenLoanApp.LIABILITY_AND_ASSET), AppEnums.ScreenLoanApp.LIABILITY_AND_ASSET.screenName)
        pagerAdapter!!.addFragment(FragmentPreview.newInstance(AppEnums.ScreenLoanApp.PROPERTY), AppEnums.ScreenLoanApp.PROPERTY.screenName)
        pagerAdapter!!.addFragment(FragmentPreview.newInstance(AppEnums.ScreenLoanApp.REFERENCE), AppEnums.ScreenLoanApp.REFERENCE.screenName)
        pagerAdapter!!.addFragment(FragmentPreview.newInstance(AppEnums.ScreenLoanApp.DOCUMENT_CHECKLIST), AppEnums.ScreenLoanApp.DOCUMENT_CHECKLIST.screenName)
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
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                binding.tvFormPage.text = pagerAdapter?.getPageTitle(position)
                checkCurrentPage()
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

    private fun bothButtonVisible() {
        binding.btnPrevious.visibility = View.VISIBLE
        binding.btnNext.visibility = View.VISIBLE
    }

}
