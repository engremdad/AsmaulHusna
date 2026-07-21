package com.islamic.asmaulhusna.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.islamic.asmaulhusna.R
import com.islamic.asmaulhusna.data.AsmaulHusnaRepository
import com.islamic.asmaulhusna.data.ContentStore
import com.islamic.asmaulhusna.data.localized
import com.islamic.asmaulhusna.ui.LocaleStore

/** Fires when a reminder alarm goes off: posts the notification, then re-arms for tomorrow. */
class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_FIRE = "com.islamic.asmaulhusna.action.REMINDER_FIRE"
        const val EXTRA_TYPE = "type"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = ReminderType.fromKey(intent.getStringExtra(EXTRA_TYPE)) ?: return
        val l = LocaleStore.wrap(context) // render notification text in the chosen language

        when (type) {
            ReminderType.DAILY_NAME -> {
                val name = AsmaulHusnaRepository.today()
                val c = name.localized(ContentStore.forLanguage(context, LocaleStore.language(context).tag))
                NotificationHelper.show(
                    context,
                    NotificationHelper.CHANNEL_REMINDERS,
                    type.notifId,
                    title = l.getString(R.string.notif_daily_title, name.transliteration),
                    body = "${name.arabic} — ${c.name} (${c.meaning})",
                    openNameId = name.id
                )
            }
            ReminderType.SUHOOR -> NotificationHelper.show(
                context, NotificationHelper.CHANNEL_REMINDERS, type.notifId,
                title = l.getString(R.string.notif_suhoor_title),
                body = l.getString(R.string.notif_suhoor_body)
            )
            ReminderType.IFTAR -> NotificationHelper.show(
                context, NotificationHelper.CHANNEL_REMINDERS, type.notifId,
                title = l.getString(R.string.notif_iftar_title),
                body = l.getString(R.string.notif_iftar_body)
            )
        }

        // Re-arm for the next day if the reminder is still enabled.
        val prefs = ReminderPrefs(context)
        if (prefs.isEnabled(type)) {
            ReminderScheduler.schedule(context, type, prefs.hour(type), prefs.minute(type))
        }
    }
}
