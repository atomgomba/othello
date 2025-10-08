package com.ekezet.othello.feature.settings

import com.ekezet.hurok.Renderer
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy

internal class SettingsRenderer : Renderer<SettingsState, SettingsModel> {
    override fun renderState(model: SettingsModel) = with(model) {
        SettingsState(
            darkStrategy = darkStrategy,
            lightStrategy = lightStrategy,
            isDarkPreferSides = darkStrategy is PreferSidesDecoratorStrategy,
            isLightPreferSides = lightStrategy is PreferSidesDecoratorStrategy,
            boardDisplayOptions = boardDisplayOptions,
            selectingStrategyFor = selectingStrategyFor,
            historyDisplayOptions = historyDisplayOptions,
            confirmExit = confirmExit,
            showConfirmResetSettingsDialog = showConfirmResetSettingsDialog,
        )
    }
}
