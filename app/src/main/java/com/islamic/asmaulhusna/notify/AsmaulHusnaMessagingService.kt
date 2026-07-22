package com.islamic.asmaulhusna.notify

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Receives Firebase Cloud Messaging pushes — the "broadcast" half of the strategy:
 * Ramadan greetings, name-of-the-day highlights, and other announcements sent from
 * the Firebase console (or a topic). No device token is uploaded anywhere — target
 * this app from the console directly or via topic subscriptions.
 *
 * Dormant until google-services.json is added to the app module (see app/build.gradle.kts).
 * Time-critical Suhoor/Iftar reminders are handled locally instead — see ReminderScheduler.
 */
class AsmaulHusnaMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // No backend: the token is not uploaded anywhere. Logged only for local debugging.
        // Push targeting is done from the Firebase console (all instances or topics).
        Log.d(TAG, "FCM registration token refreshed")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        // Prefer the notification payload; fall back to data fields for fully custom messages.
        val title = message.notification?.title ?: message.data["title"] ?: "আসমাউল হুসনা"
        val body = message.notification?.body ?: message.data["body"].orEmpty()
        val openNameId = message.data["name_id"]?.toIntOrNull()

        NotificationHelper.show(
            context = this,
            channelId = NotificationHelper.CHANNEL_ANNOUNCEMENTS,
            notifId = (System.currentTimeMillis() % 100_000).toInt(),
            title = title,
            body = body,
            openNameId = openNameId
        )
    }

    private companion object {
        const val TAG = "FCM"
    }
}
