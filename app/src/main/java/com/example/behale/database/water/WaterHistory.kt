package com.example.behale.database.water

import android.provider.CalendarContract
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.behale.database.ReminderDataBase
import com.example.behale.utils.ConstantUtils
import java.util.*

@Entity(tableName = ConstantUtils.WATER_HISTORY_TABLE)
data class WaterHistory(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var time: Calendar = Calendar.getInstance(),
    var waterIntake: Int = 0

)




