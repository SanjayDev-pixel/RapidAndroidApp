package com.finance.app.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.finance.app.persistence.model.*

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProperty(product: PropertyMaster)

    @Query("SELECT * FROM PropertyMaster WHERE leadID=:leadID  LIMIT 1")
    fun getProperty(leadID: String): LiveData<PropertyMaster>

    @Query("DELETE FROM PropertyMaster")
    fun deletePropertyMaster()

}