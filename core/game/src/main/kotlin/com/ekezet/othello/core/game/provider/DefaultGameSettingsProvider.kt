package com.ekezet.othello.core.game.provider

import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.IGameSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultGameSettingsProvider : GameSettingsProvider {
    private val _settings = MutableStateFlow(GameSettings())
    override val settings = _settings.asStateFlow()

    override fun update(settings: IGameSettings) = with(settings) {
        _settings.value = _settings.value.copy(
            displayOptions = displayOptions,
            darkStrategy = darkStrategy,
            lightStrategy = lightStrategy,
        )
    }
}
