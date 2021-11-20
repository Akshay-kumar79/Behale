package behale.health.reminder.database.diet

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface DietHistoryDao {

    @Insert
    fun insert(dietHistory: DietHistory): Long

    @Update
    fun update(dietHistory: DietHistory)

    @Query("Select * from diet_history_table order by id asc")
    fun getCompleteHistory(): LiveData<List<DietHistory>>

    @Query("Delete from diet_history_table where id = :key")
    fun delete(key: Int)

}