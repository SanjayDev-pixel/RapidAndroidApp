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
    fun getAllLeads(): LiveData<List<AllLeadMaster>?>

    @Query("SELECT * FROM AllLeadMaster WHERE status=:leadStatus")
    fun getLeadsByStatus(leadStatus: String): LiveData<List<AllLeadMaster>?>

    @Query("SELECT * FROM AllLeadMaster WHERE leadID=:leadID  LIMIT 1")
    fun getLead(leadID: Int): LiveData<AllLeadMaster?>

    @Query("DELETE FROM AllLeadMaster")
    fun deleteAllLeadMaster()
}