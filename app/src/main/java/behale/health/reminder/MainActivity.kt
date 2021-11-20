package behale.health.reminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import behale.health.reminder.receiver.StepCounterService
import behale.health.reminder.utils.ConstantUtils

class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION: Int = 100
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = this.getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        if (!sp.getBoolean(ConstantUtils.IS_ACTIVITY_RECOGNITION_PERMISSION_GRANTED, false))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                        MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION
                    )
                }

        val stepCounterIntent = Intent(this, StepCounterService::class.java)
        startService(stepCounterIntent)

        createWaterChannel(getString(R.string.water_reminder_notification_channel_id), "Water")
        createDietChannel(getString(R.string.diet_reminder_notification_channel_id), "Diet")
        createPillChannel(getString(R.string.pill_reminder_notification_channel_id), "Pill")


    }

    private fun createWaterChannel(channelID: String, channelName: String) {
        val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.description = "Water Reminder"
            notificationChannel.lightColor = Color.RED
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), attributes)

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createDietChannel(channelID: String, channelName: String) {
        val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.description = "Diet Reminder"
            notificationChannel.lightColor = Color.RED
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), attributes)

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createPillChannel(channelID: String, channelName: String) {
        val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.description = "Pill Reminder"
            notificationChannel.lightColor = Color.RED
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), attributes)

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sp.edit().putBoolean(ConstantUtils.IS_ACTIVITY_RECOGNITION_PERMISSION_GRANTED, true).apply()
                val stepCounterIntent = Intent(this, StepCounterService::class.java)
                startService(stepCounterIntent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val stepCounterIntent = Intent(this, StepCounterService::class.java)
        stopService(stepCounterIntent)
    }

}