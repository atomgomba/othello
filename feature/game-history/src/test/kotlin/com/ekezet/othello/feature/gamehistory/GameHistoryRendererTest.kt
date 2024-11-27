package com.ekezet.othello.feature.gamehistory

import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.diskCount
import com.ekezet.othello.core.data.models.numDark
import com.ekezet.othello.core.data.models.numLight
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.HistoryDisplayOptions
import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.game.data.StartBoard
import com.ekezet.othello.core.game.toPastGameState
import com.ekezet.othello.core.ui.viewModels.toImmutableList
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem
import kotlinx.collections.immutable.persistentListOf
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GameHistoryRendererTest {
    private val subject = GameHistoryRenderer()

    @Test
    fun `renderState works correctly`() {
        val pastMove = PastMove(
            board = StartBoard,
            moveAt = Position(2, 3),
            disk = Disk.Dark,
            turn = 1,
        )
        val gameEnd = GameEnd.EndedTie
        val isGrayscaleMode = true
        val alwaysScrollToBottom = false
        val moveHistory: MoveHistory = listOf(pastMove)

        val initModel = GameHistoryModel(
            moveHistory = moveHistory,
            gameEnd = gameEnd,
            gameSettings = GameSettings.Default.copy(
                boardDisplayOptions = BoardDisplayOptions.Default.copy(
                    isGrayscaleMode = isGrayscaleMode,
                ),
            ),
            historySettings = HistorySettings.Default.copy(
                historyDisplayOptions = HistoryDisplayOptions.Default.copy(
                    alwaysScrollToBottom = alwaysScrollToBottom,
                )
            )
        )

        val result = subject.renderState(initModel)

        val expectedState = GameHistoryState(
            historyItems = persistentListOf(
                HistoryItem(
                    turn = pastMove.turn,
                    move = pastMove.moveAt,
                    disk = pastMove.disk,
                    board = pastMove.board.toImmutableList(),
                    darkCount = pastMove.board.diskCount.numDark,
                    lightCount = pastMove.board.diskCount.numLight,
                    image = null,
                ),
            ),
            gameEnd = gameEnd,
            lastState = moveHistory.toPastGameState(),
            isGrayscaleMode = isGrayscaleMode,
            alwaysScrollToBottom = alwaysScrollToBottom,
        )

        assertEquals(expectedState, result)
    }
}
