package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy

internal class GameSettingsLoop private constructor(
    args: GameSettings,
    dependency: GameSettingsDependency,
) : Loop<GameSettingsState, GameSettingsModel, GameSettings, GameSettingsDependency, GameSettingsAction>(
    args = args,
    dependency = dependency,
) {
    override fun initModel() = GameSettingsModel()

    override fun GameSettingsModel.applyArgs(args: GameSettings) =
        copy(
            displayOptions = args.displayOptions,
            lightStrategy = args.lightStrategy,
            darkStrategy = args.darkStrategy,
        )

    override fun renderState(model: GameSettingsModel) = with(model) {
        GameSettingsState(
            darkStrategy = darkStrategy,
            lightStrategy = lightStrategy,
            isDarkPreferSides = darkStrategy is PreferSidesDecoratorStrategy,
            isLightPreferSides = lightStrategy is PreferSidesDecoratorStrategy,
            selectingStrategyFor = selectingStrategyFor,
            onShowStrategiesClick = { disk -> emit(OnSelectStrategyClicked(disk)) },
            onDismissStrategies = { emit(OnSelectStrategyDismissed) },
            onPreferSidesClick = { disk, prefer -> emit(OnPreferSidesClicked(disk, prefer)) },
            onStrategySelect = { disk, strategy -> emit(OnStrategyItemClicked(disk, strategy)) },
        )
    }

    internal companion object Builder :
        LoopBuilder<GameSettingsState, GameSettingsModel, GameSettings, GameSettingsDependency, GameSettingsAction> {
        override fun invoke(
            args: GameSettings?,
            dependency: GameSettingsDependency?,
        ) = GameSettingsLoop(
            args = requireNotNull(args) { "Arguments must be set" },
            dependency = requireNotNull(dependency) { "Dependency must be set" },
        )
    }
}
