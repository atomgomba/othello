package com.ekezet.othello.main.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.hurok.renderState
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.ui.components.PreviewBase
import com.ekezet.othello.main.MainLoop
import com.ekezet.othello.main.MainModel

private fun MainModel.toPreviewState() =
    renderState(::MainLoop, this)

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
internal fun MainViewPreview_Default() {
    PreviewBase {
        MainModel()
            .toPreviewState()
            .MainViewImpl(GameSettings())
    }
}
