package com.islamic.asmaulhusna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.islamic.asmaulhusna.data.FavoritesStore
import com.islamic.asmaulhusna.ui.DetailScreen
import com.islamic.asmaulhusna.ui.FavoritesScreen
import com.islamic.asmaulhusna.ui.HomeScreen
import com.islamic.asmaulhusna.ui.theme.AsmaulHusnaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AsmaulHusnaTheme {
                val context = LocalContext.current
                val favorites = remember { FavoritesStore(context) }
                val nav = rememberNavController()
                NavHost(navController = nav, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            onNameClick = { id -> nav.navigate("detail/$id") },
                            onFavoritesClick = { nav.navigate("favorites") }
                        )
                    }
                    composable(
                        "detail/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStack ->
                        val id = backStack.arguments?.getInt("id") ?: 1
                        DetailScreen(id, favorites, onBack = { nav.popBackStack() })
                    }
                    composable("favorites") {
                        FavoritesScreen(
                            favorites = favorites,
                            onNameClick = { id -> nav.navigate("detail/$id") },
                            onBack = { nav.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
