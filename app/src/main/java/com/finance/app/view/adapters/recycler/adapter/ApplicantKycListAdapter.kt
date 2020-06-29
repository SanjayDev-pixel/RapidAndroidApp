package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.view.activity.KYCActivity


class ApplicantKycListAdapter(private val mContext : Context , private val applicantDetail : ArrayList<PersonalApplicantsModel> , val leadId : String) : RecyclerView.Adapter<ApplicantKycListAdapter.ViewHolder>() {
    private var mOnCardClickListener: CardClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ApplicantKycListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_applicant_kyc_list, parent, false)
        return ViewHolder(v,mContext,leadId)
    }
    interface CardClickListener {
        fun onCardFetchKycClicked(position: Int,leadId: String,leadApplicantNumber : String?)

    }
    fun setOnCardClickListener(listener: CardClickListener) {
        mOnCardClickListener = listener
    }
    override fun onBindViewHolder(holder: ApplicantKycListAdapter.ViewHolder , position: Int) {
               holder.bindItems(applicantDetail,position)
    }




    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return applicantDetail.size
    }
    //the class is hodling the list view
    inner class ViewHolder(itemView: View,val c: Context,val leadId : String) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(applicantDetail : ArrayList<PersonalApplicantsModel> , position : Int) {
            val tvfirstName = itemView.findViewById(R.id.tvfirstName) as TextView
            val tvILastName  = itemView.findViewById(R.id.tvILastName) as TextView
            val tvLeadId  = itemView.findViewById(R.id.tvLeadId) as TextView
            val btnPerformKyc =  itemView.findViewById(R.id.btnPerformKyc) as Button
            val btnFetchKyc =  itemView.findViewById(R.id.btnFetchKyc) as Button
            tvfirstName.text = applicantDetail[position].firstName
            tvILastName.text = applicantDetail[position].lastName
            tvLeadId.text = applicantDetail[position].leadApplicantNumber
            System.out.println("inComeConsider>>>>"+applicantDetail[position].incomeConsidered)
            btnPerformKyc.setOnClickListener{

                System.out.println("lead Id DataBase>>>>"+leadId)
                if(applicantDetail[position].incomeConsidered == true)
                {
                    KYCActivity.start(c ,applicantDetail[position].leadApplicantNumber,0)
                }
                else
                {
                    KYCActivity.start(c ,applicantDetail[position].leadApplicantNumber,1)
                }

            }
            btnFetchKyc.setOnClickListener{
                mOnCardClickListener!!.onCardFetchKycClicked(position,leadId,applicantDetail[position].leadApplicantNumber)
            }
        }
    }


}
