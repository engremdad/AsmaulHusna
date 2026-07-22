package com.islamic.asmaulhusna.ui

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import com.islamic.asmaulhusna.R

object AudioPlayer {
    // Disabled until we ship properly-licensed audio: the current source has no license
    // (copyright/DMCA risk for Play Store). Flip to true once bundled/licensed audio exists.
    // The playback code below is left intact so re-enabling is a one-line change.
    const val ENABLED = false

    // Source: https://github.com/MohammedAbidNafi/99-Names-of-Allah (public repo, res/raw folder)
    private const val BASE_URL =
        "https://raw.githubusercontent.com/MohammedAbidNafi/99-Names-of-Allah/master/app/src/main/res/raw/"

    // Filenames indexed by name id (1..99). Index 0 unused.
    private val FILES = arrayOf(
        "", // 0 unused
        "rahman.mp3", "rahim.mp3", "malik.mp3", "quddus.mp3", "salam.mp3",
        "mumin.mp3", "muhaimin.mp3", "aziz.mp3", "jabbar.mp3", "mutakabbir.mp3",
        "khaliq.mp3", "bari.mp3", "musawwir.mp3", "ghaffar.mp3", "qahhar.mp3",
        "wahhab.mp3", "razzaq.mp3", "fattah.mp3", "alim.mp3", "qabid.mp3",
        "basit.mp3", "khafid.mp3", "rafi.mp3", "muizz.mp3", "mudhill.mp3",
        "sami.mp3", "basir.mp3", "hakam.mp3", "adl.mp3", "latif.mp3",
        "khabir.mp3", "halim.mp3", "azim.mp3", "ghafur.mp3", "shakur.mp3",
        "ali.mp3", "kabir.mp3", "hafiz.mp3", "muqit.mp3", "hasib.mp3",
        "jalil.mp3", "karim.mp3", "raqib.mp3", "mujib.mp3", "wasi.mp3",
        "hakim.mp3", "wadud.mp3", "majeed.mp3", "baith.mp3", "shahid.mp3",
        "haqq.mp3", "wakil.mp3", "qawi.mp3", "matin.mp3", "waliy.mp3",
        "hamid.mp3", "muhsi.mp3", "mubdi.mp3", "muid.mp3", "muhyi.mp3",
        "mumit.mp3", "hayy.mp3", "qayyum.mp3", "wajid.mp3", "majid.mp3",
        "wahid.mp3", "ahad.mp3", "samad.mp3", "qadir.mp3", "muqtadir.mp3",
        "muqaddim.mp3", "muakhkhir.mp3", "awwal.mp3", "akhir.mp3", "zahir.mp3",
        "batin.mp3", "wali.mp3", "muta_ali.mp3", "barr.mp3", "tawwab.mp3",
        "muntaqim.mp3", "afuw.mp3", "rauf.mp3", "malik_ul_mulk.mp3", "dhu_l_jalali_wal_ikram.mp3",
        "muqsit.mp3", "jami.mp3", "ghaniy.mp3", "mughni.mp3", "mani.mp3",
        "darr.mp3", "nafi.mp3", "nur.mp3", "hadi.mp3", "badi.mp3",
        "baqi.mp3", "warith.mp3", "rashid.mp3", "sabur.mp3"
    )

    private var player: MediaPlayer? = null

    fun play(context: Context, nameId: Int) {
        stop()
        if (nameId !in 1..99) return
        val url = BASE_URL + FILES[nameId]
        try {
            player = MediaPlayer().apply {
                setDataSource(url)
                setOnPreparedListener { start() }
                setOnCompletionListener { stop() }
                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayer", "Error $what/$extra for $url")
                    Toast.makeText(context, R.string.audio_load_failed, Toast.LENGTH_SHORT).show()
                    stop()
                    true
                }
                prepareAsync()
            }
            Toast.makeText(context, R.string.audio_loading, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Failed: ${e.message}")
            Toast.makeText(context, R.string.audio_failed, Toast.LENGTH_SHORT).show()
        }
    }

    fun stop() {
        player?.let {
            runCatching { if (it.isPlaying) it.stop() }
            it.release()
        }
        player = null
    }
}
