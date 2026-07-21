package com.islamic.asmaulhusna.ui

import android.content.Context
import androidx.compose.runtime.mutableStateOf

/**
 * Whether the Zikir tap counter is shown on each name's detail screen.
 * Toggled from Settings › Display and persisted across launches. On by default.
 */
object ZikirPrefs {
    private const val PREFS = "settings"
    private const val KEY_SHOW = "zikir_show"

    private val state = mutableStateOf(true)
    private var loaded = false

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun isCounterShown(context: Context): Boolean {
        if (!loaded) {
            state.value = prefs(context).getBoolean(KEY_SHOW, true)
            loaded = true
        }
        return state.value
    }

    fun setCounterShown(context: Context, shown: Boolean) {
        state.value = shown
        prefs(context).edit().putBoolean(KEY_SHOW, shown).apply()
    }
}
