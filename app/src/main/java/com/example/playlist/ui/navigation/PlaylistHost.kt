package com.example.playlist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playlist.ui.screens.MainScreen
import com.example.playlist.ui.screens.PlaylistsScreen
import com.example.playlist.ui.screens.SearchScreen
import com.example.playlist.ui.screens.SettingsScreen

@Composable
fun PlaylistHost(navController: NavHostController) {
    val goToSearch = { navController.navigate(Screen.Search.name) }
    val goToPlaylists = { navController.navigate(Screen.Playlists.name) }
    val goToSettings = { navController.navigate(Screen.Settings.name) }
    val back: () -> Unit = { navController.popBackStack() }

    NavHost(
        navController = navController,
        startDestination = Screen.Main.name
    ) {
        composable(Screen.Main.name) {
            MainScreen(
                onSearchClick = goToSearch,
                onPlaylistsClick = goToPlaylists,
                onSettingsClick = goToSettings
            )
        }
        composable(Screen.Search.name) {
            SearchScreen(onBack = back)
        }
        composable(Screen.Playlists.name) {
            PlaylistsScreen(onBack = back)
        }
        composable(Screen.Settings.name) {
            SettingsScreen(onBack = back)
        }
    }
}
