package com.ekezet.othello.core.game.store

import com.ekezet.othello.core.game.data.AppSettings
import com.ekezet.othello.core.game.data.IAppSettings
import kotlinx.coroutines.flow.StateFlow

interface AppSettingsStore {
    val settings: StateFlow<AppSettings>

    suspend fun update(new: IAppSettings)
}
