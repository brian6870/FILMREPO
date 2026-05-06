package com.filmpire.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.filmpire.app.ui.screens.*

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Movies : Screen("movies", "Movies")
    object TvSeries : Screen("tv", "TV Series")
    object Search : Screen("search", "Search")
    object Player : Screen("player", "Player")
    object Settings : Screen("settings", "Settings")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmpireApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    val isTv = remember { TvDetector.isTelevision() }

    if (isTv) {
        TvApp(currentScreen) { currentScreen = it }
    } else {
        MobileApp(currentScreen) { currentScreen = it }
    }
}

@Composable
fun MobileApp(currentScreen: Screen, onNavigate: (Screen) -> Unit) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(Screen.Home, Screen.Movies, Screen.TvSeries, Screen.Search, Screen.Settings).forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon(), contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentScreen == screen,
                        onClick = { onNavigate(screen) }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            ScreenContent(currentScreen)
        }
    }
}

@Composable
fun TvApp(currentScreen: Screen, onNavigate: (Screen) -> Unit) {
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail {
            NavigationRailItem(
                icon = { Icon(Screen.Home.icon(), contentDescription = "Home") },
                label = { Text("Home") },
                selected = currentScreen == Screen.Home,
                onClick = { onNavigate(Screen.Home) }
            )
            NavigationRailItem(
                icon = { Icon(Screen.Movies.icon(), contentDescription = "Movies") },
                label = { Text("Movies") },
                selected = currentScreen == Screen.Movies,
                onClick = { onNavigate(Screen.Movies) }
            )
            NavigationRailItem(
                icon = { Icon(Screen.TvSeries.icon(), contentDescription = "TV") },
                label = { Text("TV") },
                selected = currentScreen == Screen.TvSeries,
                onClick = { onNavigate(Screen.TvSeries) }
            )
            NavigationRailItem(
                icon = { Icon(Screen.Search.icon(), contentDescription = "Search") },
                label = { Text("Search") },
                selected = currentScreen == Screen.Search,
                onClick = { onNavigate(Screen.Search) }
            )
        }
        ScreenContent(currentScreen)
    }
}

@Composable
fun ScreenContent(screen: Screen) {
    when (screen) {
        Screen.Home -> HomeScreen()
        Screen.Movies -> CatalogScreen(catalogType = "movie")
        Screen.TvSeries -> CatalogScreen(catalogType = "tv")
        Screen.Search -> SearchScreen()
        Screen.Player -> PlayerScreen()
        Screen.Settings -> SettingsScreen()
    }
}

fun Screen.icon() = when (this) {
    Screen.Home -> Icons.Default.Home
    Screen.Movies -> Icons.Default.Movie
    Screen.TvSeries -> Icons.Default.Tv
    Screen.Search -> Icons.Default.Search
    Screen.Player -> Icons.Default.PlayArrow
    Screen.Settings -> Icons.Default.Settings
}
