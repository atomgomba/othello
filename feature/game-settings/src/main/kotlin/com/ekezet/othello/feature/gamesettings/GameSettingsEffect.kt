package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.Effect
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.effect.PublishGameSettings

internal sealed interface GameSettingsEffect : Effect<GameSettingsModel, GameSettingsDependency>

internal data class PublishGameSettings(
    override val settings: IGameSettings,
) : GameSettingsEffect, PublishGameSettings<GameSettingsModel, GameSettingsDependency>()
