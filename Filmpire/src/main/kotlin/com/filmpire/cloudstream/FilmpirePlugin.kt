package com.filmpire.cloudstream

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class FilmpirePlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(FilmpireProvider())
    }
}
