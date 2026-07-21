package com.islamic.asmaulhusna.data

import android.content.Context
import org.json.JSONArray

/**
 * The per-language, translatable content for a name: its localized name and the
 * meaning / fazilat / amal text. Loaded from assets/content/{lang}.json.
 */
data class NameContent(
    val id: Int,
    val name: String,
    val meaning: String,
    val fazilat: String,
    val amal: String
)

/**
 * Loads and caches localized name content per language. Falls back to Bengali
 * (the source language) when a translation file is missing, and to the built-in
 * data as a last resort — so the app always shows something.
 */
object ContentStore {
    private val cache = HashMap<String, Map<Int, NameContent>>()

    fun forLanguage(context: Context, lang: String): Map<Int, NameContent> {
        cache[lang]?.let { return it }
        val map = load(context, lang) ?: load(context, "bn") ?: emptyMap()
        cache[lang] = map
        return map
    }

    private fun load(context: Context, lang: String): Map<Int, NameContent>? = try {
        val text = context.assets.open("content/$lang.json")
            .bufferedReader(Charsets.UTF_8).use { it.readText() }
        val arr = JSONArray(text)
        buildMap {
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val id = o.getInt("id")
                put(id, NameContent(
                    id = id,
                    name = o.getString("name"),
                    meaning = o.getString("meaning"),
                    fazilat = o.getString("fazilat"),
                    amal = o.getString("amal")
                ))
            }
        }.takeIf { it.isNotEmpty() }
    } catch (e: Exception) {
        null
    }
}

/** Localized content for this name, falling back to the built-in Bengali fields. */
fun AsmaulHusna.localized(content: Map<Int, NameContent>): NameContent =
    content[id] ?: NameContent(id, banglaName, meaning, fazilat, amal)
