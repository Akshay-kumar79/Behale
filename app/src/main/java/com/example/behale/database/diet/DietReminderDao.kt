package com.example.behale.database.diet

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DietReminderDao {

    @Insert
    fun insert(dietReminder: DietReminderModel): Long

    @Update
    fun update(dietReminder: DietReminderModel)

    @Query("DELETE from daily_diet_reminder_table where id = :key")
    fun delete(key: Int)

    @Query("SELECT * from daily_diet_reminder_table where id = :key")
    fun getReminder(key: Int) : DietReminderModel

    @Query("DELETE from daily_diet_reminder_table")
    fun clear()

    @Query("SELECT * from daily_diet_reminder_table order by id desc")
    fun getAllReminders(): LiveData<List<DietReminderModel>>

}