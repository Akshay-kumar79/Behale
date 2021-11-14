package com.example.behale.database

import android.content.Context
import android.util.Log
import androidx.room.*
import com.example.behale.database.diet.DietHistory
import com.example.behale.database.diet.DietHistoryDao
import com.example.behale.database.diet.DietReminderDao
import com.example.behale.database.diet.DietReminderModel
import com.example.behale.database.pills.PillsHistory
import com.example.behale.database.pills.PillsHistoryDao
import com.example.behale.database.pills.PillsReminderDao
import com.example.behale.database.pills.PillsReminderModel
import com.example.behale.database.water.WaterHistory
import com.example.behale.database.water.WaterHistoryDao
import com.example.behale.database.water.WaterReminderModel
import com.example.behale.database.water.WaterReminderDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

@Database(
    entities = [WaterReminderModel::class, WaterHistory::class, DietReminderModel::class, PillsReminderModel::class, DietHistory::class, PillsHistory::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class, DateListConverter::class)
abstract class ReminderDataBase : RoomDatabase(){

        abstract val waterReminderDao: WaterReminderDao
        abstract val waterHistoryDao: WaterHistoryDao
        abstract val dietReminderDao: DietReminderDao
        abstract val pillsReminderDao: PillsReminderDao
        abstract val dietHistoryDao: DietHistoryDao
        abstract val pillsHistoryDao: PillsHistoryDao

        companion object {

            @Volatile
            private var INSTANCE: ReminderDataBase? = null

            fun getInstance(context: Context): ReminderDataBase {
                synchronized(this) {
                    var instance = INSTANCE

                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            ReminderDataBase::class.java,
                            "reminder_history_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                        INSTANCE = instance
                    }
                    return instance
                }
            }
        }

}

object DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Calendar? {
        val calendar  = Calendar.getInstance()
        calendar.timeInMillis = dateLong!!
        return  calendar
    }

    @TypeConverter
    fun fromDate(date: Calendar?): Long? {
        return date?.timeInMillis
    }
}

object DateListConverter{

    @TypeConverter
    fun toDateList(value: String?): List<Calendar>{
        if (value == null) {
            return Collections.emptyList();
        }
        val type: Type = object : TypeToken<List<Calendar>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromDateList(calendars: List<Calendar>): String{
        Log.v("myTag", Gson().toJson(calendars))
        return Gson().toJson(calendars)
    }

}
