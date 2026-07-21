package com.islamic.asmaulhusna.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.islamic.asmaulhusna.data.AsmaulHusnaRepository

/** Fires when a reminder alarm goes off: posts the notification, then re-arms for tomorrow. */
class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_FIRE = "com.islamic.asmaulhusna.action.REMINDER_FIRE"
        const val EXTRA_TYPE = "type"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = ReminderType.fromKey(intent.getStringExtra(EXTRA_TYPE)) ?: return

        when (type) {
            ReminderType.DAILY_NAME -> {
                val name = AsmaulHusnaRepository.today()
                NotificationHelper.show(
                    context,
                    NotificationHelper.CHANNEL_REMINDERS,
                    type.notifId,
                    title = "আজকের নাম · ${name.transliteration}",
                    body = "${name.arabic} — ${name.banglaName} (${name.meaning})",
                    openNameId = name.id
                )
            }
            ReminderType.SUHOOR -> NotificationHelper.show(
                context, NotificationHelper.CHANNEL_REMINDERS, type.notifId,
                title = "সাহরির সময় ঘনিয়ে এসেছে",
                body = "সাহরি শেষ হওয়ার আগে খাবার সম্পন্ন করুন এবং রোজার নিয়ত করুন।"
            )
            ReminderType.IFTAR -> NotificationHelper.show(
                context, NotificationHelper.CHANNEL_REMINDERS, type.notifId,
                title = "ইফতারের সময়",
                body = "রোজা ভাঙার সময় হয়েছে — এটি দোয়া কবুলের বরকতময় মুহূর্ত।"
            )
        }

        // Re-arm for the next day if the reminder is still enabled.
        val prefs = ReminderPrefs(context)
        if (prefs.isEnabled(type)) {
            ReminderScheduler.schedule(context, type, prefs.hour(type), prefs.minute(type))
        }
    }
}
