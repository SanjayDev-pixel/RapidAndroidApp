package com.finance.app.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.finance.app.persistence.model.*

@Dao
interface AssetLiabilityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAssetLiability(product: AssetLiabilityMaster)

    @Query("SELECT * FROM AssetLiabilityMaster WHERE leadID=:leadID LIMIT 1")
    fun getAssetLiability(leadID: String): LiveData<AssetLiabilityMaster>

    @Query("DELETE FROM AssetLiabilityMaster")
    fun deleteAssetLiability()

}