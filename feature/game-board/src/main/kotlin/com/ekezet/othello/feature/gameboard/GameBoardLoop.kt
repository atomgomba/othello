package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.feature.gameboard.GameBoardAction.OnCellClicked
import com.ekezet.othello.feature.gameboard.GameBoardAction.OnGameStarted
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.NextMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.ValidMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.newEmptyOverlay
import com.ekezet.othello.feature.gameboard.ui.viewModels.putAt
import com.ekezet.othello.feature.gameboard.ui.viewModels.toList

internal class GameBoardLoop(args: GameBoardArgs?) :
    Loop<GameBoardState, GameBoardModel, GameBoardArgs, Unit, GameBoardAction>(
        args = args,
    ) {

    override fun initModel() = GameBoardModel()

    override fun onLoopStarted() {
        emit(OnGameStarted)
    }

    override fun GameBoardModel.applyArgs(args: GameBoardArgs) =
        copy(
            displayOptions = args.displayOptions,
            lightStrategy = args.lightStrategy,
            darkStrategy = args.darkStrategy,
        )

    override fun renderState(model: GameBoardModel) = with(model) {
        GameBoardState(
            board = gameState.currentBoard.toList(),
            overlay = createOverlayItems(),
            currentDisk = gameState.currentDisk,
            diskCount = gameState.diskCount,
            opponentName = lightStrategy?.name,
            currentTurn = gameState.turn + 1,
            nextMovePosition = nextMovePosition,
            showPossibleMoves = displayOptions.showPossibleMoves,
            showBoardPositions = displayOptions.showBoardPositions,
            ended = ended,
            celebrate = ended is EndedWin && isHumanPlayer(ended.winner),
            isHumanPlayer = isHumanPlayer(gameState.currentDisk),
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

    internal companion object Builder :
        LoopBuilder<GameBoardState, GameBoardModel, GameBoardArgs, Unit, GameBoardAction> {
        override fun invoke(
            args: GameBoardArgs?,
            dependency: Unit?,
        ) = GameBoardLoop(args = args)
    }
}
