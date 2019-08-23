package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.LinearLayout
import com.finance.app.R
import com.finance.app.databinding.ActivityLoanApplicationBinding
import com.finance.app.view.fragment.LoanInformationFragment
import com.finance.app.view.fragment.NavMenuFragment
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class LoanApplicationActivity : BaseAppCompatActivity() {

    private val binding: ActivityLoanApplicationBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_loan_application)

    private var isExpand = false
    private lateinit var secondaryFragment: Fragment
    private lateinit var menuParam: LinearLayout.LayoutParams

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoanApplicationActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setNavFragment()
            secondaryFragment = LoanInformationFragment()
            updateSecondaryFragment(secondaryFragment)
        }
    }

    override fun init() {
        binding.collapseImageView.setOnClickListener {
            handleCollapseScreen()
        }
    }

    private fun setNavFragment() {
        val fragment = NavMenuFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.navMenuContainer, fragment)
        ft.commit()
    }

    private fun updateSecondaryFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.secondaryFragmentContainer, fragment)
        ft.commit()
    }

    private fun handleCollapseScreen() {
        val fragment = supportFragmentManager.findFragmentById(R.id.navMenuContainer) as NavMenuFragment

        if (isExpand) {
            menuParam = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
            binding.mainLoanLayout.navMenuContainer.layoutParams = menuParam
            val params1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
            binding.mainLoanLayout.secondaryFragmentContainer.layoutParams = params1
            fragment.notifyMenu(isExpand)
            isExpand = false
        } else {
            menuParam = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
            menuParam.weight = 1f
            binding.mainLoanLayout.navMenuContainer.layoutParams = menuParam
            val params1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
            params1.weight = 2f
            binding.mainLoanLayout.secondaryFragmentContainer.layoutParams = params1
            fragment.notifyMenu(isExpand)
            isExpand = true
        }
    }
}
