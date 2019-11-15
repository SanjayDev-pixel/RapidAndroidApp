package com.finance.app.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.BankDetailMaster
import com.finance.app.persistence.model.LoanInfoMaster
import com.finance.app.persistence.model.ReferenceMaster

@Dao
interface ReferenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReference(product: ReferenceMaster)

    @Query("SELECT * FROM ReferenceMaster WHERE leadID=:leadID")
    fun getReference(leadID: String): LiveData<ReferenceMaster>

    @Query("DELETE FROM ReferenceMaster")
    fun deleteReferenceMaster()

}