package com.finance.app.persistence.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.finance.app.persistence.model.TempModel

/**
 * Created by munishkumarthakur on 09/12/17.
 */
@Dao
interface TempDao {

    @Query("SELECT * FROM TempModel")
    fun getAllVisualTimeTrack(): LiveData<List<TempModel>?>

    @Query("SELECT * FROM TempModel  WHERE localId = :id LIMIT 1")
    fun getVisualTimeTrackById(id: String): LiveData<TempModel?>

    @Query("SELECT * FROM TempModel WHERE val3 = :isSync")
    fun getAllVisualTimeSync(isSync: Boolean): LiveData<List<TempModel>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVisualTimeTrack(item: TempModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVisualTimeTracks(items: List<TempModel>)

    @Query("Select COUNT(localId) from TempModel")
    fun getVisualTimeTrackCount(): LiveData<Int>

    @Query("DELETE FROM TempModel")
    fun deleteAllVisualTimeTrack()
}