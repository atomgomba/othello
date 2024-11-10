package com.ekezet.othello.core.game.store

import android.content.Context
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.from
import com.ekezet.othello.core.game.store.sharedprefs.loadGameSettings
import com.ekezet.othello.core.game.store.sharedprefs.persist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SharedPreferencesGameSettingsStore(
    context: Context,
) : GameSettingsStore {
    private val prefs = context.getSharedPreferences("game-settings", Context.MODE_PRIVATE)

    private val _settings: MutableStateFlow<GameSettings> = MutableStateFlow(prefs.loadGameSettings())
    override val settings: StateFlow<GameSettings>
        get() = _settings.asStateFlow()

    override suspend fun update(new: IGameSettings) {
        val data = GameSettings from new
        _settings.value = data
        prefs.persist(data)
    }
}
