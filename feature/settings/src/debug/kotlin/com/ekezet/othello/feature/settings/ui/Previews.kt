package com.ekezet.othello.feature.settings.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.hurok.renderState
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.ui.components.PreviewBase
import com.ekezet.othello.feature.settings.SettingsLoop
import com.ekezet.othello.feature.settings.SettingsModel
import com.ekezet.othello.feature.settings.SettingsRenderer

@VisibleForTesting
internal val defaultModel = SettingsModel()

@VisibleForTesting
internal val showDarkStrategySelectorModel = defaultModel.showStrategySelectorFor(Disk.Dark)

@VisibleForTesting
internal val showLightStrategySelectorModel = defaultModel.showStrategySelectorFor(Disk.Light)

private fun SettingsModel.toPreviewState() =
    renderState(::SettingsLoop, this, SettingsRenderer())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsModel.Preview() = toPreviewState()
    .GameSettingsViewImpl(selectStrategyFor = null)

@Preview
@Composable
internal fun GameSettingsViewPreview_Default() {
    PreviewBase {
        defaultModel.Preview()
    }
}

@Preview
@Composable
internal fun GameSettingsViewPreview_ShowDarkStrategySelector() {
    PreviewBase {
        showDarkStrategySelectorModel.Preview()
    }
}

@Preview
@Composable
internal fun GameSettingsViewPreview_ShowLightStrategySelector() {
    PreviewBase {
        showLightStrategySelectorModel.Preview()
    }
}
