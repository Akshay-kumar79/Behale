package com.example.behale.database.pills

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.behale.utils.ConstantUtils
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = ConstantUtils.DAILY_PILL_REMINDER_TABLE)
data class PillsReminderModel(

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,

    var pillName: String = "",
    var type: String = ConstantUtils.ADD_PILL_TYPE_EVERYDAY,
    var time: List<Calendar> = ArrayList(),
    var startFrom : Calendar = Calendar.getInstance(),
    var period: Int = 1

)