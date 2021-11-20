package behale.health.reminder.receiver

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import behale.health.reminder.utils.ConstantUtils

class StepCounterService : Service(),SensorEventListener {

    private var sensorManager: SensorManager? = null

    private lateinit var sp : SharedPreferences
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    override fun onCreate() {

        super.onCreate()
        sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sp = applicationContext.getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {
            sp.edit().putBoolean(ConstantUtils.HAS_STEP_SENSOR, true).apply()
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }else{
            sp.edit().putBoolean(ConstantUtils.HAS_STEP_SENSOR, false).apply()
        }

        return START_STICKY

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if(!sp.getBoolean(ConstantUtils.FIRST_STEP_VALUE_STORED_PREFERENCE, false)){
            sp.edit().putFloat(ConstantUtils.FIRST_STEP_VALUE_PREFERENCE,event!!.values[0]).apply()
            sp.edit().putBoolean(ConstantUtils.FIRST_STEP_VALUE_STORED_PREFERENCE, true).apply()
        }

        totalSteps = event!!.values[0]
        previousTotalSteps = sp.getFloat(ConstantUtils.FIRST_STEP_VALUE_PREFERENCE, 0f)
        val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()

        sp.edit().putInt(ConstantUtils.CURRENT_STEPS_PREFERENCE, currentSteps).apply()

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}