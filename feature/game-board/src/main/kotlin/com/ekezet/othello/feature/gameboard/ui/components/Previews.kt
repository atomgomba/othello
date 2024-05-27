package com.ekezet.othello.feature.gameboard.ui.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.othello.core.game.BoardFactory
import com.ekezet.othello.core.ui.components.PreviewBase
import com.ekezet.othello.core.ui.viewModels.toImmutableList

@ExperimentalLayoutApi
@Preview
@Composable
private fun GameBoardPreviewDefault() {
    PreviewBase {
        GameBoard(
            board = BoardFactory.starter().toImmutableList(),
        )
    }
}

@ExperimentalLayoutApi
@Preview
@Composable
private fun GameBoardPreviewShowPositions() {
    PreviewBase {
        GameBoard(
            board = BoardFactory.starter().toImmutableList(),
            showPositions = true,
        )
    }
}
