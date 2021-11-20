package behale.health.reminder.waterFragment

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import behale.health.reminder.database.water.WaterHistory
import behale.health.reminder.database.water.WaterHistoryDao
import behale.health.reminder.database.water.WaterReminderModel
import behale.health.reminder.database.water.WaterReminderDao
import behale.health.reminder.receiver.ShowWaterNotificationReceiver
import behale.health.reminder.utils.ConstantUtils
import kotlinx.coroutines.*
import java.util.*

class WaterViewModel(
    private val waterReminderDatabase: WaterReminderDao,
    private val waterHistoryDatabase: WaterHistoryDao,
    application: Application
) : AndroidViewModel(application) {

    private var sp = application.getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private var spChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sp1, key ->
        when (key) {
            ConstantUtils.WATER_PROGRESS_PREFERENCE -> _waterProgress.value = sp1.getInt(ConstantUtils.WATER_PROGRESS_PREFERENCE, 0)
            ConstantUtils.CONTAINER_SIZE_PREFERENCE -> _containerSize.value = sp1.getInt(ConstantUtils.CONTAINER_SIZE_PREFERENCE , 300)
            ConstantUtils.WATER_GOAL_PREFERENCE -> _waterGoal.value = sp1.getInt(ConstantUtils.WATER_GOAL_PREFERENCE, 2530)
        }
    }

    private val alarmManager = application.getSystemService(AlarmManager::class.java) as AlarmManager
    private val notifyIntent = Intent(getApplication(), ShowWaterNotificationReceiver::class.java)


    private val _waterGoal = MutableLiveData<Int>()
    val waterGoal: LiveData<Int>
        get() = _waterGoal

    private val _waterProgress = MutableLiveData<Int>()
    val waterProgress: LiveData<Int>
        get() = _waterProgress

    private val _containerSize = MutableLiveData<Int>()
    val containerSize: LiveData<Int>
        get() = _containerSize


    val waterReminders = waterReminderDatabase.getAllReminders()

//    val waterHistories = waterHistoryDatabase.getCompleteHistory()
//
//    val waterHistoryInDays = Transformations.map(waterHistories){ historyList ->
//
//        val formatter = SimpleDateFormat("dd-MMM")
//        var currentDate = formatter.format(historyList[0].time.time.time)
//        val historyInDays = ArrayList<WaterHistory>()
//        var currentHistory = historyList[0]
//
//        for(i in 1 until historyList.size){
//            if(formatter.format(historyList[i].time.time.time) == currentDate){
//                currentHistory.waterIntake += historyList[i].waterIntake
//            }else{
//                historyInDays.add(currentHistory)
//                currentHistory = historyList[i]
//                currentDate = formatter.format(historyList[i].time.time.time)
//            }
//        }
//        historyInDays.add(currentHistory)
//
//        historyInDays
//    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        _waterGoal.value = sp.getInt(ConstantUtils.WATER_GOAL_PREFERENCE, 2530)
        _waterProgress.value = sp.getInt(ConstantUtils.WATER_PROGRESS_PREFERENCE, 0)
        _containerSize.value = sp.getInt(ConstantUtils.CONTAINER_SIZE_PREFERENCE, 300)

        sp.registerOnSharedPreferenceChangeListener(spChangeListener)
    }

    fun onAddReminderDialogOkButtonClicked(waterReminder: WaterReminderModel) = uiScope.launch {
        //adding water reminder into database
        val currentId: Long
        withContext(Dispatchers.IO) {
            currentId = waterReminderDatabase.insert(waterReminder)
        }

        //setting reminder for first time with "waterReminder.id" as request code for pending intent
        //schedule reminder if time is after current time otherwise schedule from next day
        val c = waterReminder.time

        val pendingIntent = PendingIntent.getBroadcast(getApplication(), currentId.toInt(), notifyIntent, 0)

        if (c.after(Calendar.getInstance())) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        } else {
            c.add(Calendar.DATE, 1)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        }
    }

    private var idForUpdate: Int = -1
    private val _waterReminderForUpdate = MutableLiveData<WaterReminderModel>()
    val waterReminderForUpdate : LiveData<WaterReminderModel>
    get() = _waterReminderForUpdate

    fun onEditWaterReminderClick(id: Int) = viewModelScope.launch {
        idForUpdate = id
        var temp: WaterReminderModel
        withContext(Dispatchers.IO){
            temp = waterReminderDatabase.getReminder(idForUpdate)
        }
        _waterReminderForUpdate.value = temp
    }

    fun onEditReminderDialogOkButtonClicked(waterReminder: WaterReminderModel) = uiScope.launch {

        waterReminder.id = idForUpdate
        val isChecked: Boolean
        //updating water reminder in database
        withContext(Dispatchers.IO) {
            isChecked = waterReminderDatabase.getReminder(idForUpdate).switchOn
            waterReminder.switchOn = isChecked
            waterReminderDatabase.update(waterReminder)
        }

        //updating reminder with alarm manager
        //schedule reminder if time is after current time else cancel previous set alarm
        val c = waterReminder.time

        if (isChecked) {
            val pendingIntent = PendingIntent.getBroadcast(getApplication(), waterReminder.id, notifyIntent, 0)

            if (c.after(Calendar.getInstance())) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            } else {
                c.add(Calendar.DATE, 1)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            }
        }
    }

    fun onEditReminderDeleteButtonClicked() = uiScope.launch() {

        //delete from database
        withContext(Dispatchers.IO) {
            waterReminderDatabase.delete(idForUpdate)
        }

        // cancel reminder for request code "idForUpdate"
        val pendingIntent = PendingIntent.getBroadcast(getApplication(), idForUpdate, notifyIntent, 0)
        alarmManager.cancel(pendingIntent)
        Log.v("mytag", "$idForUpdate :Reminder Deleted")

    }

    fun onWaterReminderSwitchClick(waterReminderID: Int) = uiScope.launch {

        val waterReminder: WaterReminderModel
        val isChecked: Boolean

        //updating database with new check state
        withContext(Dispatchers.IO) {
            waterReminder = waterReminderDatabase.getReminder(waterReminderID)

            isChecked = !waterReminder.switchOn
            waterReminder.switchOn = isChecked
            waterReminderDatabase.update(waterReminder)

        }

        // updating reminder
        if (isChecked) {
            val c = waterReminder.time

            val pendingIntent = PendingIntent.getBroadcast(getApplication(), waterReminderID, notifyIntent, 0)
            if (c.after(Calendar.getInstance())) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
                Log.v("mytag", "$waterReminderID is scheduled")
            }else{
                c.add(Calendar.DATE, 1)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            }
        } else {
            val pendingIntent = PendingIntent.getBroadcast(getApplication(), waterReminderID, notifyIntent, 0)
            alarmManager.cancel(pendingIntent)
            Log.v("mytag", "$waterReminderID is canceled")
        }

    }

    fun onDrinkWaterClicked() = uiScope.launch {

        // to save in history database
        var waterAmountGetDrunk = containerSize.value

        //check if water limit reached
        if (waterProgress.value!!.plus(containerSize.value!!) > 15000) {
            waterAmountGetDrunk = 15000 - waterProgress.value!!

            // run only if water progress is less then 15000 and update database
            if (waterAmountGetDrunk > 0) {
                _waterProgress.value = 15000
                sp.edit().putInt(ConstantUtils.WATER_PROGRESS_PREFERENCE, waterProgress.value!!).apply()

                withContext(Dispatchers.IO) {
                    val waterHistory = WaterHistory(time = Calendar.getInstance(), waterIntake = waterAmountGetDrunk)
                    waterHistoryDatabase.insert(waterHistory)
                }
            }

            Toast.makeText(getApplication(), "you have reached upper limit of water intake of a day", Toast.LENGTH_SHORT).show()
            return@launch
        }
        _waterProgress.value = waterProgress.value!! + containerSize.value!!
        sp.edit().putInt(ConstantUtils.WATER_PROGRESS_PREFERENCE, waterProgress.value!!).apply()

        withContext(Dispatchers.IO) {
            val waterHistory = WaterHistory(time = Calendar.getInstance(), waterIntake = waterAmountGetDrunk!!)
            waterHistoryDatabase.insert(waterHistory)
        }

    }

    fun updateContainerSize(newContainerSize: Int) {
        _containerSize.value = newContainerSize
        sp.edit().putInt(ConstantUtils.CONTAINER_SIZE_PREFERENCE, containerSize.value!!).apply()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}