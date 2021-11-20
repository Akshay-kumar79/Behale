package behale.health.reminder.database.pills

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PillsHistoryDao {

    @Insert
    fun insert(pillsHistory: PillsHistory): Long

    @Update
    fun update(pillsHistory: PillsHistory)

    @Query("Select * from pill_history_table order by id asc")
    fun getCompleteHistory(): LiveData<List<PillsHistory>>

    @Query("Delete from pill_history_table where id = :key")
    fun delete(key: Int)

}