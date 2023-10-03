package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.Effect
import com.ekezet.hurok.LoopScope
import com.ekezet.othello.core.game.data.IGameSettings

internal sealed interface GameSettingsEffect : Effect<GameSettingsModel, GameSettingsDependency>

internal data class PublishGameSettings(
    private val settings: IGameSettings,
) : GameSettingsEffect {
    override suspend fun LoopScope<GameSettingsModel, GameSettingsDependency>.trigger(
        dependency: GameSettingsDependency?,
    ) = dependency?.run {
        gameSettingsStore.update(settings)
    }
}
