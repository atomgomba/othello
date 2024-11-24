package com.ekezet.othello.core.game.store

import android.content.Context
import com.ekezet.othello.core.game.data.AppSettings
import com.ekezet.othello.core.game.data.IAppSettings
import com.ekezet.othello.core.game.data.from
import com.ekezet.othello.core.game.store.sharedprefs.loadAppSettings
import com.ekezet.othello.core.game.store.sharedprefs.persist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SharedPreferencesAppSettingsStore(
    context: Context,
) : AppSettingsStore {
    private val prefs = context.getSharedPreferences("app-settings", Context.MODE_PRIVATE)

    private val _settings: MutableStateFlow<AppSettings> = MutableStateFlow(prefs.loadAppSettings())
    override val settings: StateFlow<AppSettings>
        get() = _settings.asStateFlow()

    override suspend fun update(new: IAppSettings) {
        val data = AppSettings from new
        _settings.value = data
        prefs.persist(data)
    }
}
