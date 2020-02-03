package com.finance.app.view.fragment.loanApplicationFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.finance.app.R
import com.finance.app.databinding.AssetLiabilityFragmentFormBinding
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */


class AssetLiabilityFragmentForm : BaseFragment() {

    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: AssetLiabilityFragmentFormBinding

    private var leadApplicantNumber: String? = null


    companion object {
        fun newInstance(leadApplicantNumber: String): AssetLiabilityFragmentForm {
            val fragment = AssetLiabilityFragmentForm()
            fragment.leadApplicantNumber = leadApplicantNumber
            return fragment
        }

    }

    override fun init() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ArchitectureApp.instance.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.asset_liability_fragment_form)

        initViews()
        setOnClickListener()

        return binding.root
    }

    private fun initViews() {

    }

    private fun setOnClickListener() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Now Fetch data from server...
        fetchLeadAssetsAndLiabilityDetails()

    }

    private fun fetchLeadAssetsAndLiabilityDetails() {
        leadApplicantNumber?.let { activity?.let { it1 -> binding.customAssetView.initApplicantDetails(it1, it) } }

//        LeadMetaData.getLeadObservable().observe(this@AssetLiabilityFragmentForm, Observer { leadDetails ->
//            leadDetails?.let {
//                val filterList = it.assetLiabilityData.applicantDetails?.filter { assetsLiability -> leadApplicantNumber == assetsLiability.leadApplicantNumber }
//                binding.customAssetView.attachView(activity!!, filterList)
//                if (!filterList.isNullOrEmpty()) {
//                    LeadMetaData.getLeadId()?.let { it1 -> binding.customAssetView.attachView(activity!!, index, filterList?.get(0), it1) }
//                }
        /*else {
                LeadMetaData.getLeadId()?.let { it1 -> binding.customAssetView.attachView(activity!!, index, AssetLiabilityModel(), it1) }
            }*/
//            }
//        })
    }
}
