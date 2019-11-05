package com.finance.app.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.finance.app.persistence.model.*

@Dao
interface EmploymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmployment(product: EmploymentMaster)

    @Query("SELECT * FROM EmploymentMaster WHERE leadID=:leadID  LIMIT 1")
    fun getEmployment(leadID: String): LiveData<EmploymentMaster>

    @Query("DELETE FROM EmploymentMaster")
    fun deleteEmploymentMaster()

}