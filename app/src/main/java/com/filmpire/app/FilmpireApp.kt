package com.filmpire.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FilmpireApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Pre-load bundled plugins
        PluginLoader.init(this)
    }
}
