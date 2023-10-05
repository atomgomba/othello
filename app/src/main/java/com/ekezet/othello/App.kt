package com.ekezet.othello

import android.app.Application
import com.ekezet.othello.core.logging.AppLogging
import com.ekezet.othello.main.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        AppLogging.init(isDebug = BuildConfig.DEBUG)

        startKoin {
            androidContext(this@App)

            modules(mainModule())
        }
    }
}
