package com.example.behale.database.water

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.behale.utils.ConstantUtils
import java.util.*

@Entity(tableName = ConstantUtils.DAILY_WATER_REMINDER_TABLE)
data class WaterReminderModel(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var time: Calendar = Calendar.getInstance(),
    var switchOn: Boolean = true
)
