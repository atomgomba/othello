package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Loop
import com.ekezet.othello.feature.gameboard.GameBoardAction.OnCellClicked
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.NextMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.ValidMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.newEmptyOverlay
import com.ekezet.othello.feature.gameboard.ui.viewModels.putAt
import com.ekezet.othello.feature.gameboard.ui.viewModels.toList
import kotlinx.coroutines.CoroutineScope

internal class GameBoardLoop(parentScope: CoroutineScope, initModel: GameBoardModel) :
    Loop<GameBoardState, GameBoardModel, GameBoardArgs, Unit, GameBoardAction>(
        loopCoroutineScope = parentScope,
        initModel = initModel,
    ) {

    override fun GameBoardModel.applyArgs(args: GameBoardArgs) =
        copy(
            displayOptions = args.displayOptions,
            opponentStrategy = args.opponentStrategy,
        )

    override fun renderState(model: GameBoardModel) = with(model) {
        GameBoardState(
            board = gameState.currentBoard.toList(),
            overlay = createOverlayItems(),
            currentDisk = gameState.currentDisk,
            diskCount = diskCount,
            opponentName = opponentStrategy?.name ?: "Human",
            currentTurn = gameState.turn + 1,
            nextMovePosition = nextMovePosition,
            showPossibleMoves = displayOptions.showPossibleMoves,
            showBoardPositions = displayOptions.showBoardPositions,
            hasPossibleMoves = gameState.validMoves.isNotEmpty(),
            onCellClick = { x, y -> emit(OnCellClicked(x to y)) },
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
            items.putAt(nextMovePosition, NextMoveIndicatorOverlayItem)
        }

        return items.toList()
    }
}

internal fun gameBoardLoop(parentScope: CoroutineScope, args: GameBoardArgs) =
    GameBoardLoop(parentScope = parentScope, initModel = GameBoardModel.fromArgs(args))
