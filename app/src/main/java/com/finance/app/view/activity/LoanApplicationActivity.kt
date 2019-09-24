package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.finance.app.R
import com.finance.app.databinding.ActivityLoanApplicationBinding
import com.finance.app.view.fragment.*
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class LoanApplicationActivity : BaseAppCompatActivity() {

    private val binding: ActivityLoanApplicationBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_loan_application)

    private lateinit var navFragment: NavMenuFragment
    private lateinit var secondaryFragment: Fragment
    private lateinit var menuParam: LinearLayout.LayoutParams

    companion object {
        private var isExpand = false
        fun start(context: Context) {
            val intent = Intent(context, LoanApplicationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        setNavFragment()
        secondaryFragment = LoanInformationFragment()
        setSecondaryFragment(secondaryFragment)
        binding.collapseImageView.setOnClickListener {
            handleCollapseScreen(isExpand)
        }
    }

    private fun setNavFragment() {
        val fragment = NavMenuFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.navMenuContainer, fragment)
        ft.commit()
    }

    private fun setSecondaryFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.secondaryFragmentContainer, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    fun handleCollapseScreen(collapse: Boolean) {
        navFragment = supportFragmentManager.findFragmentById(R.id.navMenuContainer) as NavMenuFragment
        if (collapse) {
            expandScreen(collapse)
        } else {
            collapseScreen(collapse)
        }
    }

    private fun expandScreen(collapse: Boolean) {
        menuParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        binding.mainLoanLayout.navMenuContainer.layoutParams = menuParam
        val mainParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        binding.mainLoanLayout.secondaryFragmentContainer.layoutParams = mainParam
        navFragment.notifyMenu(collapse)
        isExpand = false
    }

    private fun collapseScreen(collapse: Boolean) {
        menuParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        menuParam.weight = 1f
        binding.mainLoanLayout.navMenuContainer.layoutParams = menuParam
        val mainParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        mainParam.weight = 2f
        binding.mainLoanLayout.secondaryFragmentContainer.layoutParams = mainParam
        navFragment.notifyMenu(collapse)
        isExpand = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
 }
