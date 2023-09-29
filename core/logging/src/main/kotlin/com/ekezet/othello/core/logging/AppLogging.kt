package com.ekezet.othello.core.logging

import timber.log.Timber
import timber.log.Timber.DebugTree

object AppLogging {
    fun init(isDebug: Boolean) {
        Timber.plant(if (isDebug) DebugTree() else ReleaseTree())
    }
}
