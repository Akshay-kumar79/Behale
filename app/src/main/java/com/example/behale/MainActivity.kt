package com.example.behale

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.behale.receiver.ResetAtNightReceiver
import com.example.behale.receiver.StepCounterService
import java.util.*

class MainActivity : AppCompatActivity() {

    private val alarmManager by lazy { getSystemService(AlarmManager::class.java) as AlarmManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        createWaterChannel(getString(R.string.water_reminder_notification_channel_id), "Water")
        createDietChannel(getString(R.string.diet_reminder_notification_channel_id), "Diet")
        createPillChannel(getString(R.string.pill_reminder_notification_channel_id), "Pill")

        val stepCounterIntent = Intent(this, StepCounterService::class.java)
        startService(stepCounterIntent)

        val resetIntent = Intent(this, ResetAtNightReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 111111, resetIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

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

    override fun onDestroy() {
        super.onDestroy()
        val stepCounterIntent = Intent(this, StepCounterService::class.java)
        stopService(stepCounterIntent)
    }

}