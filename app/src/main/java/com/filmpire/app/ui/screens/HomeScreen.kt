package com.filmpire.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Continue Watching", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sampleContinueWatching) { item ->
                MediaCard(item.title, item.posterUrl, item.progress)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Trending Now", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sampleTrending) { item ->
                MediaCard(item.title, item.posterUrl)
            }
        }
    }
}

@Composable
fun MediaCard(title: String, posterUrl: String, progress: Float? = null) {
    Card(modifier = Modifier.width(140.dp).height(210.dp)) {
        Box {
            // Poster placeholder
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                    Text(title.take(1), style = MaterialTheme.typography.headlineLarge)
                }
            }
            if (progress != null) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                )
            }
        }
    }
    Spacer(modifier = Modifier.width(4.dp))
    Text(title, style = MaterialTheme.typography.bodySmall, maxLines = 2)
}

data class MediaItem(val title: String, val posterUrl: String, val progress: Float? = null)

val sampleContinueWatching = listOf(
    MediaItem("The Boys", "poster1", 0.6f),
    MediaItem("Stranger Things", "poster2", 0.3f),
    MediaItem("Breaking Bad", "poster3", 0.8f),
)

val sampleTrending = listOf(
    MediaItem("Dune Part Two", "poster4"),
    MediaItem("Fallout", "poster5"),
    MediaItem("The Matrix", "poster6"),
    MediaItem("Inception", "poster7"),
    MediaItem("Interstellar", "poster8"),
)
