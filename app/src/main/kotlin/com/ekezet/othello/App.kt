package com.ekezet.othello

import android.app.Application
import androidx.compose.runtime.Composer
import androidx.compose.runtime.ExperimentalComposeRuntimeApi
import androidx.compose.runtime.tooling.ComposeStackTraceMode
import com.ekezet.othello.core.data.ExcludeFromCoverage
import com.ekezet.othello.core.logging.AppLogging
import com.ekezet.othello.main.di.MainGraph
import dev.zacsweers.metro.createGraphFactory

@ExcludeFromCoverage
class App : Application() {
    val mainGraph: MainGraph by lazy {
        createGraphFactory<MainGraph.Factory>().create(applicationContext)
    }

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
    }
}
