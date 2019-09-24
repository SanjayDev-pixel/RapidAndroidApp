package com.finance.app.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.finance.app.R
import com.finance.app.presenter.connector.AllSpinnerValueConnector
import com.finance.app.presenter.presenter.AllSpinnerValuePresenter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class SplashScreen : BaseAppCompatActivity(), AllSpinnerValueConnector.ViewOpt {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private val presenterOpt = AllSpinnerValuePresenter(this)

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

        presenterOpt.callNetwork(ConstantsApi.CALL_ALL_SPINNER_VALUE)

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

    override fun getAllSpinnerValueSuccess(value: Response.ResponseAllSpinnerValue) {
        saveDataToDB()
    }

    private fun saveDataToDB() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllSpinnerValueFailure(msg: String) {
        showToast(msg)
    }
}