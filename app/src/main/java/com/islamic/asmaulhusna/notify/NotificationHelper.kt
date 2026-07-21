package com.islamic.asmaulhusna.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.islamic.asmaulhusna.MainActivity
import com.islamic.asmaulhusna.R

/** Creates channels and posts notifications for both local reminders and FCM messages. */
object NotificationHelper {

    /** Time-critical daily reminders: today's name, Suhoor, Iftar. */
    const val CHANNEL_REMINDERS = "reminders"

    /** Broadcast announcements delivered over FCM (Ramadan greetings, etc.). */
    const val CHANNEL_ANNOUNCEMENTS = "announcements"

    /** Intent extra used to deep-link a notification to a specific name's detail screen. */
    const val EXTRA_OPEN_NAME_ID = "open_name_id"

    private const val GOLD = 0xFFD4AF37.toInt()

    fun ensureChannels(context: Context) {
        val nm = context.getSystemService(NotificationManager::class.java) ?: return
        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_REMINDERS, "দৈনিক রিমাইন্ডার", NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "আজকের নাম, সাহরি ও ইফতারের রিমাইন্ডার" }
        )
        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ANNOUNCEMENTS, "ঘোষণা", NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "রমজান ও বিশেষ ঘোষণা" }
        )
    }

    fun show(
        context: Context,
        channelId: String,
        notifId: Int,
        title: String,
        body: String,
        openNameId: Int? = null
    ) {
        ensureChannels(context)

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (openNameId != null) putExtra(EXTRA_OPEN_NAME_ID, openNameId)
        }
        val pending = PendingIntent.getActivity(
            context, notifId, tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setColor(GOLD)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pending)
            .build()

        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return
        try {
            manager.notify(notifId, notification)
        } catch (_: SecurityException) {
            // POST_NOTIFICATIONS not granted — nothing to post.
        }
    }
}
