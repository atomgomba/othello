package com.ekezet.othello.feature.gamehistory.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.data.defaultBoard
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.ui.components.PreviewBase
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
        GameEndItemView(GameEnd.EndedTie, lastState)
    }
}

@Composable
@Preview
private fun GameEndItemViewWinPreview() {
    PreviewBase {
        GameEndItemView(gameEndedWinItem, lastState)
    }
}

private val moveHistoryMoveItem = HistoryItem(
    turn = 42,
    move = Position(4, 5),
    disk = Disk.Dark,
    board = defaultBoard.toImmutableList(),
)

private val moveHistoryPassItem = HistoryItem(
    turn = 42,
    move = null,
    disk = Disk.Light,
    board = defaultBoard.toImmutableList(),
)

private val lastState = CurrentGameState.new()

private val gameEndedWinItem = GameEnd.EndedWin(winner = Disk.Dark)
