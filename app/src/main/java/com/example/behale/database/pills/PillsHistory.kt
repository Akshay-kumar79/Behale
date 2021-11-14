package com.example.behale.database.pills

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.behale.utils.ConstantUtils
import java.util.*

@Entity(tableName = ConstantUtils.PILL_HISTORY_TABLE)
data class PillsHistory(

    @PrimaryKey
    var id: Int,

    var pillName: String = "",
    var time: Calendar = Calendar.getInstance(),

)