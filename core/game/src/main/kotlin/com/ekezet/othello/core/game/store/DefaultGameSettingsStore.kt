package com.ekezet.othello.core.game.store

import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.IGameSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultGameSettingsStore(
    initialGameSettings: GameSettings = GameSettings(),
) : GameSettingsStore {
    private val _settings: MutableStateFlow<GameSettings> = MutableStateFlow(initialGameSettings)
    override val settings: StateFlow<GameSettings>
        get() = _settings.asStateFlow()

    override fun update(new: IGameSettings) {
        _settings.value = GameSettings.from(new)
    }
}
