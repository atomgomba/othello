package com.ekezet.othello.feature.settings

import com.ekezet.hurok.Effect
import com.ekezet.othello.core.game.data.AppSettings
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.game.data.IAppSettings
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.IHistorySettings
import com.ekezet.othello.core.game.effect.PublishAppSettings
import com.ekezet.othello.core.game.effect.PublishGameSettings
import com.ekezet.othello.core.game.effect.PublishHistorySettings

internal sealed interface GameSettingsEffect : Effect<SettingsModel, SettingsDependency>

internal data class PublishGameSettings(
    override val settings: IGameSettings,
) : GameSettingsEffect, PublishGameSettings<SettingsModel, SettingsDependency>()

internal data class PublishHistorySettings(
    override val settings: IHistorySettings,
) : GameSettingsEffect, PublishHistorySettings<SettingsModel, SettingsDependency>()

internal data class PublishAppSettings(
    override val settings: IAppSettings,
) : GameSettingsEffect, PublishAppSettings<SettingsModel, SettingsDependency>()

internal data object ResetAppSettings : GameSettingsEffect {
    override suspend fun SettingsEmitter.trigger(dependency: SettingsDependency?) = dependency?.run {
        gameSettingsStore.update(GameSettings.Default)
        historySettingsStore.update(HistorySettings.Default)
        appSettingsStore.update(AppSettings.Default)
    }
}
