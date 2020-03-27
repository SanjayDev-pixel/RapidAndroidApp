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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLead(product: AllLeadMaster)

    @Query("SELECT * FROM AllLeadMaster")
    fun getAllLeads(): LiveData<List<AllLeadMaster>?>

    @Query("SELECT * FROM AllLeadMaster")
    fun getAllLeadsSynchron(): List<AllLeadMaster>?

    @Query("SELECT * FROM AllLeadMaster WHERE status=:leadStatus")
    fun getLeadsByStatus(leadStatus: String): LiveData<List<AllLeadMaster>?>

    @Query("SELECT * FROM AllLeadMaster WHERE isSyncWithServer = :isSyncWithServer")
    fun getAllLeadsNotSyncWithServer(isSyncWithServer: Boolean = false): LiveData<List<AllLeadMaster>?>

    @Query("SELECT * FROM AllLeadMaster WHERE leadID=:leadID  LIMIT 1")
    fun getLead(leadID: Int): LiveData<AllLeadMaster?>

    @Query("DELETE FROM AllLeadMaster")
    fun deleteAllLeadMaster()

    @Query("SELECT * FROM AllLeadMaster WHERE leadID LIKE :leadID")
    fun getAllLeadSearch(leadID: String): LiveData<List<AllLeadMaster>?>

    @Query("SELECT * FROM AllLeadMaster WHERE leadID LIKE '%' || :searchParam || '%' OR applicantFirstName LIKE '%' || :searchParam || '%'")
    fun getAllLeads(searchParam: String): LiveData<List<AllLeadMaster>?>

    @Query("SELECT * FROM AllLeadMaster WHERE status=:leadStatus AND leadID LIKE '%' || :searchParam || '%' OR applicantFirstName LIKE '%' || :searchParam || '%'")
    fun getLeadsByStatus(leadStatus: String, searchParam: String): LiveData<List<AllLeadMaster>?>
    /**
     * Updating only status
     * By lead id
     */
    @Query("UPDATE AllLeadMaster SET status = :status WHERE leadID =:id")
    fun updateLeadStatus(status: String? , id: Int?)


}