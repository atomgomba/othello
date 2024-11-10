package com.ekezet.othello.core.game.store

import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.game.data.IHistorySettings
import kotlinx.coroutines.flow.StateFlow

interface HistorySettingsStore {
    val settings: StateFlow<HistorySettings>

    suspend fun update(new: IHistorySettings)
}
