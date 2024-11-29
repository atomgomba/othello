package com.ekezet.othello.feature.settings.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.hurok.renderState
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.ui.components.PreviewBase
import com.ekezet.othello.feature.settings.SettingsLoop
import com.ekezet.othello.feature.settings.SettingsModel
import com.ekezet.othello.feature.settings.SettingsRenderer

private val defaultModel = SettingsModel()
private val showDarkStrategySelectorModel = defaultModel.showStrategySelectorFor(Disk.Dark)
private val showLightStrategySelectorModel = defaultModel.showStrategySelectorFor(Disk.Light)

private fun SettingsModel.toPreviewState() =
    renderState(::SettingsLoop, this, SettingsRenderer())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsModel.Preview() = toPreviewState()
    .SettingsViewImpl(selectStrategyFor = null)

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
