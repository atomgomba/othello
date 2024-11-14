package com.ekezet.othello.feature.gamehistory.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.putAtAndClone
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.DefaultBoard
import com.ekezet.othello.core.game.state.OthelloGameState
import com.ekezet.othello.core.ui.components.PreviewBase
import com.ekezet.othello.core.ui.render.renderToBitmap
import com.ekezet.othello.core.ui.viewModels.toImmutableList
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem

@Composable
@Preview
private fun HistoryItemViewMovePreview() {
    PreviewBase {
        HistoryItemView(item = moveHistoryMoveItem)
    }
}

@Composable
@Preview
private fun HistoryItemViewPassPreview() {
    PreviewBase {
        HistoryItemView(item = moveHistoryPassItem)
    }
}

@Composable
@Preview
private fun GameEndItemViewTiePreview() {
    PreviewBase {
        GameEndItemView(GameEnd.EndedTie)
    }
}

@Composable
@Preview
private fun GameEndItemViewWinPreview() {
    PreviewBase {
        GameEndItemView(gameEndedWinItem)
    }
}

private val lastMoveImage = PastMove(
    board = DefaultBoard.putAtAndClone(Position(4, 5), Disk.Dark),
    disk = Disk.Dark,
    turn = 1,
    moveAt = Position(4, 5),
).renderToBitmap()

private val lastPassImage = PastMove(
    board = DefaultBoard.putAtAndClone(Position(4, 5), Disk.Dark),
    disk = Disk.Dark,
    turn = 1,
    moveAt = null,
).renderToBitmap()

private val moveHistoryMoveItem = HistoryItem(
    turn = 42,
    move = Position(4, 5),
    disk = Disk.Dark,
    board = DefaultBoard.toImmutableList(),
    image = lastMoveImage,
    lightCount = 8,
    darkCount = 2,
)

private val moveHistoryPassItem = HistoryItem(
    turn = 42,
    move = null,
    disk = Disk.Light,
    board = DefaultBoard.toImmutableList(),
    image = lastPassImage,
    lightCount = 8,
    darkCount = 2,
)

private val lastState = OthelloGameState.Default

private val gameEndedWinItem = GameEnd.EndedWin(winner = Disk.Dark)
