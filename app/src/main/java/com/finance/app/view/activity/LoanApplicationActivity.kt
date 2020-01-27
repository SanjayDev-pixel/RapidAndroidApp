package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.ActivityLoanApplicationBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.utility.LeadAndLoanDetail
import com.finance.app.view.fragment.NavMenuFragment
import com.finance.app.view.fragment.loanApplicationFragments.LoanInfoFragmentNew
import com.finance.app.viewModel.AppDataViewModel
import motobeans.architecture.appDelegates.ViewModelType
import motobeans.architecture.appDelegates.viewModelProvider
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class LoanApplicationActivity : BaseAppCompatActivity() {
    private val binding: ActivityLoanApplicationBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_loan_application)

    private val appDataViewModel: AppDataViewModel by viewModelProvider(this, ViewModelType.WITH_DAO)

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var navFragment: NavMenuFragment
    private lateinit var secondaryFragment: Fragment

    companion object {
        var leadMaster: AllLeadMaster? = null
        private const val KEY_LEAD_ID = "leadId"
        fun start(context: Context, leadId: Int?) {
            val intent = Intent(context, LoanApplicationActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(KEY_LEAD_ID, leadId!!)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        getLeadId()
        setNavFragment()
        setClickListeners()
        secondaryFragment = LoanInfoFragmentNew.newInstance()
        setSecondaryFragment(secondaryFragment)
    }

    private fun setClickListeners() {
        binding.collapseImageView.setOnClickListener {
            navFragment.toggleMenu()
        }
    }

    private fun getLeadId() {
        val bundle = intent.extras
        bundle?.let {
            val leadId = bundle.getInt(KEY_LEAD_ID)
            getLeadFromDB(leadId)
        }
    }

    private fun getLeadFromDB(leadId: Int) {
        appDataViewModel.getLeadData(leadId).observe(this, Observer { LeadMaster ->
            LeadMaster?.let {
                leadMaster = LeadMaster
                setLeadNum(LeadMaster.leadNumber)
            }
        })
    }

    fun getLead(): AllLeadMaster? {
        return leadMaster
    }

    private fun setNavFragment() {
        navFragment = NavMenuFragment()
        val ft = supportFragmentManager.beginTransaction().apply {
            add(R.id.navMenuContainer, navFragment)
            addToBackStack(null)
        }
        ft.commit()
    }

    private fun setSecondaryFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction().apply {
            add(R.id.secondaryFragmentContainer, fragment)
            addToBackStack(null)
        }
        ft.commit()
    }

    override fun onBackPressed() = showDialog()

    private fun showDialog() {
        runOnUiThread {
            if (!isFinishing) {
                AlertDialog.Builder(this)
                        .setTitle(getString(R.string.warning_msg))
                        .setMessage(getString(R.string.data_loss_msg))
                        .setCancelable(false)
                        .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                        .setPositiveButton("ok") { _, _ ->
                            this.finish()
                            super.onBackPressed()
                        }.show()
            }
        }
    }
 }
