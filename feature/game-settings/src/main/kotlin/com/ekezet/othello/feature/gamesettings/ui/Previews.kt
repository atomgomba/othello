package com.ekezet.othello.feature.gamesettings.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.hurok.firstState
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.ui.components.PreviewBase
import com.ekezet.othello.feature.gamesettings.GameSettingsDependency
import com.ekezet.othello.feature.gamesettings.GameSettingsLoop
import com.ekezet.othello.feature.gamesettings.GameSettingsModel

internal val defaultModel = GameSettingsModel()

internal val showDarkStrategySelectorModel = defaultModel.showStrategySelectorFor(Disk.Dark)

internal val showLightStrategySelectorModel = defaultModel.showStrategySelectorFor(Disk.Light)

private fun GameSettingsModel.toPreviewState() = GameSettingsLoop(
    model = this,
    args = GameSettings(),
    dependency = GameSettingsDependency(),
).firstState

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
internal fun GameSettingsViewPreview_Default() {
    PreviewBase {
        defaultModel
            .toPreviewState()
            .GameSettingsViewImpl(selectStrategyFor = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
internal fun GameSettingsViewPreview_ShowDarkStrategySelector() {
    PreviewBase {
        showDarkStrategySelectorModel
            .toPreviewState()
            .GameSettingsViewImpl(selectStrategyFor = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
internal fun GameSettingsViewPreview_ShowLightStrategySelector() {
    PreviewBase {
        showLightStrategySelectorModel
            .toPreviewState()
            .GameSettingsViewImpl(selectStrategyFor = null)
    }
}
