package com.example.behale.dietFragment

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.behale.database.ReminderDataBase
import com.example.behale.database.diet.DietHistory
import com.example.behale.database.diet.DietHistoryDao
import com.example.behale.database.diet.DietReminderDao
import com.example.behale.database.diet.DietReminderModel
import com.example.behale.receiver.ShowDietNotificationReceiver
import com.example.behale.receiver.ShowWaterNotificationReceiver
import com.example.behale.utils.ConstantUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DietViewModel(application: Application) : AndroidViewModel(application) {

    private val dietReminderDatabase : DietReminderDao = ReminderDataBase.getInstance(application).dietReminderDao
    private val dietHistoryDataBase = ReminderDataBase.getInstance(application).dietHistoryDao

    private var sp = application.getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private var spChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sp1, key ->
        when (key) {
            ConstantUtils.DAILY_BURNOUT_PREFERENCE -> _dailyBurnout.value = sp1.getInt(ConstantUtils.DAILY_BURNOUT_PREFERENCE, 0)
        }
    }

    private val _dailyBurnout = MutableLiveData<Int>()
    val dailyBurnout: LiveData<Int>
        get() = _dailyBurnout

    val dietReminders = dietReminderDatabase.getAllReminders()
    val progress = Transformations.map(dietReminders){
        var prog = 0
        for(i in it){
            prog += i.calorie
        }
        prog
    }

    private val alarmManager = application.getSystemService(AlarmManager::class.java) as AlarmManager

    init {
        _dailyBurnout.value = sp.getInt(ConstantUtils.DAILY_BURNOUT_PREFERENCE, 2200)
        sp.registerOnSharedPreferenceChangeListener(spChangeListener)
    }

    fun onAddDietDialogOkButtonClicked(dietReminderModel: DietReminderModel) = viewModelScope.launch {

        val dietHistory = DietHistory(
            id = 0,
            dietName = dietReminderModel.dietName,
            calorie = dietReminderModel.calorie,
            time = dietReminderModel.time,
            isReminderOn = dietReminderModel.isReminderOn
        )

        val currentId: Long
        withContext(Dispatchers.IO){
            currentId = dietReminderDatabase.insert(dietReminderModel)
            dietHistory.id = currentId.toInt()
            dietHistoryDataBase.insert(dietHistory)
        }

        val c = dietReminderModel.time

        if(dietReminderModel.isReminderOn) {

            val notifyIntent = Intent(getApplication(), ShowDietNotificationReceiver::class.java)
            notifyIntent.putExtra(ConstantUtils.DIET_NAME, dietReminderModel.dietName)
            val pendingIntent = PendingIntent.getBroadcast(getApplication(), currentId.toInt(), notifyIntent, 0)

            if (c.after(Calendar.getInstance())) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
                Log.v("mytag", "${c.timeInMillis}")
            } else {
                c.add(Calendar.DATE, 1)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            }
        }

    }

    private var idForUpdate = -1
    private val _dietReminderForUpdate = MutableLiveData<DietReminderModel>()
    val dietReminderForUpdate : LiveData<DietReminderModel>
        get() = _dietReminderForUpdate

    fun onEditDietReminderClicked(id: Int) = viewModelScope.launch{
        idForUpdate = id
        var temp: DietReminderModel
        withContext(Dispatchers.IO){
            temp = dietReminderDatabase.getReminder(id)
        }
        _dietReminderForUpdate.value = temp
    }

    fun onEditDietDialogDeleteButtonClicked() = viewModelScope.launch{

        withContext(Dispatchers.IO){
            dietReminderDatabase.delete(idForUpdate)
            dietHistoryDataBase.delete(idForUpdate)
        }

        val notifyIntent = Intent(getApplication(), ShowDietNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(getApplication(), idForUpdate, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)

    }

    fun onEditDietDialogOkButtonClicked(dietReminderModel: DietReminderModel) = viewModelScope.launch {

        dietReminderModel.id = idForUpdate

        val dietHistory = DietHistory(
            id = idForUpdate,
            dietName = dietReminderModel.dietName,
            calorie = dietReminderModel.calorie,
            time = dietReminderModel.time,
            isReminderOn = dietReminderModel.isReminderOn
        )

        withContext(Dispatchers.IO){
            dietReminderDatabase.update(dietReminderModel)
            dietHistoryDataBase.update(dietHistory)
        }

        val c = dietReminderModel.time
        val notifyIntent = Intent(getApplication(), ShowDietNotificationReceiver::class.java)
        notifyIntent.putExtra(ConstantUtils.DIET_NAME, dietReminderModel.dietName)
        Log.v("mytag", dietReminderModel.dietName)
        val pendingIntent = PendingIntent.getBroadcast(getApplication(), dietReminderModel.id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (dietReminderModel.isReminderOn) {

            if (c.after(Calendar.getInstance())) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            } else {
                c.add(Calendar.DATE, 1)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            }
        }else{
            alarmManager.cancel(pendingIntent)
        }

    }

    override fun onCleared() {
        super.onCleared()
        sp.unregisterOnSharedPreferenceChangeListener(spChangeListener)
    }
}