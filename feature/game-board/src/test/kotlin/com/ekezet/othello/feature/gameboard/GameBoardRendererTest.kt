package com.ekezet.othello.feature.gameboard

import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameEnd.EndedWin
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.ui.viewModels.toImmutableList
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.NextMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.ValidMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.newEmptyOverlay
import com.ekezet.othello.feature.gameboard.ui.viewModels.putAt
import com.ekezet.othello.feature.gameboard.ui.viewModels.toImmutableList
import org.junit.Test
import kotlin.test.assertEquals

internal class GameBoardRendererTest {
    private val subject = GameBoardRenderer()

    @Test
    fun `renderToState works correctly`() {
        val initModel = GameBoardModel()

        val result = subject.renderState(initModel)

        val expectedOverlay = initModel.gameState.currentBoard.newEmptyOverlay().apply {
            putAt(3, 2, ValidMoveIndicatorOverlayItem(disk = initModel.gameState.currentDisk))
            putAt(4, 5, ValidMoveIndicatorOverlayItem(disk = initModel.gameState.currentDisk))
            putAt(2, 3, ValidMoveIndicatorOverlayItem(disk = initModel.gameState.currentDisk))
            putAt(5, 4, ValidMoveIndicatorOverlayItem(disk = initModel.gameState.currentDisk))
        }
        val expectedState = with(initModel) {
            GameBoardState(
                board = gameState.currentBoard.toImmutableList(),
                overlay = expectedOverlay.toImmutableList(),
                currentDisk = gameState.currentDisk,
                darkStrategyName = darkStrategy?.name,
                lightStrategyName = lightStrategy?.name,
                diskCount = gameState.diskCount,
                opponentName = lightStrategy?.name,
                currentTurn = gameState.turn + 1,
                nextMovePosition = nextMovePosition,
                displayOptions = displayOptions,
                ended = ended,
                passed = passed,
                celebrate = false,
                isHumanPlayer = gameState.currentDisk.isHumanPlayer,
            )
        }

        assertEquals(expectedState, result)
    }

    @Test
    fun `renderToState when showPossibleMoves is false`() {
        val initModel = GameBoardModel(
            displayOptions = BoardDisplayOptions.Default.copy(
                showPossibleMoves = false,
            ),
        )

        val result = subject.renderState(initModel)

        val expectedState = with(initModel) {
            GameBoardState(
                board = gameState.currentBoard.toImmutableList(),
                overlay = gameState.currentBoard.newEmptyOverlay().toImmutableList(),
                currentDisk = gameState.currentDisk,
                darkStrategyName = darkStrategy?.name,
                lightStrategyName = lightStrategy?.name,
                diskCount = gameState.diskCount,
                opponentName = lightStrategy?.name,
                currentTurn = gameState.turn + 1,
                nextMovePosition = nextMovePosition,
                displayOptions = displayOptions,
                ended = ended,
                passed = passed,
                celebrate = false,
                isHumanPlayer = gameState.currentDisk.isHumanPlayer,
            )
        }

        assertEquals(expectedState, result)
    }

    @Test
    fun `renderToState when nextMovePosition is set`() {
        val initModel = GameBoardModel(
            displayOptions = BoardDisplayOptions.Default.copy(
                showPossibleMoves = false,
            ),
            nextMovePosition = Position(2, 3),
        )

        val result = subject.renderState(initModel)

        val expectedOverlay = initModel.gameState.currentBoard.newEmptyOverlay().apply {
            putAt(2, 3, NextMoveIndicatorOverlayItem(disk = initModel.gameState.currentDisk))
        }
        val expectedState = with(initModel) {
            GameBoardState(
                board = gameState.currentBoard.toImmutableList(),
                overlay = expectedOverlay.toImmutableList(),
                currentDisk = gameState.currentDisk,
                darkStrategyName = darkStrategy?.name,
                lightStrategyName = lightStrategy?.name,
                diskCount = gameState.diskCount,
                opponentName = lightStrategy?.name,
                currentTurn = gameState.turn + 1,
                nextMovePosition = nextMovePosition,
                displayOptions = displayOptions,
                ended = ended,
                passed = passed,
                celebrate = false,
                isHumanPlayer = gameState.currentDisk.isHumanPlayer,
            )
        }

        assertEquals(expectedState, result)
    }

    @Test
    fun `renderToState when ended is set and human wins`() {
        val winner = Disk.Dark
        val initModel = GameBoardModel(
            displayOptions = BoardDisplayOptions.Default.copy(
                showPossibleMoves = false,
            ),
            ended = EndedWin(winner = winner),
        )

        val result = subject.renderState(initModel)

        val expectedState = with(initModel) {
            GameBoardState(
                board = gameState.currentBoard.toImmutableList(),
                overlay = gameState.currentBoard.newEmptyOverlay().toImmutableList(),
                currentDisk = gameState.currentDisk,
                darkStrategyName = darkStrategy?.name,
                lightStrategyName = lightStrategy?.name,
                diskCount = gameState.diskCount,
                opponentName = lightStrategy?.name,
                currentTurn = gameState.turn + 1,
                nextMovePosition = nextMovePosition,
                displayOptions = displayOptions,
                ended = ended,
                passed = passed,
                celebrate = true,
                isHumanPlayer = gameState.currentDisk.isHumanPlayer,
            )
        }

        assertEquals(expectedState, result)
    }
}
