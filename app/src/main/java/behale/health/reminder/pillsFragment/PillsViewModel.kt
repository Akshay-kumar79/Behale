package behale.health.reminder.pillsFragment

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.*
import behale.health.reminder.database.ReminderDataBase
import behale.health.reminder.database.pills.PillsReminderModel
import behale.health.reminder.receiver.ShowPillNotificationReceiver
import behale.health.reminder.utils.ConstantUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PillsViewModel(application: Application) : AndroidViewModel(application) {

    private val alarmManager = application.getSystemService(AlarmManager::class.java) as AlarmManager

    private val pillReminderDataBase = ReminderDataBase.getInstance(application).pillsReminderDao
    private val pillHistoryDataBase = ReminderDataBase.getInstance(application).pillsHistoryDao

    val pillReminders = pillReminderDataBase.getAllReminders()

    private val _spinnerSelectedItem = MutableLiveData<Int>()
    val spinnerSelectedItem: LiveData<Int>
        get() = _spinnerSelectedItem

    private val _sevenDayListForChip = MutableLiveData<List<String>>()
    val sevenDayListForChip: LiveData<List<String>>
        get() = _sevenDayListForChip

    private val _listOfTimeForAddPill = MutableLiveData<List<Calendar>>()
    val listOfTimeForAddPill: LiveData<List<Calendar>>
        get() = _listOfTimeForAddPill

    private var mutableListOfTime: MutableList<Calendar> = mutableListOf()
    val timePickerDialogListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        val time = Calendar.getInstance()
        time.set(Calendar.HOUR_OF_DAY, hourOfDay)
        time.set(Calendar.MINUTE, minute)
        time.set(Calendar.SECOND, 0)
        time.set(Calendar.MILLISECOND, 0)
        mutableListOfTime.add(time)
        _listOfTimeForAddPill.value = mutableListOfTime
    }

    init {
        _listOfTimeForAddPill.value = ArrayList()
        _sevenDayListForChip.value = ArrayList()
        _sevenDayListForChip.value = getListOfNextSevenDays()
    }


    private var idForUpdate = -1
    private val _pillReminderForUpdate = MutableLiveData<PillsReminderModel>()
    val pillReminderForUpdate: LiveData<PillsReminderModel>
        get() = _pillReminderForUpdate

    fun onEditPillReminderClicked(id: Int) = viewModelScope.launch {
        idForUpdate = id
        val temp: PillsReminderModel
        withContext(Dispatchers.IO) {
            temp = pillReminderDataBase.getReminder(idForUpdate)
        }
        _pillReminderForUpdate.value = temp
    }

    fun onEditPillDeleteButtonClicked() = viewModelScope.launch {

        val pillReminder: PillsReminderModel
        withContext(Dispatchers.IO) {
            pillReminder = pillReminderDataBase.getReminder(idForUpdate)
            pillReminderDataBase.delete(idForUpdate)
        }

        if(pillReminder.type == ConstantUtils.ADD_PILL_TYPE_EVERYDAY){
            for (c in pillReminder.time){
                val notifyIntent = Intent(getApplication(), ShowPillNotificationReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(getApplication(), idForUpdate + pillReminder.time.indexOf(c) * 100000, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.cancel(pendingIntent)
            }
        }else{
            val notifyIntent = Intent(getApplication(), ShowPillNotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(getApplication(), idForUpdate, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)
        }

    }


    fun onAddPillTickButtonClick(pillsReminder: PillsReminderModel) = viewModelScope.launch {

        var currentId: Long
        withContext(Dispatchers.IO) {
            currentId = pillReminderDataBase.insert(pillsReminder)
        }

        if (pillsReminder.type == ConstantUtils.ADD_PILL_TYPE_ALTERNATIVE_DAY || pillsReminder.type == ConstantUtils.ADD_PILL_TYPE_PERIODIC) {
            val c = pillsReminder.time[0]
            val startFrom = pillsReminder.startFrom

            startFrom.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY))
            startFrom.set(Calendar.MINUTE, c.get(Calendar.MINUTE))
            startFrom.set(Calendar.SECOND, 0)
            startFrom.set(Calendar.MILLISECOND, 0)

            val notifyIntent = Intent(getApplication(), ShowPillNotificationReceiver::class.java)
            notifyIntent.putExtra(ConstantUtils.PILL_NAME, pillsReminder.pillName)
            val pendingIntent = PendingIntent.getBroadcast(getApplication(), currentId.toInt(), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            if (startFrom.after(Calendar.getInstance())) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startFrom.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            } else {
                startFrom.add(Calendar.DATE, pillsReminder.period)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startFrom.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            }
        } else {
            for (c in pillsReminder.time) {
                val startFrom = pillsReminder.startFrom

                startFrom.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY))
                startFrom.set(Calendar.MINUTE, c.get(Calendar.MINUTE))
                startFrom.set(Calendar.SECOND, 0)
                startFrom.set(Calendar.MILLISECOND, 0)

                val notifyIntent = Intent(getApplication(), ShowPillNotificationReceiver::class.java)
                notifyIntent.putExtra(ConstantUtils.PILL_NAME, pillsReminder.pillName)
                val pendingIntent = PendingIntent.getBroadcast(getApplication(), currentId.toInt() + pillsReminder.time.indexOf(c) * 100000, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                if (startFrom.after(Calendar.getInstance())) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startFrom.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
                } else {
                    startFrom.add(Calendar.DATE, pillsReminder.period)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startFrom.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
                }

                Log.v("mytag", "${currentId.toInt() + pillsReminder.time.indexOf(c)*100000}")
            }
        }
    }

    fun onAddPillSpinnerItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        _spinnerSelectedItem.value = position
    }

    private fun getListOfNextSevenDays(): ArrayList<String> {
        val todayDate = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd-MMM")

        val sevenDayList: ArrayList<String> = ArrayList()
        sevenDayList.add("Today")

        for (i in 1..6) {
            todayDate.add(Calendar.DAY_OF_YEAR, 1)
            sevenDayList.add(formatter.format(todayDate.time.time))
        }

        return sevenDayList
    }

    fun clearAddPillScreenCache() {
        mutableListOfTime.clear()
        _listOfTimeForAddPill.value = mutableListOfTime
    }


}