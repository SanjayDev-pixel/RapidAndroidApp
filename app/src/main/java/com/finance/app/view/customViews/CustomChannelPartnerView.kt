package com.finance.app.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.persistence.model.*
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.view.customViews.Interfaces.IspinnerMainView
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.Constants.APP.DIRECT
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class CustomChannelPartnerView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null) :
        LinearLayout(mContext, attrs) {

    private val TAG = "CustomSignatureView"

    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    private val presenter = Presenter()

    private lateinit var mBranchId: String
    private var mChannelTypeId: String = ""
    private var empId: String? = null

    private lateinit var sourcingPartner: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var partnerName: CustomSpinnerViewTest<ChannelPartnerName>

    private lateinit var layoutSourcingPartner: LinearLayout
    private lateinit var layoutPartnerName: LinearLayout

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.layout_channel_partner, this, true)
        initializeViews(rootView)
    }

    private fun initializeViews(rootView: View) {
        layoutPartnerName = rootView.findViewById(R.id.layoutPartnerName)
        layoutSourcingPartner = rootView.findViewById(R.id.layoutSourcingPartner)
    }

    private lateinit var activity: FragmentActivity
    private var isMandatory: Boolean = false

    fun attachActivity(activity: FragmentActivity) {
        this.activity = activity
        proceedFurther()
    }

    private fun proceedFurther() {
        ArchitectureApp.instance.component.inject(this)
        getDropDownsFromDB()
    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(activity, Observer {
            it?.let {
                setChannelPartner(it)
            }
        })
    }

    private fun setChannelPartner(masterDropDown: AllMasterDropDown) {
        partnerName = CustomSpinnerViewTest(context = mContext, dropDowns = ArrayList(), label = "Channel Partner Name")
        layoutPartnerName.addView(partnerName)

        sourcingPartner = CustomSpinnerViewTest(context = context, dropDowns = masterDropDown.SourcingChannelPartner!!, label = "Sourcing Channel Partner *", iSpinnerMainView = object : IspinnerMainView<DropdownMaster> {
            override fun getSelectedValue(value: DropdownMaster) {
                getPartnerNameFromApi(value.getCompareValue())
            }
        })
        layoutSourcingPartner.addView(sourcingPartner)
    }

    private fun getPartnerNameFromApi(channelId: String) {
        mChannelTypeId = channelId
        mBranchId = sharedPreferences.getLeadDetail()?.branchID!!
        empId = sharedPreferences.getEmpId()
        if (mChannelTypeId.toInt() != DIRECT) {
            presenter.callNetwork(ConstantsApi.CALL_SOURCE_CHANNEL_PARTNER_NAME, CallSourcingPartnerName())
            layoutPartnerName.visibility = View.VISIBLE
        } else {
            presenter.callNetwork(ConstantsApi.CALL_SOURCE_CHANNEL_PARTNER_NAME, CallSourcingPartnerName())
            layoutPartnerName.visibility = View.GONE
        }
    }

    fun isMandatory(isMandatory: Boolean) {
        this.isMandatory = isMandatory
    }

    fun selectSourcingChannelPartner(loanInfoModel: LoanInfoModel) {
        sourcingPartner.setSelection(loanInfoModel.sourcingChannelPartnerTypeDetailID.toString())
    }

    fun getSourcingChannelPartner(): DropdownMaster? {
        return sourcingPartner.getSelectedValue()
    }

    fun getPartnerName(): ChannelPartnerName? {
        return partnerName.getSelectedValue()
    }


    fun clearData() {
        sourcingPartner.clearSpinner()
        partnerName.clearSpinner()
    }

    fun disableSelf() {
        sourcingPartner.disableSelf()
        partnerName.disableSelf()
    }

    fun validateAndHandleError(): Boolean {
        var errorCount = 0
        val sPartner = sourcingPartner.getSelectedValue()

        if (sPartner == null) {
            errorCount++
            sourcingPartner.showError(true)
        }
        return errorCount <= 0
    }

    inner class CallSourcingPartnerName : ViewGeneric<ArrayList<String>, Response.ResponseSourceChannelPartnerName>(context = mContext!!) {
        override val apiRequest: ArrayList<String>
            get() = arrayListOf(mBranchId, mChannelTypeId, empId!!)

        override fun getApiSuccess(value: Response.ResponseSourceChannelPartnerName) {
            if (value.responseCode == Constants.SUCCESS) {
                layoutPartnerName.removeAllViews()
                setChannelPartnerNameDropDown(value.responseObj)
            }
        }

        val leadMaster = AllLeadMaster()
        private fun setChannelPartnerNameDropDown(channelPartners: ArrayList<ChannelPartnerName>?) {
            partnerName = CustomSpinnerViewTest(context = mContext, dropDowns = channelPartners, label = "Channel Partner Name")
            layoutPartnerName.addView(partnerName)

            leadMaster?.loanData?.channelPartnerDsaID?.let {
                partnerName.setSelection(leadMaster?.loanData?.channelPartnerDsaID.toString())
            }
        }
    }
}