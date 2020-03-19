package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.ActivityDashboardNewBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.view.adapters.recycler.adapter.DashboardChartAdapter
import com.finance.app.workers.UtilWorkManager
import com.google.gson.Gson
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject


class DashboardActivity : BaseAppCompatActivity() {

    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    private var adapterChart: DashboardChartAdapter? = null
    private val binding: ActivityDashboardNewBinding by ActivityBindingProviderDelegate(this, R.layout.activity_dashboard_new)


    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)

        UtilWorkManager.globalWorkManagerPeriodically()

        hideSecondaryToolbar()
        setVersionName()

        initializeChartData()
    }

    private fun setVersionName() {
        try {
            val pInfo = this.packageManager.getPackageInfo(this.packageName, 0)
            val version = pInfo.versionName
            binding.tvVersionName.text = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    fun initializeChartData() {
        val dashboardResponse = Gson().fromJson(Constants.TEMP_DATA.apiChartResult, Response.DashboardResponse::class.java)

        initChartAdapter(dashboardResponse = dashboardResponse)
    }

    private fun initChartAdapter(dashboardResponse: Response.DashboardResponse) {
        adapterChart = DashboardChartAdapter(mActivity = this, dashboardChartData = dashboardResponse)
        binding.rvDashboardCharts.adapter = adapterChart
        binding.rvDashboardCharts.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

    }

    public override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe
    fun getEventBackgroundSync(syncEnum: AppEvents.BackGroundSyncEvent) {
        AppEvents().initiateBackgroundSync(syncEnum = syncEnum)
    }
}
