package com.filmpire.app.plugin

import android.content.Context
import java.io.File

object PluginLoader {
    fun init(context: Context) {
        val pluginDir = File(context.filesDir, "plugins")
        pluginDir.mkdirs()
        
        val bundledPlugin = File(pluginDir, "Filmpire.cs3")
        if (!bundledPlugin.exists()) {
            try {
                context.assets.open("Filmpire.cs3").use { input ->
                    bundledPlugin.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (_: Exception) {}
        }
    }
}
