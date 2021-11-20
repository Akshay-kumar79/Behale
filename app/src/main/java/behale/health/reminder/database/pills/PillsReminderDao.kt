package behale.health.reminder.database.pills

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PillsReminderDao {

    @Insert
    fun insert(pillsReminder: PillsReminderModel): Long

    @Update
    fun update(pillsReminder: PillsReminderModel)

    @Query("DELETE from daily_pills_reminder_table where id = :key")
    fun delete(key: Int)

    @Query("SELECT * from daily_pills_reminder_table where id = :key")
    fun getReminder(key: Int) : PillsReminderModel

    @Query("DELETE from daily_pills_reminder_table")
    fun clear()

    @Query("SELECT * from daily_pills_reminder_table order by id desc")
    fun getAllReminders(): LiveData<List<PillsReminderModel>>

}