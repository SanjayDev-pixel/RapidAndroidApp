package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.finance.app.R
import com.finance.app.databinding.ActivityLoanApplicationBinding
import com.finance.app.view.fragment.LoanInfoFragment
import com.finance.app.view.fragment.NavMenuFragment
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class LoanApplicationActivity : BaseAppCompatActivity() {

    private val binding: ActivityLoanApplicationBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_loan_application)

    private lateinit var navFragment: NavMenuFragment
    private lateinit var secondaryFragment: Fragment
    private lateinit var menuParam: LinearLayout.LayoutParams

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoanApplicationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        binding.collapseImageView.setOnClickListener {
            navFragment.toggleMenu()
        }
        setNavFragment()
        secondaryFragment = LoanInfoFragment()
        setSecondaryFragment(secondaryFragment)
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

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
 }
