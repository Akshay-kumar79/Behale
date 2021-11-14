package com.example.behale.database.diet

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.behale.utils.ConstantUtils
import java.util.*

@Entity(tableName = ConstantUtils.DIET_HISTORY_TABLE)
data class DietHistory(

    @PrimaryKey
    var id: Int,

    var dietName: String = "",
    var calorie: Int = 0,
    var time: Calendar = Calendar.getInstance(),
    var isReminderOn: Boolean = false

)