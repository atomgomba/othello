package com.ekezet.othello.main.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.hurok.renderState
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.ui.components.PreviewBase
import com.ekezet.othello.main.MainLoop
import com.ekezet.othello.main.MainModel
import com.ekezet.othello.main.di.mainModule
import com.ekezet.othello.main.navigation.MainDestinations

private fun MainModel.toPreviewState() =
    renderState(::MainLoop, this)

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
internal fun MainViewPreview_GameBoard_Default() {
    PreviewBase {
        MainModel()
            .toPreviewState()
            .MainViewImpl(gameSettings = GameSettings())
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
internal fun MainViewPreview_GameSettings_Default() {
    PreviewBase(moduleList = listOf(mainModule())) {
        MainModel()
            .toPreviewState()
            .MainViewImpl(
                gameSettings = GameSettings(),
                startDestination = MainDestinations.GameSettingsDestination.id,
            )
    }
}
