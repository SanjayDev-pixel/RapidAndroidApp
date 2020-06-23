package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.CoApplicantsList
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.activity.KYCActivity
import com.finance.app.viewModel.LeadDataViewModel
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import motobeans.architecture.appDelegates.ViewModelType
import motobeans.architecture.retrofit.response.Response

class ApplicantKycListAdapter(val mContext : Context , val applicantDetail : ArrayList<CoApplicantsList> , val leadId : String) : RecyclerView.Adapter<ApplicantKycListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ApplicantKycListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_applicant_kyc_list, parent, false)
        return ViewHolder(v,mContext,leadId)
    }

    override fun onBindViewHolder(holder: ApplicantKycListAdapter.ViewHolder , position: Int) {
               holder.bindItems(applicantDetail,position)
    }
    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return applicantDetail.size
    }
    //the class is hodling the list view
    class ViewHolder(itemView: View,val c: Context,val leadId : String) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(applicantDetail : ArrayList<CoApplicantsList>,position : Int) {
            val tvfirstName = itemView.findViewById(R.id.tvfirstName) as TextView
            val tvILastName  = itemView.findViewById(R.id.tvILastName) as TextView
            val tvLeadId  = itemView.findViewById(R.id.tvLeadId) as TextView
            val btnPerformKyc =  itemView.findViewById(R.id.btnPerformKyc) as Button
            tvfirstName.text = applicantDetail[position].firstName
            //tvILastName.text = applicantDetail[position].lastName
            tvLeadId.text = applicantDetail[position].leadApplicantNumber


            btnPerformKyc.setOnClickListener{


                System.out.println("lead Id DataBase>>>>"+leadId)
                KYCActivity.start(c ,applicantDetail[position].leadApplicantNumber,0)
            }
        }
    }
}