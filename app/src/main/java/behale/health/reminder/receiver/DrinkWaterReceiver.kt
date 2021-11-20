package behale.health.reminder.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.core.content.ContextCompat
import behale.health.reminder.database.ReminderDataBase
import behale.health.reminder.database.water.WaterHistory
import behale.health.reminder.database.water.WaterHistoryDao
import behale.health.reminder.utils.ConstantUtils
import kotlinx.coroutines.*
import java.util.*

class DrinkWaterReceiver : BroadcastReceiver() {

    private lateinit var sp: SharedPreferences
    private var waterProgress = 0
    private var containerSize = 0

    private lateinit var waterHistoryDatabase: WaterHistoryDao
    lateinit var mContext: Context

    override fun onReceive(context: Context?, intent: Intent?) {

        waterHistoryDatabase = ReminderDataBase.getInstance(context!!.applicationContext).waterHistoryDao
        mContext = context

        sp = context.getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        waterProgress = sp.getInt(ConstantUtils.WATER_PROGRESS_PREFERENCE, 0)
        containerSize = sp.getInt(ConstantUtils.CONTAINER_SIZE_PREFERENCE, 300)

        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager

        drinkWater()
        notificationManager.cancelAll()
    }

    private fun drinkWater() = GlobalScope.launch {

        // to save in history database
        var waterAmountGetDrunk = containerSize

        //check if water limit reached
        if (waterProgress.plus(containerSize) > 15000) {
            waterAmountGetDrunk = 15000 - waterProgress

            // run only if water progress is less then 15000 and update database
            if (waterAmountGetDrunk > 0) {
                waterProgress = 15000
                sp.edit().putInt(ConstantUtils.WATER_PROGRESS_PREFERENCE, waterProgress).apply()

                val waterHistory = WaterHistory(time = Calendar.getInstance(), waterIntake = waterAmountGetDrunk)
                waterHistoryDatabase.insert(waterHistory)

            }

            withContext(Dispatchers.Main) {
                Toast.makeText(mContext, "you have reached upper limit of water intake of a day", Toast.LENGTH_SHORT).show()
            }
            return@launch
        }
        waterProgress += containerSize
        sp.edit().putInt(ConstantUtils.WATER_PROGRESS_PREFERENCE, waterProgress).apply()

        val waterHistory = WaterHistory(time = Calendar.getInstance(), waterIntake = waterAmountGetDrunk)
        waterHistoryDatabase.insert(waterHistory)

    }

}