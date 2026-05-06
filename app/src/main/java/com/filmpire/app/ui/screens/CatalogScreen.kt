package com.filmpire.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CatalogScreen(catalogType: String) {
    val title = if (catalogType == "movie") "Movies" else "TV Series"
    val items = if (catalogType == "movie") sampleMovies else sampleTvShows

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                MediaCard(item.title, item.posterUrl)
            }
        }
    }
}

val sampleMovies = listOf(
    MediaItem("Dune Part Two", "m1"),
    MediaItem("The Matrix", "m2"),
    MediaItem("Inception", "m3"),
    MediaItem("Interstellar", "m4"),
    MediaItem("The Dark Knight", "m5"),
    MediaItem("Pulp Fiction", "m6"),
    MediaItem("Fight Club", "m7"),
    MediaItem("Forrest Gump", "m8"),
)

val sampleTvShows = listOf(
    MediaItem("The Boys", "t1"),
    MediaItem("Stranger Things", "t2"),
    MediaItem("Breaking Bad", "t3"),
    MediaItem("Game of Thrones", "t4"),
    MediaItem("The Office", "t5"),
    MediaItem("Friends", "t6"),
    MediaItem("The Mandalorian", "t7"),
    MediaItem("Dark", "t8"),
)
