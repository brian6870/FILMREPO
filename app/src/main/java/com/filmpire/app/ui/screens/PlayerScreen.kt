package com.filmpire.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PlayerScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Video surface
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF1A1A1A)
        ) {
            Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("ExoPlayer", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            }
        }
        
        // Controls overlay
        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.SkipPrevious, "Previous", tint = Color.White, modifier = Modifier.size(36.dp))
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.PlayArrow, "Play", tint = Color.White, modifier = Modifier.size(48.dp))
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.SkipNext, "Next", tint = Color.White, modifier = Modifier.size(36.dp))
            }
        }
    }
}
