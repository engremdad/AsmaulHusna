package com.islamic.asmaulhusna.notify

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

/**
 * Schedules each reminder as a daily alarm using an inexact, doze-friendly alarm
 * (setAndAllowWhileIdle) — the reminder arrives around the chosen time, within the
 * OS's tolerance window. We deliberately avoid exact alarms: Google Play restricts
 * those to apps whose core function is alarms/reminders, which this reference app is
 * not. Each firing reschedules itself for the next day (see ReminderReceiver).
 */
object ReminderScheduler {

    fun rescheduleAll(context: Context, prefs: ReminderPrefs = ReminderPrefs(context)) {
        ReminderType.entries.forEach { type ->
            if (prefs.isEnabled(type)) schedule(context, type, prefs.hour(type), prefs.minute(type))
            else cancel(context, type)
        }
    }

    fun schedule(context: Context, type: ReminderType, hour: Int, minute: Int) {
        val am = context.getSystemService(AlarmManager::class.java) ?: return
        val triggerAt = nextTriggerMillis(hour, minute)
        val pending = alarmPendingIntent(context, type)
        // Inexact but delivered even in doze; no exact-alarm permission required.
        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
    }

    fun cancel(context: Context, type: ReminderType) {
        val am = context.getSystemService(AlarmManager::class.java) ?: return
        am.cancel(alarmPendingIntent(context, type))
    }

    private fun alarmPendingIntent(context: Context, type: ReminderType): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ReminderReceiver.ACTION_FIRE
            putExtra(ReminderReceiver.EXTRA_TYPE, type.key)
        }
        return PendingIntent.getBroadcast(
            context, type.requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun nextTriggerMillis(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (next.timeInMillis <= now.timeInMillis) next.add(Calendar.DAY_OF_YEAR, 1)
        return next.timeInMillis
    }
}
