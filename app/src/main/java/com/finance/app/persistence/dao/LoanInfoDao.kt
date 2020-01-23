package com.finance.app.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.finance.app.persistence.model.LoanInfoMaster

@Dao
interface LoanInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoanInfo(product: LoanInfoMaster)

    @Query("SELECT * FROM LoanInfoMaster WHERE leadID=:leadID LIMIT 1")
    fun getLoanInfo(leadID: String): LiveData<LoanInfoMaster?>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateLoanInfo(loanInfo: LoanInfoMaster)

    @Query("DELETE FROM LoanProductMaster")
    fun deleteLoanProduct()

}