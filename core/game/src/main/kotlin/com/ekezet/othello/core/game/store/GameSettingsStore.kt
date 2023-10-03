package com.ekezet.othello.core.game.store

import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.IGameSettings
import kotlinx.coroutines.flow.StateFlow

interface GameSettingsStore {
    val settings: StateFlow<GameSettings>

    fun update(new: IGameSettings)
}
