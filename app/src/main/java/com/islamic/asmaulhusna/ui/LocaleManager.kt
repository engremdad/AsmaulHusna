package com.islamic.asmaulhusna.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.islamic.asmaulhusna.data.ContentStore
import com.islamic.asmaulhusna.data.NameContent
import java.util.Locale

/**
 * In-app language switching without AppCompat: we persist the chosen BCP-47 tag
 * and wrap the base context in MainActivity.attachBaseContext so the whole
 * (single-activity) Compose UI renders in that language. Layout direction is set
 * from the locale, so Arabic and Urdu lay out right-to-left automatically.
 *
 * Only the app's UI chrome is localized — the 99 names' meaning/fazilat/amal
 * content stays in its source language.
 */
enum class AppLanguage(val tag: String, val endonym: String, val english: String) {
    ENGLISH("en", "English", "English"),
    BENGALI("bn", "বাংলা", "Bengali"),
    ARABIC("ar", "العربية", "Arabic"),
    URDU("ur", "اردو", "Urdu"),
    INDONESIAN("id", "Bahasa Indonesia", "Indonesian"),
    TURKISH("tr", "Türkçe", "Turkish"),
    HINDI("hi", "हिन्दी", "Hindi");

    companion object {
        val DEFAULT = ENGLISH
        fun fromTag(tag: String?): AppLanguage = entries.firstOrNull { it.tag == tag } ?: DEFAULT
    }
}

object LocaleStore {
    private const val PREFS = "settings"
    private const val KEY_LANG = "app_language"

    fun language(context: Context): AppLanguage {
        val tag = context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_LANG, null)
        return AppLanguage.fromTag(tag)
    }

    /** False until the user has picked a language once — used to gate the first-launch screen. */
    fun isLanguageChosen(context: Context): Boolean =
        context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .contains(KEY_LANG)

    fun setLanguage(context: Context, language: AppLanguage) {
        context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANG, language.tag)
            .apply()
    }

    /** Returns a context configured for the currently-selected language. */
    fun wrap(context: Context): Context = wrap(context, language(context))

    fun wrap(context: Context, language: AppLanguage): Context {
        val locale = Locale.forLanguageTag(language.tag)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}

/** The localized name/meaning/fazilat/amal map for the currently-selected language. */
@Composable
fun rememberNameContent(): Map<Int, NameContent> {
    val context = LocalContext.current
    val lang = LocaleStore.language(context).tag
    return remember(lang) { ContentStore.forLanguage(context, lang) }
}
