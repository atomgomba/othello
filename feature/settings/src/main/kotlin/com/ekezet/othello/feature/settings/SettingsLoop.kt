package com.ekezet.othello.feature.settings

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder

internal class SettingsLoop internal constructor(
    model: SettingsModel,
    renderer: SettingsRenderer,
    args: SettingsArgs?,
    argsApplyer: SettingsArgsApplyer?,
    dependency: SettingsDependency? = null,
) : Loop<SettingsState, SettingsModel, SettingsArgs, SettingsDependency, SettingsAction>(
    model = model,
    renderer = renderer,
    args = args,
    argsApplyer = argsApplyer,
    dependency = dependency,
) {
    internal companion object Builder :
        LoopBuilder<SettingsState, SettingsModel, SettingsArgs, SettingsDependency, SettingsAction> {
        private val argsApplyer: SettingsArgsApplyer
            get() = SettingsArgsApplyer { args ->
                with(args) {
                    copy(
                        boardDisplayOptions = gameSettings.boardDisplayOptions,
                        historyDisplayOptions = historySettings.historyDisplayOptions,
                        lightStrategy = gameSettings.lightStrategy,
                        darkStrategy = gameSettings.darkStrategy,
                        confirmExit = appSettings.confirmExit,
                    )
                }
            }

        override fun build(args: SettingsArgs?) = SettingsLoop(
            model = SettingsModel(),
            renderer = SettingsRenderer(),
            args = requireNotNull(args) { "Arguments must be set" },
            argsApplyer = argsApplyer,
            dependency = SettingsDependency(),
        )
    }
}
