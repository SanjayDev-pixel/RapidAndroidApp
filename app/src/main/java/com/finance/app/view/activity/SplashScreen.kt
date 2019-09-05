package com.finance.app.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.finance.app.R
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class SplashScreen : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    companion object {
        private const val SPLASH_SCREEN_TIME_OUT = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ArchitectureApp.instance.component.inject(this)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({

            if (sharedPreferencesUtil.isLogin()) {
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