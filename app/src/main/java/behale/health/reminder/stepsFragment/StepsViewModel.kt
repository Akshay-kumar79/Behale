package behale.health.reminder.stepsFragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import behale.health.reminder.utils.ConstantUtils

class StepsViewModel(application: Application) : AndroidViewModel(application) {

    private val sp: SharedPreferences = application.getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)

    private val _progress = MutableLiveData<Int>()
    val progress : LiveData<Int>
        get() = _progress

    private val _stepGoal = MutableLiveData<Int>()
    val stepGoal : LiveData<Int>
    get() = _stepGoal


    val hasStepSensor = sp.getBoolean(ConstantUtils.HAS_STEP_SENSOR, false)

    private var spChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sp1, key ->
        when(key){
            ConstantUtils.CURRENT_STEPS_PREFERENCE -> _progress.value = sp.getInt(ConstantUtils.CURRENT_STEPS_PREFERENCE, 0)
            ConstantUtils.STEPS_GOAL_PREFERENCE -> _stepGoal.value = sp.getInt(ConstantUtils.STEPS_GOAL_PREFERENCE, 0)
        }
    }

    init {
        _stepGoal.value = sp.getInt(ConstantUtils.STEPS_GOAL_PREFERENCE, 10000)
        _progress.value = sp.getInt(ConstantUtils.CURRENT_STEPS_PREFERENCE, 0)
        sp.registerOnSharedPreferenceChangeListener(spChangeListener)
    }
}