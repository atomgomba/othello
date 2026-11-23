package com.ekezet.othello.feature.settings.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.othello.core.ui.components.PreviewBase
import com.ekezet.othello.feature.settings.SettingsModel
import com.ekezet.othello.feature.settings.SettingsRenderer

private val defaultModel = SettingsModel()

private fun SettingsModel.toPreviewState() =
    SettingsRenderer().renderState(this)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsModel.Preview() = toPreviewState()
    .SettingsViewImpl(emit = {})

@Preview
@Composable
internal fun GameSettingsViewPreview_Default() {
    PreviewBase {
        defaultModel.Preview()
    }
}
