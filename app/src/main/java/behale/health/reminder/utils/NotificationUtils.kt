package behale.health.reminder.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import behale.health.reminder.R
import behale.health.reminder.receiver.DrinkWaterReceiver


private const val WATER_NOTIFICATION_ID = 0
private const val DIET_NOTIFICATION_ID = 1
private const val PILL_NOTIFICATION_ID = 2

fun NotificationManager.sendWaterReminderNotification(context: Context, contentText: String){

    val drinkWaterIntent = Intent(context, DrinkWaterReceiver::class.java)
    val drinkWaterPendingIntent = PendingIntent.getBroadcast(context, 0, drinkWaterIntent, 0)

    val notificationBuilder = NotificationCompat.Builder(context, context.getString(R.string.water_reminder_notification_channel_id))
        .setSmallIcon(R.drawable.ic_waterdrop)
        .setContentTitle("Water Reminder")
        .setContentText(contentText)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .addAction(R.drawable.ic_waterdrop, "drink now", drinkWaterPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(WATER_NOTIFICATION_ID, notificationBuilder.build())
}

fun NotificationManager.sendDietReminderNotification(context: Context, contentText: String){

    val notificationBuilder = NotificationCompat.Builder(context, context.getString(R.string.diet_reminder_notification_channel_id))
        .setSmallIcon(R.drawable.ic_icon_map_food)
        .setContentTitle("Diet Reminder")
        .setContentText(contentText)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(DIET_NOTIFICATION_ID, notificationBuilder.build())
}

fun NotificationManager.sendPillReminderNotification(context: Context, contentText: String){

    val notificationBuilder = NotificationCompat.Builder(context, context.getString(R.string.pill_reminder_notification_channel_id))
        .setSmallIcon(R.drawable.ic_pills)
        .setContentTitle("Diet Reminder")
        .setContentText(contentText)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(PILL_NOTIFICATION_ID, notificationBuilder.build())
}