package com.finance.app.persistence.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finance.app.persistence.model.AllMasterDropDownValue

@Dao
interface AllMasterDropDownDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMasterDropDownValue(product: AllMasterDropDownValue)

    @Query("SELECT * FROM AllMasterDropDownValue LIMIT 1")
    fun getMasterDropdownValue(): LiveData<AllMasterDropDownValue>

    @Query("DELETE FROM AllMasterDropDownValue")
    fun deleteAllMasterDropdownValue()

}