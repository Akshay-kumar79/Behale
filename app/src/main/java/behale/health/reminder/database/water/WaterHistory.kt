package behale.health.reminder.database.water

import androidx.room.Entity
import androidx.room.PrimaryKey
import behale.health.reminder.utils.ConstantUtils
import java.util.*

@Entity(tableName = ConstantUtils.WATER_HISTORY_TABLE)
data class WaterHistory(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var time: Calendar = Calendar.getInstance(),
    var waterIntake: Int = 0

)




