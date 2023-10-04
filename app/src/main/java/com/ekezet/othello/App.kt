package com.ekezet.othello

import android.app.Application
import com.ekezet.othello.core.logging.AppLogging
import com.ekezet.othello.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level.DEBUG
import org.koin.core.logger.Level.WARNING

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        AppLogging.init(isDebug = BuildConfig.DEBUG)

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) DEBUG else WARNING)

            androidContext(this@App)

            modules(mainModule())
        }
    }
}
