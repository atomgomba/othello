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

internal val defaultModel = SettingsModel()

internal val showDarkStrategySelectorModel = defaultModel.showStrategySelectorFor(Disk.Dark)

internal val showLightStrategySelectorModel = defaultModel.showStrategySelectorFor(Disk.Light)

private fun SettingsModel.toPreviewState() =
    renderState(::SettingsLoop, this, SettingsRenderer())

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
