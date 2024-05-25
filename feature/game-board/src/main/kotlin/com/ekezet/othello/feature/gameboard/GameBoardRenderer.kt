package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Renderer
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.NextMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.ValidMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.newEmptyOverlay
import com.ekezet.othello.feature.gameboard.ui.viewModels.putAt
import com.ekezet.othello.feature.gameboard.ui.viewModels.toImmutableList

internal class GameBoardRenderer : Renderer<GameBoardModel, GameBoardDependency, GameBoardState> {
    override fun renderState(model: GameBoardModel) = with(model) {
        GameBoardState(
            board = gameState.currentBoard.toImmutableList(),
            overlay = createOverlayItems(),
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
            celebrate = ended is EndedWin && ended.winner.isHumanPlayer,
            isHumanPlayer = gameState.currentDisk.isHumanPlayer,
        )
    }

    private fun GameBoardModel.createOverlayItems(): BoardOverlayList {
        val items = gameState.currentBoard.newEmptyOverlay()

        if (displayOptions.showPossibleMoves) {
            val validMoves = gameState.validMoves
            if (validMoves.isNotEmpty()) {
                for (move in validMoves) {
                    items.putAt(move.position, ValidMoveIndicatorOverlayItem(currentDisk))
                }
            }
        }

        if (nextMovePosition != null) {
            items.putAt(nextMovePosition, NextMoveIndicatorOverlayItem(currentDisk))
        }

        return items.toImmutableList()
    }
}
