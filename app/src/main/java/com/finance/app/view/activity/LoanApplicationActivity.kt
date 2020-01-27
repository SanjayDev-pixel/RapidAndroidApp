package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.finance.app.R
import com.finance.app.databinding.ActivityLoanApplicationBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.CoApplicantsList
import com.finance.app.view.fragment.NavMenuFragment
import com.finance.app.view.fragment.loanApplicationFragments.LoanInfoFragmentNew
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class LoanApplicationActivity : BaseAppCompatActivity() {
    private val binding: ActivityLoanApplicationBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_loan_application)

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var navFragment: NavMenuFragment
    private lateinit var secondaryFragment: Fragment


    companion object {
        var leadMaster: AllLeadMaster? = null
        fun start(context: Context) {
            val intent = Intent(context, LoanApplicationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        hideToolbar()
        hideSecondaryToolbar()
        ArchitectureApp.instance.component.inject(this)
        binding.collapseImageView.setOnClickListener {
            navFragment.toggleMenu()
        }
        setLeadNumber()
        setNavFragment()
        secondaryFragment = LoanInfoFragmentNew.newInstance()
        setSecondaryFragment(secondaryFragment)

        val layout_back: LinearLayout=findViewById(R.id.lytBack)
        layout_back.setOnClickListener(){
            showDialog()
        }
        val lead = sharedPreferences.getLeadDetail()
        binding.tvMobile.text = lead?.applicantContactNumber
        binding.header.tvLeadNumber.text = lead?.leadNumber
        val leadName = lead?.applicantFirstName + " " + lead?.applicantMiddleName+ " " + lead?.applicantLastName
        binding.applicantName.text=leadName
        binding.tvDesignation.text=getString(R.string.applicant)

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    private fun setLeadNumber() {

        val lead = sharedPreferences.getLeadDetail()
        lead?.let{
            setLeadNum(lead.leadNumber!!)
        }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(coApplicantsList: CoApplicantsList){

        binding.applicantName.text = coApplicantsList.firstName.plus(" " + coApplicantsList.middleName)
        //binding.tvLeadid.text="Lead Number:".plus(coApplicantsList!!.leadApplicantNumber)
        binding.header.tvLeadNumber.text = coApplicantsList.leadApplicantNumber
        binding.tvDesignation.text=coApplicantsList.applicantType
        binding.tvMobile.text=coApplicantsList.mobile


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }



 }


