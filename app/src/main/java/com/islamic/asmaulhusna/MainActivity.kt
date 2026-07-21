package com.islamic.asmaulhusna

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.islamic.asmaulhusna.data.FavoritesStore
import com.islamic.asmaulhusna.notify.NotificationHelper
import com.islamic.asmaulhusna.notify.ReminderScheduler
import com.islamic.asmaulhusna.ui.DetailScreen
import com.islamic.asmaulhusna.ui.FavoritesScreen
import com.islamic.asmaulhusna.ui.HomeScreen
import com.islamic.asmaulhusna.ui.NotificationSettingsScreen
import com.islamic.asmaulhusna.ui.theme.AsmaulHusnaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationHelper.ensureChannels(this)
        ReminderScheduler.rescheduleAll(this)

        // A tapped reminder may deep-link to a specific name's detail.
        val openNameId = intent?.getIntExtra(NotificationHelper.EXTRA_OPEN_NAME_ID, -1)
            ?.takeIf { it > 0 }

        setContent {
            AsmaulHusnaTheme {
                val context = LocalContext.current
                val favorites = remember { FavoritesStore(context) }
                val nav = rememberNavController()

                val notifPermission = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { /* result handled by the system; nothing to do here */ }

                fun requestNotifPermission() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                LaunchedEffect(Unit) {
                    openNameId?.let { nav.navigate("detail/$it") }
                }

                NavHost(navController = nav, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            onNameClick = { id -> nav.navigate("detail/$id") },
                            onFavoritesClick = { nav.navigate("favorites") },
                            onSettingsClick = { nav.navigate("settings") }
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
                    composable("settings") {
                        NotificationSettingsScreen(
                            onBack = { nav.popBackStack() },
                            onRequestPermission = { requestNotifPermission() }
                        )
                    }
                }
            }
        }
    }
}
