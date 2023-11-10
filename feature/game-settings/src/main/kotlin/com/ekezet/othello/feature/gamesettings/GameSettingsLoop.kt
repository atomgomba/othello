package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy

internal class GameSettingsLoop internal constructor(
    model: GameSettingsModel,
    args: GameSettings,
    dependency: GameSettingsDependency,
) : Loop<GameSettingsState, GameSettingsModel, GameSettings, GameSettingsDependency, GameSettingsAction>(
    model = model,
    args = args,
    dependency = dependency,
) {
    private val actions = GameSettingsStateActions(
        onStrategySelectorClick = { disk -> emit(OnStrategySelectorClicked(disk)) },
        onStrategySelectorDismiss = { emit(OnStrategySelectorDismissed) },
        onPreferSidesToggle = { disk, prefer -> emit(OnPreferSidesToggled(disk, prefer)) },
        onStrategySelect = { disk, strategy -> emit(OnStrategySelected(disk, strategy)) },
        onShowPossibleMovesClick = { emit(OnShowPossibleMovesClicked) },
        onShowBoardPositionsClick = { emit(OnShowBoardPositionsClicked) },
        onGrayscaleModeClick = { emit(OnGrayscaleModeClicked) },
    )

    override fun GameSettingsModel.applyArgs(args: GameSettings) =
        copy(
            displayOptions = args.displayOptions,
            lightStrategy = args.lightStrategy,
            darkStrategy = args.darkStrategy,
        )

    override fun renderState(model: GameSettingsModel) = with(model) {
        GameSettingsState(
            actions = actions,
            darkStrategy = darkStrategy,
            lightStrategy = lightStrategy,
            isDarkPreferSides = darkStrategy is PreferSidesDecoratorStrategy,
            isLightPreferSides = lightStrategy is PreferSidesDecoratorStrategy,
            displayOptions = displayOptions,
            selectingStrategyFor = selectingStrategyFor,
        )
    }

    internal companion object Builder :
        LoopBuilder<GameSettingsState, GameSettingsModel, GameSettings, GameSettingsDependency, GameSettingsAction> {
        override fun build(args: GameSettings?) = GameSettingsLoop(
            model = GameSettingsModel(),
            args = requireNotNull(args) { "Arguments must be set" },
            dependency = GameSettingsDependency(),
        )
    }
}
