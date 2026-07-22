package com.islamic.asmaulhusna.notify

import android.content.Context
import androidx.annotation.StringRes
import com.islamic.asmaulhusna.R

/**
 * The three local, offline reminders. Each has stable IDs so its alarm and
 * notification can be scheduled, cancelled, and replaced deterministically.
 * Labels/hints are string resources so they follow the app's chosen language.
 */
enum class ReminderType(
    val key: String,
    val notifId: Int,
    val requestCode: Int,
    val defaultHour: Int,
    val defaultMinute: Int,
    @StringRes val labelRes: Int,
    @StringRes val hintRes: Int
) {
    DAILY_NAME("daily_name", 1001, 2001, 8, 0,
        R.string.reminder_daily_name, R.string.reminder_daily_name_hint),
    SUHOOR("suhoor", 1002, 2002, 4, 30,
        R.string.reminder_suhoor, R.string.reminder_suhoor_hint),
    IFTAR("iftar", 1003, 2003, 18, 30,
        R.string.reminder_iftar, R.string.reminder_iftar_hint);

    companion object {
        fun fromKey(key: String?): ReminderType? = entries.firstOrNull { it.key == key }
    }
}

/** SharedPreferences-backed toggle + time for each reminder. */
class ReminderPrefs(context: Context) {
    private val prefs =
        context.applicationContext.getSharedPreferences("reminders", Context.MODE_PRIVATE)

    fun isEnabled(t: ReminderType): Boolean = prefs.getBoolean("${t.key}_on", false)
    fun hour(t: ReminderType): Int = prefs.getInt("${t.key}_h", t.defaultHour)
    fun minute(t: ReminderType): Int = prefs.getInt("${t.key}_m", t.defaultMinute)

    fun set(t: ReminderType, enabled: Boolean, hour: Int, minute: Int) {
        prefs.edit()
            .putBoolean("${t.key}_on", enabled)
            .putInt("${t.key}_h", hour)
            .putInt("${t.key}_m", minute)
            .apply()
    }
}
