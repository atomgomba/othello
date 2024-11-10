package com.ekezet.othello.feature.settings

import com.ekezet.hurok.Effect
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.IHistorySettings
import com.ekezet.othello.core.game.effect.PublishGameSettings
import com.ekezet.othello.core.game.effect.PublishHistorySettings

internal sealed interface GameSettingsEffect : Effect<SettingsModel, SettingsDependency>

internal data class PublishGameSettings(
    override val settings: IGameSettings,
) : GameSettingsEffect, PublishGameSettings<SettingsModel, SettingsDependency>()

internal data class PublishHistorySettings(
    override val settings: IHistorySettings,
) : GameSettingsEffect, PublishHistorySettings<SettingsModel, SettingsDependency>()