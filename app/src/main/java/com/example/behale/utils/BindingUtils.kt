package com.example.behale.utils


import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.behale.R
import com.example.behale.database.diet.DietReminderModel
import com.example.behale.database.pills.PillsReminderModel
import com.example.behale.database.water.WaterReminderModel
import com.example.behale.dietFragment.adapters.DietReminderAdapter
import com.example.behale.pillsFragment.adapter.AddPillTimeAdapter
import com.example.behale.pillsFragment.adapter.PillsReminderAdapter
import com.example.behale.waterFragment.adapters.WaterReminderAdapter
import java.text.SimpleDateFormat
import java.util.*

// Water
@BindingAdapter("listWaterReminderData")
fun listWaterReminderData(recyclerView: RecyclerView, data: List<WaterReminderModel>?) {
    val adapter = recyclerView.adapter as WaterReminderAdapter
    if (data != null) {
        adapter.setData(data)
    }
}


//Diet
@BindingAdapter("listDietReminderData")
fun listDietReminderData(recyclerView: RecyclerView, data: List<DietReminderModel>?) {
    val adapter = recyclerView.adapter as DietReminderAdapter
    if (data != null) {
        adapter.setData(data)
    }
}

@BindingAdapter(value = ["setTimeColorHour", "setTimeColorMinute"], requireAll = true)
fun setTimeColor(textView: TextView, hour: Int, minute: Int) {
    val currentTime = Calendar.getInstance()
    val proviedTime = Calendar.getInstance()
    proviedTime.set(Calendar.HOUR_OF_DAY, hour)
    proviedTime.set(Calendar.MINUTE, minute)
    proviedTime.set(Calendar.SECOND, 0)
    proviedTime.set(Calendar.MILLISECOND, 0)

    if (proviedTime.timeInMillis >= currentTime.timeInMillis) {
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.mainThemeColor2))
    } else {
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.red))
    }
}


//Pills
@BindingAdapter("listPillsReminderData")
fun listPillsReminderData(recyclerView: RecyclerView, data: List<PillsReminderModel>?) {
    val adapter = recyclerView.adapter as PillsReminderAdapter
    if (data != null) {
        adapter.setData(data)
    }
}

@BindingAdapter("listTimeDataInAddPill")
fun listTimeDataInAddPill(recyclerView: RecyclerView, data: List<Calendar>?) {
    val adapter = recyclerView.adapter as AddPillTimeAdapter
    if (data != null) {
        adapter.setData(data)
    }
}

@BindingAdapter("setTimeTextForPillDetail")
fun setTimeTextForPillDetail(textView: TextView, data: List<Calendar>?) {
    var textToShow = ""
    if (data != null) {
        for (c in data) {
            if (data.indexOf(c) == data.size - 1)
                textToShow += StringUtils.getTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))
            else
                textToShow += "${StringUtils.getTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))}, "
        }
    }
    textView.text = textToShow

}


//All
object StringUtils {

    @JvmStatic
    fun getTimeString(hour: Int, minute: Int): String {
        var newHourText: String = hour.toString()
        var newMinuteText: String = minute.toString()
        if (hour < 10) {
            newHourText = "0$hour"
        }
        if (minute < 10) {
            newMinuteText = "0$minute"
        }

        return "$newHourText:$newMinuteText"
    }

    @JvmStatic
    fun getDateString(calendar: Calendar?): String {
        val formatter = SimpleDateFormat("dd MMM, yyyy")
        if (calendar != null) {
            return formatter.format(calendar.timeInMillis)
        }
        return "null"
    }

}
