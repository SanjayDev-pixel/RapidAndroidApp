package com.finance.app.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.finance.app.persistence.model.BankDetailMaster
import com.finance.app.persistence.model.LoanInfoMaster

@Dao
interface BankDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBankDetail(product: BankDetailMaster)

    @Query("SELECT * FROM BankDetailMaster WHERE leadID=:leadID LIMIT 1")
    fun getBankDetail(leadID: String): LiveData<BankDetailMaster>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateBankDetail(bankDetail: BankDetailMaster)

    @Query("DELETE FROM BankDetailMaster")
    fun deleteBankDetail()

}