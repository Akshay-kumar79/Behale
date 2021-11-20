package behale.health.reminder.database.diet

import androidx.room.Entity
import androidx.room.PrimaryKey
import behale.health.reminder.utils.ConstantUtils
import java.util.*

@Entity(tableName = ConstantUtils.DAILY_DIET_REMINDER_TABLE)
data class DietReminderModel(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var dietName: String = "",
    var calorie: Int = 0,
    var time: Calendar = Calendar.getInstance(),
    var isReminderOn: Boolean = false
)
