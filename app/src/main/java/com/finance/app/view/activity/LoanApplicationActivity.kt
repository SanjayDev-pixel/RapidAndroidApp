package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.finance.app.R
import com.finance.app.view.fragment.LoanInformationFragment
import com.finance.app.view.fragment.NavMenuFragment

class LoanApplicationActivity : AppCompatActivity() {

    private lateinit var secondaryFragment: Fragment

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoanApplicationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_application)
        
        if (savedInstanceState == null) {
            setNavFragment()
            secondaryFragment = LoanInformationFragment()
            setSecondaryFragment(secondaryFragment)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
 }
