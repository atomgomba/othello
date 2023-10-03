package com.ekezet.othello.core.game.provider

import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.IGameSettings
import kotlinx.coroutines.flow.StateFlow

interface GameSettingsProvider {
    val settings: StateFlow<GameSettings>
    fun update(settings: IGameSettings)
}
