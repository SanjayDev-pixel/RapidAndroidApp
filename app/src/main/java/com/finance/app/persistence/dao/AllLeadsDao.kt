package com.finance.app.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finance.app.persistence.model.AllLeadMaster

@Dao
interface AllLeadsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLeadsList(product: ArrayList<AllLeadMaster>)

    @Query("SELECT * FROM AllLeadMaster")
    fun getAllLeads(): LiveData<List<AllLeadMaster>>

    @Query("SELECT * FROM AllLeadMaster WHERE status='Pending'")
    fun getPendingLeads(): LiveData<List<AllLeadMaster>>

    @Query("SELECT * FROM AllLeadMaster WHERE leadID=:leadID  LIMIT 1")
    fun getLead(leadID: Int): LiveData<AllLeadMaster>

    @Query("SELECT * FROM AllLeadMaster WHERE status='Submitted'")
    fun getSubmittedLeads(): LiveData<List<AllLeadMaster>>

    @Query("SELECT * FROM AllLeadMaster WHERE status='Rejected'")
    fun getRejectedLeads(): LiveData<List<AllLeadMaster>>

    @Query("DELETE FROM AllLeadMaster")
    fun deleteAllLeadMaster()
}