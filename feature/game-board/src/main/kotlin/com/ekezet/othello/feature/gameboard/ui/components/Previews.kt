package com.ekezet.othello.feature.gameboard.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.othello.core.game.BoardFactory
import com.ekezet.othello.feature.gameboard.ui.viewModels.toList

@Preview
@Composable
internal fun GameBoardPreview() {
    val board = BoardFactory.starter()

    Box {
        GameBoard(board = board.toList())
    }
}
