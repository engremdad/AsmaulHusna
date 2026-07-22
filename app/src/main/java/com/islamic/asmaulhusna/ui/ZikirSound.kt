package com.islamic.asmaulhusna.ui

import android.media.AudioManager
import android.media.ToneGenerator

/**
 * A short click played on each Zikir tap when the sound toggle is on.
 * Uses [ToneGenerator] so no audio asset is needed; the instance is created
 * lazily and reused. Creation can fail on some devices, in which case we stay
 * silent rather than crash.
 */
object ZikirSound {
    private var tone: ToneGenerator? = null

    private fun generator(): ToneGenerator? {
        if (tone == null) {
            tone = try {
                ToneGenerator(AudioManager.STREAM_MUSIC, 70)
            } catch (_: RuntimeException) {
                null
            }
        }
        return tone
    }

    /** Plays a brief tick. Safe to call rapidly; overlapping taps just restart the tone. */
    fun click() {
        try {
            generator()?.startTone(ToneGenerator.TONE_PROP_BEEP, 90)
        } catch (_: RuntimeException) {
            // A dead/released generator: drop it so the next call rebuilds it.
            tone = null
        }
    }
}
