package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Renderer
import com.ekezet.othello.core.game.GameEnd.EndedWin
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.ui.viewModels.toImmutableList
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.NextMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.PastMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.ValidMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.newEmptyOverlay
import com.ekezet.othello.feature.gameboard.ui.viewModels.putAt
import com.ekezet.othello.feature.gameboard.ui.viewModels.toImmutableList

internal class GameBoardRenderer : Renderer<GameBoardModel, GameBoardDependency, GameBoardState> {
    private val GameBoardModel.historicalMove: PastMove?
        inline get() = if (isCurrentTurn) null else currentGameState.pastMoves.lastOrNull()

    override fun renderState(model: GameBoardModel) = with(model) {
        GameBoardState(
            board = currentGameState.board.toImmutableList(),
            overlay = createOverlayItems(),
            currentDisk = currentGameState.currentDisk,
            darkStrategyName = darkStrategy?.name,
            lightStrategyName = lightStrategy?.name,
            diskCount = currentGameState.diskCount,
            opponentName = lightStrategy?.name,
            displayedTurn = selectedTurn + 1,
            displayedMaxTurnCount = turnCount + 1,
            hasNextTurn = selectedTurn < turnCount,
            nextMovePosition = nextMovePosition,
            displayOptions = boardDisplayOptions,
            ended = ended.takeIf { isCurrentTurn },
            passed = if (historicalMove != null) historicalMove!!.moveAt == null else passed,
            celebrate = isCurrentTurn && ended is EndedWin && ended.winner.isHumanPlayer,
            isHumanPlayer = currentGameState.currentDisk.isHumanPlayer,
            isCurrentTurn = isCurrentTurn,
        )
    }

    private fun GameBoardModel.createOverlayItems(): BoardOverlayList {
        val items = currentGameState.board.newEmptyOverlay()
        if (boardDisplayOptions.showPossibleMoves) {
            val validMoves = currentGameState.validMoves
            if (validMoves.isNotEmpty()) {
                for (move in validMoves) {
                    items.putAt(move.position, ValidMoveIndicatorOverlayItem(currentDisk))
                }
            }
        }
        if (nextMovePosition != null) {
            items.putAt(nextMovePosition, NextMoveIndicatorOverlayItem(currentDisk))
        }
        historicalMove?.let { pastMove ->
            val moveAt = pastMove.moveAt
            if (moveAt != null) {
                items.putAt(moveAt, PastMoveIndicatorOverlayItem(pastMove.disk))
            }
        }
        return items.toImmutableList()
    }
}
