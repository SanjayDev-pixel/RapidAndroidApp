package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.finance.app.R
import com.finance.app.databinding.ActivityLoanApplicationBinding
import com.finance.app.view.fragment.LoanInfoFragment
import com.finance.app.view.fragment.NavMenuFragment
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class LoanApplicationActivity : BaseAppCompatActivity() {
    private val binding: ActivityLoanApplicationBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_loan_application)

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var navFragment: NavMenuFragment
    private lateinit var secondaryFragment: Fragment

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoanApplicationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        binding.collapseImageView.setOnClickListener {
            navFragment.toggleMenu()
        }
        setLeadNumber()
        setNavFragment()
        secondaryFragment = LoanInfoFragment()
        setSecondaryFragment(secondaryFragment)
    }

    private fun setLeadNumber() {
        val leadNum = sharedPreferences.getLeadNum()
        setLeadNum(leadNum)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

 }
