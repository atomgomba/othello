package com.ekezet.othello

import android.app.Application
import com.ekezet.othello.core.logging.AppLogging

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        AppLogging.init(isDebug = BuildConfig.DEBUG)
    }
}
