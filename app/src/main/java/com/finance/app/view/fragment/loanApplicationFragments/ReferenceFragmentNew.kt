package com.finance.app.view.fragment.loanApplicationFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.FragmentReferenceBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.ReferenceModel
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.ReferenceAdapter
import com.finance.app.view.dialogs.ReferenceDetailDialogFragment
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import java.util.*
import javax.inject.Inject

class ReferenceFragmentNew : BaseFragment(), ReferenceDetailDialogFragment.OnReferenceDetailDialogCallback {

    @Inject
    lateinit var dataBase: DataBaseUtil

    private lateinit var mContext: Context
    private lateinit var binding: FragmentReferenceBinding

    private var referenceAdapter: ReferenceAdapter? = null
    private var allMasterDropDown: AllMasterDropDown? = null

    private var leadDetails: AllLeadMaster? = null

    companion object {
        fun newInstance(): ReferenceFragmentNew {
            return ReferenceFragmentNew()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        ArchitectureApp.instance.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_reference)
        binding.lifecycleOwner = this

        initViews()
        setOnClickListeners()

        return view
    }

    override fun init() {
    }

    private fun initViews() {
    }

    private fun setOnClickListeners() {
        binding.vwAdd.setOnClickListener { showReferenceDetailFormDialog(ReferenceDetailDialogFragment.Action.NEW) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Now fetch data from where-ever you want....
        fetchLeadDetails()
        fetchSpinnersDataFromDB()
    }

    private fun fetchLeadDetails() {
        LeadMetaData.getLeadObservable().observe(this, Observer { leadDetails ->
            leadDetails?.let {
                this@ReferenceFragmentNew.leadDetails = it
                //Set Reference Details List.
                it.referenceData.referenceDetails?.let { it1 -> setReferenceDetailAdapter(it1) }
            }
        })
    }

    private fun setReferenceDetailAdapter(referenceDetails: ArrayList<ReferenceModel>) {
        binding.rcReference.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        referenceAdapter = ReferenceAdapter(mContext, referenceDetails)
        binding.rcReference.adapter = referenceAdapter
//        referenceAdapter?.setOnBankDetailClickListener(this)
        binding.rcReference.visibility = View.VISIBLE
    }

    private fun fetchSpinnersDataFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues?.let {
                allMasterDropDown = it
            }
        })
    }

    private fun showReferenceDetailFormDialog(action: ReferenceDetailDialogFragment.Action, referenceModel: ReferenceModel? = null) {
        allMasterDropDown?.let { ReferenceDetailDialogFragment.newInstance(action, this@ReferenceFragmentNew, it, referenceModel).show(fragmentManager, "Reference Detail") }
    }

    override fun onSaveBankDetail(referenceModel: ReferenceModel) {

    }

    override fun onEditBankDetail(referenceModel: ReferenceModel) {
    }
}
