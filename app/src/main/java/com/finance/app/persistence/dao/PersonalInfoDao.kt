package com.finance.app.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.finance.app.persistence.model.LoanInfoMaster
import com.finance.app.persistence.model.PersonalInfoMaster

@Dao
interface PersonalInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonalInfo(product: PersonalInfoMaster)

    @Query("SELECT * FROM PersonalInfoMaster WHERE leadID=:leadID LIMIT 1")
    fun getPersonalInfo(leadID: String): LiveData<PersonalInfoMaster>

    @Query("DELETE FROM PersonalInfoMaster")
    fun deleteLoanProduct()

}