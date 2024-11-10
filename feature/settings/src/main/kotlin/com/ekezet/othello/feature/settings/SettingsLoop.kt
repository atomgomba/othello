package com.ekezet.othello.feature.settings

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.core.game.data.GameSettings

internal class SettingsLoop internal constructor(
    model: SettingsModel,
    renderer: SettingsRenderer,
    args: GameSettings? = null,
    dependency: SettingsDependency? = null,
) : Loop<SettingsState, SettingsModel, GameSettings, SettingsDependency, GameSettingsAction>(
    model = model,
    renderer = renderer,
    args = args,
    dependency = dependency,
) {
    override fun SettingsModel.applyArgs(args: GameSettings) =
        copy(
            boardDisplayOptions = args.boardDisplayOptions,
            lightStrategy = args.lightStrategy,
            darkStrategy = args.darkStrategy,
            confirmExit = args.confirmExit,
        )

    internal companion object Builder :
        LoopBuilder<SettingsState, SettingsModel, GameSettings, SettingsDependency, GameSettingsAction> {
        override fun build(args: GameSettings?) = SettingsLoop(
            model = SettingsModel(),
            renderer = SettingsRenderer(),
            args = requireNotNull(args) { "Arguments must be set" },
            dependency = SettingsDependency(),
        )
    }
}
