package com.finance.app.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.LayoutChannelPartnerBinding
import com.finance.app.persistence.model.*
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.customViews.interfaces.IspinnerMainView
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.Constants.APP.DIRECT
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.AppUtilExtensions
import javax.inject.Inject

class CustomChannelPartnerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : LinearLayout(context, attrs) {

    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    private lateinit var binding: LayoutChannelPartnerBinding

    private val presenter = Presenter()

    private var mBranchId: String? = null
    private var mChannelTypeId: String? = null
    private var empId: String? = null
    private lateinit var activity: FragmentActivity
    private var isMandatory = false
    private var sourcingPartner: CustomSpinnerView<DropdownMaster>? = null
    private var partnerName: CustomSpinnerView<ChannelPartnerName>? = null

    fun attachActivity(activity: FragmentActivity, isMandatory: Boolean = false) {
        this.activity = activity
        this.isMandatory = isMandatory
        binding = AppUtilExtensions.initCustomViewBinding(context = context,
                layoutId = R.layout.layout_channel_partner, container = this)
        proceedFurther()
    }

    private fun proceedFurther() {
        ArchitectureApp.instance.component.inject(this)
        getDropDownsFromDB()
    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue()
                .observe(activity, Observer { allDroDownValue ->

                    allDroDownValue?.let {
                        initializeSourcingPartner(allDroDownValue)
            }
        })
    }

    private var sPartner: DropdownMaster? = null

    private fun initializeSourcingPartner(masterDropDown: AllMasterDropDown) {
        partnerName = CustomSpinnerView(mContext = context, isMandatory = true,
                dropDowns = ArrayList(), label = "Channel Partner Name *")
        binding.layoutPartnerName.addView(partnerName)

        sourcingPartner = CustomSpinnerView(mContext = context,
                dropDowns = masterDropDown.SourcingChannelPartner!!, label = "Sourcing Channel Partner *",
                iSpinnerMainView = object : IspinnerMainView<DropdownMaster> {

                    override fun getSelectedValue(value: DropdownMaster) {
                        sPartner = value
                        getPartnerNameFromApi(value.getCompareValue())
            }
        })
        binding.layoutSourcingPartner.addView(sourcingPartner)
    }

    private fun getPartnerNameFromApi(channelId: String) {
        mChannelTypeId = channelId
        mBranchId = LeadMetaData.getLeadData()?.branchID
        empId = sharedPreferences.getEmpId()

        if ((mChannelTypeId?.toInt() ?: 0) != DIRECT) {
            presenter.callNetwork(ConstantsApi.CALL_SOURCE_CHANNEL_PARTNER_NAME, CallSourcingPartnerName())
            binding.layoutPartnerName.visibility = View.VISIBLE
        } else {
            presenter.callNetwork(ConstantsApi.CALL_SOURCE_CHANNEL_PARTNER_NAME, CallSourcingPartnerName())
            binding.layoutPartnerName.visibility = View.GONE
        }
    }

    fun setSourcingPartner(loanInfoModel: LoanInfoModel) {
       sourcingPartner?.let{
           sourcingPartner!!.setSelection(loanInfoModel.sourcingChannelPartnerTypeDetailID.toString())
       }
    }

    fun getSourcingPartner(): DropdownMaster? {
        return sourcingPartner?.getSelectedValue()
    }

    fun getPartnerName(): ChannelPartnerName? {
        return partnerName?.getSelectedValue()
    }

    inner class CallSourcingPartnerName : ViewGeneric<ArrayList<String?>,
            Response.ResponseSourceChannelPartnerName>(context = context) {

        override val apiRequest: ArrayList<String?>
            get() = arrayListOf(mBranchId, mChannelTypeId, empId)

        override fun getApiSuccess(value: Response.ResponseSourceChannelPartnerName) {
            if (value.responseCode == Constants.SUCCESS) {
                binding.layoutPartnerName.removeAllViews()
                setChannelPartnerNameDropDown(value.responseObj)
            }
        }

        private val leadMaster = AllLeadMaster()
        private fun setChannelPartnerNameDropDown(channelPartners: ArrayList<ChannelPartnerName>?) {
            partnerName = CustomSpinnerView(mContext = context, dropDowns = channelPartners, label = "Channel Partner Name")
            binding.layoutPartnerName.addView(partnerName)

            leadMaster.loanData?.channelPartnerDsaID?.let {
                partnerName?.setSelection(leadMaster.loanData?.channelPartnerDsaID.toString())
            }
        }
    }
}