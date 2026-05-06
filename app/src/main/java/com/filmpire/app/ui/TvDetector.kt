package com.filmpire.app.ui

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

object TvDetector {
    @Composable
    fun isTelevision(): Boolean {
        val context = LocalContext.current
        val pm = context.packageManager
        return pm.hasSystemFeature(PackageManager.FEATURE_LEANBACK) ||
               pm.hasSystemFeature(PackageManager.FEATURE_TELEVISION)
    }
}
