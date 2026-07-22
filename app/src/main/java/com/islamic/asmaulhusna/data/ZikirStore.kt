package com.islamic.asmaulhusna.data

import android.content.Context
import androidx.compose.runtime.mutableStateOf

/**
 * Per-name Zikir (dhikr) tap counter with an optional recitation target,
 * persisted across sessions.
 *
 * Each name keeps its own running count so the user can track how many times
 * they have recited a given name, plus a target (e.g. 33, 99, 100) to aim for.
 * Both are stored in SharedPreferences keyed by the name id — counts under the
 * plain id, targets under a "t" prefix.
 */
class ZikirStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("zikir", Context.MODE_PRIVATE)
    val counts = mutableStateOf(load(prefix = "c_"))
    val targets = mutableStateOf(load(prefix = "t_"))

    private fun load(prefix: String): Map<Int, Int> =
        prefs.all.mapNotNull { (key, value) ->
            if (!key.startsWith(prefix)) return@mapNotNull null
            val id = key.removePrefix(prefix).toIntOrNull() ?: return@mapNotNull null
            val v = (value as? Int) ?: return@mapNotNull null
            id to v
        }.toMap()

    fun countFor(id: Int): Int = counts.value[id] ?: 0

    /** Target recitations for this name, or 0 when none has been set. */
    fun targetFor(id: Int): Int = targets.value[id] ?: 0

    fun increment(id: Int) {
        val next = countFor(id) + 1
        counts.value = counts.value + (id to next)
        prefs.edit().putInt("c_$id", next).apply()
    }

    fun reset(id: Int) {
        counts.value = counts.value - id
        prefs.edit().remove("c_$id").apply()
    }

    /** Set the target; a value of 0 or less clears it. */
    fun setTarget(id: Int, target: Int) {
        if (target <= 0) {
            targets.value = targets.value - id
            prefs.edit().remove("t_$id").apply()
        } else {
            targets.value = targets.value + (id to target)
            prefs.edit().putInt("t_$id", target).apply()
        }
    }
}
