package com.islamic.asmaulhusna.data

import android.content.Context
import androidx.compose.runtime.mutableStateOf

class FavoritesStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    val favorites = mutableStateOf(load())

    private fun load(): Set<Int> =
        prefs.getStringSet("ids", emptySet())!!.mapNotNull { it.toIntOrNull() }.toSet()

    fun toggle(id: Int) {
        val current = favorites.value.toMutableSet()
        if (!current.add(id)) current.remove(id)
        favorites.value = current
        prefs.edit().putStringSet("ids", current.map { it.toString() }.toSet()).apply()
    }

    fun isFavorite(id: Int): Boolean = favorites.value.contains(id)
}
