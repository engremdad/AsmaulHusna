package com.islamic.asmaulhusna.ui

import android.content.Context
import androidx.compose.runtime.mutableFloatStateOf

/**
 * App-wide text size multiplier, applied on top of the system font scale by
 * adjusting LocalDensity in MainActivity. Persisted so the chosen size sticks
 * across launches. 1.0 is the app's default size.
 */
object TextScaleStore {
    private const val PREFS = "settings"
    private const val KEY_SCALE = "text_scale"

    const val MIN = 0.8f
    const val MAX = 1.6f
    const val STEP = 0.1f
    const val DEFAULT = 1.0f

    // Reactive holder so Compose re-lays-out the whole app when the size changes.
    private val state = mutableFloatStateOf(DEFAULT)
    private var loaded = false

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun scale(context: Context): Float {
        if (!loaded) {
            state.floatValue = prefs(context).getFloat(KEY_SCALE, DEFAULT)
            loaded = true
        }
        return state.floatValue
    }

    fun setScale(context: Context, value: Float) {
        val clamped = value.coerceIn(MIN, MAX)
        state.floatValue = clamped
        prefs(context).edit().putFloat(KEY_SCALE, clamped).apply()
    }
}
