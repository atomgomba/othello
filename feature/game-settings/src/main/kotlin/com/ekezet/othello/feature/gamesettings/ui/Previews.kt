package com.ekezet.othello.feature.gamesettings.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.othello.core.game.data.defaultDarkStrategy
import com.ekezet.othello.core.game.data.defaultDisplayOptions
import com.ekezet.othello.core.game.data.defaultLightStrategy
import com.ekezet.othello.core.ui.components.PreviewBase
import com.ekezet.othello.feature.gamesettings.GameSettingsState

private val defaultState = GameSettingsState(
    isLightPreferSides = true,
    isDarkPreferSides = false,
    displayOptions = defaultDisplayOptions,
    lightStrategy = defaultLightStrategy,
    darkStrategy = defaultDarkStrategy,
    selectingStrategyFor = null,
    onShowStrategiesClick = {},
    onDismissStrategies = {},
    onPreferSidesClick = { _, _ -> },
    onStrategySelect = { _, _ -> },
    onShowPossibleMovesClick = {},
    onShowBoardPositionsClick = {},
    onGrayscaleModeClick = {},
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun GameSettingsViewPreviewDefault() {
    PreviewBase {
        defaultState.GameSettingsViewImpl(pickStrategyFor = null)
    }
}
