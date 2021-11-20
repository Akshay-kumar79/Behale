package behale.health.reminder.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import behale.health.reminder.utils.ConstantUtils
import behale.health.reminder.utils.sendDietReminderNotification
import behale.health.reminder.utils.sendPillReminderNotification
import behale.health.reminder.utils.sendWaterReminderNotification


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
