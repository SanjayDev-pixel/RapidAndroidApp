package com.finance.app.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.finance.app.R
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class SplashScreen : BaseAppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    companion object {
        private const val SPLASH_SCREEN_TIME_OUT = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Handler().postDelayed({

            if (sharedPreferences.isLogin()) {
                DashboardActivity.start(this)
            } else {
                val nextActivity = Intent(this,
                        LoginActivity::class.java)
                startActivity(nextActivity)
            }
            finish()
        }, SPLASH_SCREEN_TIME_OUT)
    }
}