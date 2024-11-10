package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.Renderer
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy

internal class GameSettingsRenderer : Renderer<GameSettingsModel, GameGameSettingsDependency, GameSettingsState> {
    override fun renderState(model: GameSettingsModel) = with(model) {
        GameSettingsState(
            darkStrategy = darkStrategy,
            lightStrategy = lightStrategy,
            isDarkPreferSides = darkStrategy is PreferSidesDecoratorStrategy,
            isLightPreferSides = lightStrategy is PreferSidesDecoratorStrategy,
            boardDisplayOptions = boardDisplayOptions,
            selectingStrategyFor = selectingStrategyFor,
            confirmExit = confirmExit,
        )
    }
}
