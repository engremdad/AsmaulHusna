package com.islamic.asmaulhusna.ui

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

object AudioPlayer {
    private var player: MediaPlayer? = null

    fun play(context: Context, nameId: Int) {
        stop()
        val fileName = "audio/%03d.mp3".format(nameId)
        try {
            val afd = context.assets.openFd(fileName)
            player = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                start()
                setOnCompletionListener { stop() }
            }
            afd.close()
        } catch (e: Exception) {
            Log.w("AudioPlayer", "Audio file missing: $fileName (${e.message})")
        }
    }

    fun stop() {
        player?.release()
        player = null
    }
}
