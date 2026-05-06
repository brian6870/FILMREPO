package com.filmpire.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Extensions", style = MaterialTheme.typography.titleMedium)
        Text("Filmpire Extension v1.0.0 - Installed", style = MaterialTheme.typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("About", style = MaterialTheme.typography.titleMedium)
        Text("Filmpire - Watch free movies and TV shows", style = MaterialTheme.typography.bodyMedium)
        Text("Powered by TMDB & AllMovieLand", style = MaterialTheme.typography.bodySmall)
    }
}
