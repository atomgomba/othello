package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.core.game.data.GameSettings

internal class GameSettingsLoop private constructor(
    args: GameSettings,
    dependency: GameSettingsDependency?,
) : Loop<GameSettingsState, GameSettingsModel, GameSettings, GameSettingsDependency, GameSettingsAction>(
    args,
    dependency,
) {
    override fun initModel() = GameSettingsModel()

    override fun GameSettingsModel.applyArgs(args: GameSettings) =
        copy(
            displayOptions = args.displayOptions,
            lightStrategy = args.lightStrategy,
            darkStrategy = args.darkStrategy,
        )

    override fun renderState(model: GameSettingsModel) =
        GameSettingsState

    internal companion object Builder :
        LoopBuilder<GameSettingsState, GameSettingsModel, GameSettings, GameSettingsDependency, GameSettingsAction> {
        override fun invoke(
            args: GameSettings?,
            dependency: GameSettingsDependency?,
        ) = GameSettingsLoop(
            args = requireNotNull(args) { "GameSettingsLoop arguments must be set" },
            dependency = dependency,
        )
    }
}
