package com.ekezet.othello

import android.app.Application
import androidx.compose.runtime.Composer
import androidx.compose.runtime.ExperimentalComposeRuntimeApi
import androidx.compose.runtime.tooling.ComposeStackTraceMode
import com.ekezet.othello.core.data.ExcludeFromCoverage
import com.ekezet.othello.core.logging.AppLogging
import com.ekezet.othello.main.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@ExcludeFromCoverage
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        @OptIn(ExperimentalComposeRuntimeApi::class)
        Composer.setDiagnosticStackTraceMode(
            if (BuildConfig.DEBUG) {
                ComposeStackTraceMode.SourceInformation
            } else {
                ComposeStackTraceMode.None
            }
        )

        AppLogging.init(isDebug = BuildConfig.DEBUG)

        startKoin {
            androidContext(baseContext)

            modules(mainModule())
        }
    }
}
