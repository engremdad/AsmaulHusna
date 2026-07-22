package com.islamic.asmaulhusna.ui

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import com.islamic.asmaulhusna.R

/**
 * Plays the 99 pronunciation clips bundled as raw resources.
 *
 * Audio: human recordings by Mohammed Sadiq via Wikimedia Commons, licensed
 * CC BY-SA 4.0. Files were renamed for Android. Attribution is surfaced in
 * Settings › Audio credits (see R.string.audio_credits_body); keep it there
 * while these files ship. Nothing streams — playback is fully offline.
 */
object AudioPlayer {
    const val ENABLED = true

    // Raw resource id for each name, indexed by name id (1..99). Index 0 unused.
    private val RES = intArrayOf(
        0, // 0 unused
        R.raw.asma_001_ar_rahman, R.raw.asma_002_ar_rahim, R.raw.asma_003_al_malik,
        R.raw.asma_004_al_quddus, R.raw.asma_005_as_salam, R.raw.asma_006_al_mumin,
        R.raw.asma_007_al_muhaymin, R.raw.asma_008_al_aziz, R.raw.asma_009_al_jabbar,
        R.raw.asma_010_al_mutakabbir, R.raw.asma_011_al_khaliq, R.raw.asma_012_al_bari,
        R.raw.asma_013_al_musawwir, R.raw.asma_014_al_ghaffar, R.raw.asma_015_al_qahhar,
        R.raw.asma_016_al_wahhab, R.raw.asma_017_ar_razzaq, R.raw.asma_018_al_fattah,
        R.raw.asma_019_al_alim, R.raw.asma_020_al_qabid, R.raw.asma_021_al_basit,
        R.raw.asma_022_al_khafid, R.raw.asma_023_ar_rafi, R.raw.asma_024_al_muizz,
        R.raw.asma_025_al_mudhill, R.raw.asma_026_as_sami, R.raw.asma_027_al_basir,
        R.raw.asma_028_al_hakam, R.raw.asma_029_al_adl, R.raw.asma_030_al_latif,
        R.raw.asma_031_al_khabir, R.raw.asma_032_al_halim, R.raw.asma_033_al_azim,
        R.raw.asma_034_al_ghafur, R.raw.asma_035_ash_shakur, R.raw.asma_036_al_ali,
        R.raw.asma_037_al_kabir, R.raw.asma_038_al_hafiz, R.raw.asma_039_al_muqit,
        R.raw.asma_040_al_hasib, R.raw.asma_041_al_jalil, R.raw.asma_042_al_karim,
        R.raw.asma_043_ar_raqib, R.raw.asma_044_al_mujib, R.raw.asma_045_al_wasi,
        R.raw.asma_046_al_hakim, R.raw.asma_047_al_wadud, R.raw.asma_048_al_majid,
        R.raw.asma_049_al_baith, R.raw.asma_050_ash_shahid, R.raw.asma_051_al_haqq,
        R.raw.asma_052_al_wakil, R.raw.asma_053_al_qawi, R.raw.asma_054_al_matin,
        R.raw.asma_055_al_wali, R.raw.asma_056_al_hamid, R.raw.asma_057_al_muhsi,
        R.raw.asma_058_al_mubdi, R.raw.asma_059_al_muid, R.raw.asma_060_al_muhyi,
        R.raw.asma_061_al_mumit, R.raw.asma_062_al_hayy, R.raw.asma_063_al_qayyum,
        R.raw.asma_064_al_wajid, R.raw.asma_065_al_majid, R.raw.asma_066_al_wahid,
        R.raw.asma_067_al_ahad, R.raw.asma_068_as_samad, R.raw.asma_069_al_qadir,
        R.raw.asma_070_al_muqtadir, R.raw.asma_071_al_muqaddim, R.raw.asma_072_al_muakhkhir,
        R.raw.asma_073_al_awwal, R.raw.asma_074_al_akhir, R.raw.asma_075_az_zahir,
        R.raw.asma_076_al_batin, R.raw.asma_077_al_wali, R.raw.asma_078_al_mutaali,
        R.raw.asma_079_al_barr, R.raw.asma_080_at_tawwab, R.raw.asma_081_al_muntaqim,
        R.raw.asma_082_al_afuw, R.raw.asma_083_ar_rauf, R.raw.asma_084_malik_ul_mulk,
        R.raw.asma_085_dhul_jalaal_wal_ikraam, R.raw.asma_086_al_muqsit, R.raw.asma_087_al_jame,
        R.raw.asma_088_al_ghani, R.raw.asma_089_al_mughni, R.raw.asma_090_al_mani,
        R.raw.asma_091_ad_darr, R.raw.asma_092_an_nafi, R.raw.asma_093_an_nur,
        R.raw.asma_094_al_hadi, R.raw.asma_095_al_badi, R.raw.asma_096_al_baqi,
        R.raw.asma_097_al_warith, R.raw.asma_098_ar_rashid, R.raw.asma_099_as_sabur
    )

    private var player: MediaPlayer? = null

    fun play(context: Context, nameId: Int) {
        stop()
        if (nameId !in 1..99) return
        try {
            // MediaPlayer.create returns null if the resource can't be opened/decoded.
            val created = MediaPlayer.create(context.applicationContext, RES[nameId])
            if (created == null) {
                Toast.makeText(context, R.string.audio_load_failed, Toast.LENGTH_SHORT).show()
                return
            }
            player = created.apply {
                setOnCompletionListener { stop() }
                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayer", "Error $what/$extra for name $nameId")
                    Toast.makeText(context, R.string.audio_load_failed, Toast.LENGTH_SHORT).show()
                    stop()
                    true
                }
                start()
            }
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
