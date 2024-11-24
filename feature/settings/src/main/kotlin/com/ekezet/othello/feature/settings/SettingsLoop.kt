package com.ekezet.othello.feature.settings

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder

internal class SettingsLoop internal constructor(
    model: SettingsModel,
    renderer: SettingsRenderer,
    args: SettingsArgs?,
    dependency: SettingsDependency? = null,
) : Loop<SettingsState, SettingsModel, SettingsArgs, SettingsDependency, GameSettingsAction>(
    model = model,
    renderer = renderer,
    args = args,
    dependency = dependency,
) {
    override fun SettingsModel.applyArgs(args: SettingsArgs) = with(args) {
        copy(
            boardDisplayOptions = gameSettings.boardDisplayOptions,
            historyDisplayOptions = historySettings.historyDisplayOptions,
            lightStrategy = gameSettings.lightStrategy,
            darkStrategy = gameSettings.darkStrategy,
            confirmExit = gameSettings.confirmExit,
        )
    }

    internal companion object Builder :
        LoopBuilder<SettingsState, SettingsModel, SettingsArgs, SettingsDependency, GameSettingsAction> {
        override fun build(args: SettingsArgs?) = SettingsLoop(
            model = SettingsModel(),
            renderer = SettingsRenderer(),
            args = requireNotNull(args) { "Arguments must be set" },
            dependency = SettingsDependency(),
        )
    }
}
