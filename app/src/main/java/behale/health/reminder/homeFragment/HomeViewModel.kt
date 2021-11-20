package behale.health.reminder.homeFragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import behale.health.reminder.database.ReminderDataBase
import behale.health.reminder.database.water.WaterHistory
import behale.health.reminder.database.water.WaterHistoryDao
import behale.health.reminder.utils.ConstantUtils
import kotlinx.coroutines.*
import java.util.*

class HomeViewModel(private val waterHistoryDatabase: WaterHistoryDao, application: Application) : AndroidViewModel(application) {

    private var sp: SharedPreferences = application.getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private var spChangeListener = SharedPreferences.OnSharedPreferenceChangeListener{ sp1, key ->
        when(key) {
            ConstantUtils.WATER_PROGRESS_PREFERENCE ->
                _waterProgress.value = sp1.getInt( ConstantUtils.WATER_PROGRESS_PREFERENCE, 0)

            ConstantUtils.CONTAINER_SIZE_PREFERENCE ->
                _containerSize.value = sp1.getInt( ConstantUtils.CONTAINER_SIZE_PREFERENCE, 300)

            ConstantUtils.WATER_GOAL_PREFERENCE ->
                _waterGoal.value = sp1.getInt( ConstantUtils.WATER_GOAL_PREFERENCE, 2530)

            ConstantUtils.CURRENT_STEPS_PREFERENCE ->
                _stepProgress.value = sp.getInt(ConstantUtils.CURRENT_STEPS_PREFERENCE, 0)
        }
    }

    private val _stepProgress = MutableLiveData<Int>()
    val stepProgress : LiveData<Int>
        get() = _stepProgress

    private val _stepGoal = MutableLiveData<Int>()
    val stepGoal : LiveData<Int>
        get() = _stepGoal

    private val _waterProgress = MutableLiveData<Int>()
    val waterProgress: LiveData<Int>
        get() = _waterProgress

    private val _containerSize = MutableLiveData<Int>()
    val containerSize: LiveData<Int>
        get() = _containerSize

    private val _waterGoal = MutableLiveData<Int>()
    val waterGoal: LiveData<Int>
        get() = _waterGoal

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val dietReminderDataBase = ReminderDataBase.getInstance(application).dietReminderDao
    private val pillsReminderDataBase = ReminderDataBase.getInstance(application).pillsReminderDao

    val dietReminders = dietReminderDataBase.getAllReminders()
    val pillReminders = pillsReminderDataBase.getAllReminders()

    init {
        _stepProgress.value = sp.getInt(ConstantUtils.CURRENT_STEPS_PREFERENCE, 0)
        _stepGoal.value = sp.getInt(ConstantUtils.STEPS_GOAL_PREFERENCE, 10000)
        _waterProgress.value = sp.getInt( ConstantUtils.WATER_PROGRESS_PREFERENCE, 0)
        _containerSize.value = sp.getInt( ConstantUtils.CONTAINER_SIZE_PREFERENCE, 300)
        _waterGoal.value = sp.getInt(ConstantUtils.WATER_GOAL_PREFERENCE, 2530)
        sp.registerOnSharedPreferenceChangeListener(spChangeListener)
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}