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
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GameBoardRendererTest {
    private val subject = GameBoardRenderer()

    @Test
    fun `renderToState works correctly`() {
        val initModel = GameBoardModel()

        val result = subject.renderState(initModel)

        val expectedOverlay = initModel.currentGameState.board.newEmptyOverlay().apply {
            putAt(3, 2, ValidMoveIndicatorOverlayItem(disk = initModel.currentGameState.currentDisk))
            putAt(4, 5, ValidMoveIndicatorOverlayItem(disk = initModel.currentGameState.currentDisk))
            putAt(2, 3, ValidMoveIndicatorOverlayItem(disk = initModel.currentGameState.currentDisk))
            putAt(5, 4, ValidMoveIndicatorOverlayItem(disk = initModel.currentGameState.currentDisk))
        }
        val expectedState = with(initModel) {
            GameBoardState(
                board = currentGameState.board.toImmutableList(),
                overlay = expectedOverlay.toImmutableList(),
                currentDisk = currentGameState.currentDisk,
                darkStrategyName = darkStrategy?.name,
                lightStrategyName = lightStrategy?.name,
                diskCount = currentGameState.diskCount,
                opponentName = lightStrategy?.name,
                displayedTurn = currentGameState.turn + 1,
                displayedMaxTurnCount = turnCount + 1,
                hasNextTurn = false,
                isCurrentTurn = true,
                nextMovePosition = nextMovePosition,
                displayOptions = boardDisplayOptions,
                ended = ended,
                passed = passed,
                celebrate = false,
                isHumanPlayer = currentGameState.currentDisk.isHumanPlayer,
            )
        }

        assertEquals(expectedState, result)
    }

    @Test
    fun `renderToState when showPossibleMoves is false`() {
        val initModel = GameBoardModel(
            boardDisplayOptions = BoardDisplayOptions.Default.copy(
                showPossibleMoves = false,
            ),
        )

        val result = subject.renderState(initModel)

        val expectedState = with(initModel) {
            GameBoardState(
                board = currentGameState.board.toImmutableList(),
                overlay = currentGameState.board.newEmptyOverlay().toImmutableList(),
                currentDisk = currentGameState.currentDisk,
                darkStrategyName = darkStrategy?.name,
                lightStrategyName = lightStrategy?.name,
                diskCount = currentGameState.diskCount,
                opponentName = lightStrategy?.name,
                displayedTurn = currentGameState.turn + 1,
                displayedMaxTurnCount = turnCount + 1,
                hasNextTurn = false,
                isCurrentTurn = true,
                nextMovePosition = nextMovePosition,
                displayOptions = boardDisplayOptions,
                ended = ended,
                passed = passed,
                celebrate = false,
                isHumanPlayer = currentGameState.currentDisk.isHumanPlayer,
            )
        }

        assertEquals(expectedState, result)
    }

    @Test
    fun `renderToState when nextMovePosition is set`() {
        val initModel = GameBoardModel(
            boardDisplayOptions = BoardDisplayOptions.Default.copy(
                showPossibleMoves = false,
            ),
            nextMovePosition = Position(2, 3),
        )

        val result = subject.renderState(initModel)

        val expectedOverlay = initModel.currentGameState.board.newEmptyOverlay().apply {
            putAt(2, 3, NextMoveIndicatorOverlayItem(disk = initModel.currentGameState.currentDisk))
        }
        val expectedState = with(initModel) {
            GameBoardState(
                board = currentGameState.board.toImmutableList(),
                overlay = expectedOverlay.toImmutableList(),
                currentDisk = currentGameState.currentDisk,
                darkStrategyName = darkStrategy?.name,
                lightStrategyName = lightStrategy?.name,
                diskCount = currentGameState.diskCount,
                opponentName = lightStrategy?.name,
                displayedTurn = currentGameState.turn + 1,
                displayedMaxTurnCount = turnCount + 1,
                hasNextTurn = false,
                isCurrentTurn = true,
                nextMovePosition = nextMovePosition,
                displayOptions = boardDisplayOptions,
                ended = ended,
                passed = passed,
                celebrate = false,
                isHumanPlayer = currentGameState.currentDisk.isHumanPlayer,
            )
        }

        assertEquals(expectedState, result)
    }

    @Test
    fun `renderToState when ended is set and human wins`() {
        val winner = Disk.Dark
        val initModel = GameBoardModel(
            boardDisplayOptions = BoardDisplayOptions.Default.copy(
                showPossibleMoves = false,
            ),
            ended = EndedWin(winner = winner),
        )

        val result = subject.renderState(initModel)

        val expectedState = with(initModel) {
            GameBoardState(
                board = currentGameState.board.toImmutableList(),
                overlay = currentGameState.board.newEmptyOverlay().toImmutableList(),
                currentDisk = currentGameState.currentDisk,
                darkStrategyName = darkStrategy?.name,
                lightStrategyName = lightStrategy?.name,
                diskCount = currentGameState.diskCount,
                opponentName = lightStrategy?.name,
                displayedTurn = currentGameState.turn + 1,
                displayedMaxTurnCount = turnCount + 1,
                hasNextTurn = false,
                isCurrentTurn = true,
                nextMovePosition = nextMovePosition,
                displayOptions = boardDisplayOptions,
                ended = ended,
                passed = passed,
                celebrate = true,
                isHumanPlayer = currentGameState.currentDisk.isHumanPlayer,
            )
        }

        assertEquals(expectedState, result)
    }

    @Test
    fun `renderToState when ended is set and human wins and past turn`() {
        val winner = Disk.Dark
        val initModel = GameBoardModel(
            selectedTurn = 42,
            boardDisplayOptions = BoardDisplayOptions.Default.copy(
                showPossibleMoves = false,
            ),
            ended = EndedWin(winner = winner),
        )

        val result = subject.renderState(initModel)

        val expectedState = with(initModel) {
            GameBoardState(
                board = currentGameState.board.toImmutableList(),
                overlay = currentGameState.board.newEmptyOverlay().toImmutableList(),
                currentDisk = currentGameState.currentDisk,
                darkStrategyName = darkStrategy?.name,
                lightStrategyName = lightStrategy?.name,
                diskCount = currentGameState.diskCount,
                opponentName = lightStrategy?.name,
                displayedTurn = 43,
                displayedMaxTurnCount = turnCount + 1,
                hasNextTurn = false,
                isCurrentTurn = false,
                nextMovePosition = nextMovePosition,
                displayOptions = boardDisplayOptions,
                ended = null,
                passed = passed,
                celebrate = false,
                isHumanPlayer = currentGameState.currentDisk.isHumanPlayer,
            )
        }

        assertEquals(expectedState, result)
    }
}
