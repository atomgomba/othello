package com.ekezet.othello.feature.settings

import com.ekezet.hurok.Effect
import com.ekezet.othello.core.data.models.Disk
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
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.settings.feature.strategyPicker.showStrategyPickerFor

internal sealed interface SettingsEffect : Effect<SettingsModel, SettingsDependency>

internal data class PublishSettings(
    override val settings: IGameSettings,
) : SettingsEffect, PublishGameSettings<SettingsModel, SettingsDependency>()

internal data class PublishHistorySettings(
    override val settings: IHistorySettings,
) : SettingsEffect, PublishHistorySettings<SettingsModel, SettingsDependency>()

internal data class PublishAppSettings(
    override val settings: IAppSettings,
) : SettingsEffect, PublishAppSettings<SettingsModel, SettingsDependency>()

internal data object ResetAppSettings : SettingsEffect {
    override suspend fun SettingsEmitter.trigger(dependency: SettingsDependency?) = dependency?.run {
        gameSettingsStore.update(GameSettings.Default)
        historySettingsStore.update(HistorySettings.Default)
        appSettingsStore.update(AppSettings.Default)
    }
}

internal data class ShowStrategyPicker(val player: Disk, val current: Strategy?) : SettingsEffect {
    override suspend fun SettingsEmitter.trigger(dependency: SettingsDependency?) = dependency?.run {
        strategyPickerLoop.emit(showStrategyPickerFor(player = player, default = current))
    }
}
