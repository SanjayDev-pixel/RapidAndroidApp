package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.finance.app.R
import com.finance.app.databinding.ActivityDashboardBinding
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import motobeans.architecture.util.roundTo2DecimalPlaces
import javax.inject.Inject

class DashboardActivity : BaseAppCompatActivity() {
    private val binding: ActivityDashboardBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_dashboard)
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private val moduleValues = listOf("Select Module","Disbarsal", "Loan Sanction", "Login")
    private val slotValues = listOf("Select Slot","MTD", "YTD")

    companion object {
        private var filesPercent = 0.0
        private var volumePercent = 0.0
        private var targetFiles = 5
        private var achievedFiles = 3
        private var targetVolume = 700000
        private var achievedVolume = 500000
        private var percentShortfallFile = 0.0
        private var percentShortfallVolume = 0.0

        fun start(context: Context) {
            val intent = Intent(context, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        hideSecondaryToolbar()
        provideDropdownValue()
        setListenersOnDropdown()
    }

    private fun provideDropdownValue() {
        val moduleAdapter = ArrayAdapter(this,
                R.layout.spinner_item, moduleValues)
        moduleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerModule.adapter = moduleAdapter

        val slotAdapter = ArrayAdapter(this,
                R.layout.spinner_item, slotValues)
        slotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSlot.adapter = slotAdapter
    }

    private fun setListenersOnDropdown() {
        binding.spinnerModule.onItemSelectedListener = object : OnItemSelectedListener {
            override
            fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int,
                               id: Long) {
                updateData(moduleValues[position])
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }

        binding.spinnerSlot.onItemSelectedListener = object : OnItemSelectedListener {
            override
            fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int,
                               id: Long) {
                updateData(slotValues[position])
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // do Nothing
            }
        }
    }

    private fun updateData(value: Any) {
        when (value) {
            moduleValues[1] -> {
                achievedFiles = 4
                achievedVolume = 650000
            }
            moduleValues[2] -> {
                achievedFiles = 2
                achievedVolume = 350000
            }
            moduleValues[3] -> {
                achievedFiles = 5
                achievedVolume = 450000
            }
            slotValues[1] -> {
                achievedFiles = 4
                achievedVolume = 650000
            }
            slotValues[2] -> {
                achievedFiles = 7
                achievedVolume = 540000
            }
        }

        updateFileChart()
        updateVolumeChart()
        updateFormData()
    }

    private fun updateFileChart() {
        try {
            filesPercent = ((achievedFiles.toDouble() / targetFiles.toDouble()) * 100)
            filesPercent = filesPercent.roundTo2DecimalPlaces()
            percentShortfallFile = (100 - filesPercent).roundTo2DecimalPlaces()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.progressFiles.percentage.text = filesPercent.toString().plus("%")
        binding.progressFiles.statsProgressbar.progress = filesPercent.toInt()
    }

    private fun updateVolumeChart() {
        try {
            volumePercent = ((achievedVolume.toDouble() / targetVolume.toDouble()) * 100)
            volumePercent = volumePercent.roundTo2DecimalPlaces()
            percentShortfallVolume = (100 - volumePercent).roundTo2DecimalPlaces()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.progressVolume.percentage.text = volumePercent.toString().plus("%")
        binding.progressVolume.statsProgressbar.progress = volumePercent.toInt()
    }

    private fun updateFormData() {
        binding.tvAchievedFiles.text = achievedFiles.toString()
        binding.tvTargetFiles.text = targetFiles.toString()
        binding.tvTargetVolume.text = targetVolume.toString()
        binding.tvAchievedVolume.text = achievedVolume.toString()
        binding.tvPercentAchievedFiles.text = filesPercent.toString().plus("%")
        binding.tvPercentAchievedVolume.text = volumePercent.toString().plus("%")
        binding.tvPercentShortfallFiles.text = percentShortfallFile.toString().plus("%")
        binding.tvPercentShortfallVolume.text = percentShortfallVolume.toString().plus("%")
    }

}
