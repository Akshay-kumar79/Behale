package behale.health.reminder.utils


import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import behale.health.reminder.R
import behale.health.reminder.database.diet.DietReminderModel
import behale.health.reminder.database.pills.PillsReminderModel
import behale.health.reminder.database.water.WaterReminderModel
import behale.health.reminder.dietFragment.adapters.DietReminderAdapter
import behale.health.reminder.pillsFragment.adapter.AddPillTimeAdapter
import behale.health.reminder.pillsFragment.adapter.PillsReminderAdapter
import behale.health.reminder.waterFragment.adapters.WaterReminderAdapter
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
