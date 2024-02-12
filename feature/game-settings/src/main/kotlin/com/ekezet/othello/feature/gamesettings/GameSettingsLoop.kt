package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.core.game.data.GameSettings

internal class GameSettingsLoop internal constructor(
    model: GameSettingsModel,
    renderer: GameSettingsRenderer,
    args: GameSettings? = null,
    dependency: GameSettingsDependency? = null,
) : Loop<GameSettingsState, GameSettingsModel, GameSettings, GameSettingsDependency, GameSettingsAction>(
    model = model,
    renderer = renderer,
    args = args,
    dependency = dependency,
) {
    override fun GameSettingsModel.applyArgs(args: GameSettings) =
        copy(
            displayOptions = args.displayOptions,
            lightStrategy = args.lightStrategy,
            darkStrategy = args.darkStrategy,
        )

    internal companion object Builder :
        LoopBuilder<GameSettingsState, GameSettingsModel, GameSettings, GameSettingsDependency, GameSettingsAction> {
        override fun build(args: GameSettings?) = GameSettingsLoop(
            model = GameSettingsModel(),
            renderer = GameSettingsRenderer(),
            args = requireNotNull(args) { "Arguments must be set" },
            dependency = GameSettingsDependency(),
        )
    }
}
