package com.ekezet.othello.core.game.store

import android.content.Context
import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.game.data.IHistorySettings
import com.ekezet.othello.core.game.data.from
import com.ekezet.othello.core.game.store.sharedprefs.loadHistorySettings
import com.ekezet.othello.core.game.store.sharedprefs.persist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SharedPreferencesHistorySettingsStore(
    context: Context,
) : HistorySettingsStore {
    private val prefs = context.getSharedPreferences("history-settings", Context.MODE_PRIVATE)

    private val _settings: MutableStateFlow<HistorySettings> = MutableStateFlow(prefs.loadHistorySettings())
    override val settings: StateFlow<HistorySettings>
        get() = _settings.asStateFlow()

    override suspend fun update(new: IHistorySettings) {
        val data = HistorySettings from new
        _settings.value = data
        prefs.persist(data)
    }
}
