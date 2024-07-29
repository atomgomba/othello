package com.ekezet.othello.feature.gamehistory

import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.diskCount
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.DefaultBoard
import com.ekezet.othello.core.game.data.GameSettings
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
            board = DefaultBoard,
            moveAt = Position(2, 3),
            disk = Disk.Dark,
            turn = 1,
        )
        val moveHistory = listOf(pastMove)
        val gameEnd = GameEnd.EndedTie
        val isGrayscaleMode = true

        val initModel = GameHistoryModel(
            moveHistory = moveHistory,
            gameEnd = gameEnd,
            gameSettings = GameSettings.Default.copy(
                displayOptions = BoardDisplayOptions.Default.copy(
                    isGrayscaleMode = isGrayscaleMode,
                ),
            ),
        )

        val result = subject.renderState(initModel)

        val expectedState = GameHistoryState(
            historyItems = persistentListOf(
                HistoryItem(
                    turn = pastMove.turn,
                    move = pastMove.moveAt,
                    disk = pastMove.disk,
                    board = pastMove.board.toImmutableList(),
                    darkCount = pastMove.board.diskCount.first,
                    lightCount = pastMove.board.diskCount.second,
                    image = null,
                ),
            ),
            gameEnd = gameEnd,
            lastState = moveHistory.toPastGameState(),
            isGrayscaleMode = isGrayscaleMode,
        )

        assertEquals(expectedState, result)
    }
}
