package com.example.behale.database.water

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WaterHistoryDao {

    @Insert
    fun insert(waterHistory: WaterHistory): Long

    @Update
    fun update(waterHistory: WaterHistory)

    @Query("Select * from water_history_table order by id asc")
    fun getCompleteHistory(): LiveData<List<WaterHistory>>

}