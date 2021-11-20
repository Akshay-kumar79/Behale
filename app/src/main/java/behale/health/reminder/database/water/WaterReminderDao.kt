package behale.health.reminder.database.water

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WaterReminderDao {

    @Insert
    fun insert(waterReminder: WaterReminderModel): Long

    @Update
    fun update(waterReminder: WaterReminderModel)

    @Query("DELETE from daily_water_reminder_table where id = :key")
    fun delete(key: Int)

    @Query("SELECT * from daily_water_reminder_table where id = :key")
    fun getReminder(key: Int) : WaterReminderModel

    @Query("DELETE from daily_water_reminder_table")
    fun clear()

    @Query("SELECT * from daily_water_reminder_table order by id desc")
    fun getAllReminders(): LiveData<List<WaterReminderModel>>


}