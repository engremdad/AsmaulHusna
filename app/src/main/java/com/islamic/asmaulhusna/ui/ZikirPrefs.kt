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
    private const val KEY_SOUND = "zikir_sound"

    private val state = mutableStateOf(true)
    private var loaded = false

    // A tap sound while counting. Off by default — silent counting is the norm.
    private val soundState = mutableStateOf(false)
    private var soundLoaded = false

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

    fun isSoundOn(context: Context): Boolean {
        if (!soundLoaded) {
            soundState.value = prefs(context).getBoolean(KEY_SOUND, false)
            soundLoaded = true
        }
        return soundState.value
    }

    fun setSoundOn(context: Context, on: Boolean) {
        soundState.value = on
        prefs(context).edit().putBoolean(KEY_SOUND, on).apply()
    }
}
