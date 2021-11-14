package com.example.behale.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.behale.utils.ConstantUtils
import com.example.behale.utils.sendDietReminderNotification
import com.example.behale.utils.sendPillReminderNotification
import com.example.behale.utils.sendWaterReminderNotification


class ShowWaterNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager = ContextCompat.getSystemService(context!!, NotificationManager::class.java) as NotificationManager
        notificationManager.sendWaterReminderNotification(context, "It's time to drink water")

    }

}

class ShowDietNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager = ContextCompat.getSystemService(context!!, NotificationManager::class.java) as NotificationManager
        notificationManager.sendDietReminderNotification(context, "It's time to Eat '${intent?.getStringExtra(ConstantUtils.DIET_NAME)}'")

    }
}

class ShowPillNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager = ContextCompat.getSystemService(context!!, NotificationManager::class.java) as NotificationManager
        notificationManager.sendPillReminderNotification(context, "It's time to take '${intent?.getStringExtra(ConstantUtils.PILL_NAME)}'")

    }
}
