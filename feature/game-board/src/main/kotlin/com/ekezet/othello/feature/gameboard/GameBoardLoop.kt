package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.defaultGameState
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin
import com.ekezet.othello.feature.gameboard.actions.GameBoardAction
import com.ekezet.othello.feature.gameboard.actions.OnGameStarted
import com.ekezet.othello.feature.gameboard.actions.OnMoveMade
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.NextMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.ValidMoveIndicatorOverlayItem
import com.ekezet.othello.feature.gameboard.ui.viewModels.newEmptyOverlay
import com.ekezet.othello.feature.gameboard.ui.viewModels.putAt
import com.ekezet.othello.feature.gameboard.ui.viewModels.toList

internal class GameBoardLoop internal constructor(
    model: GameBoardModel,
    args: GameSettings? = null,
    firstAction: GameBoardAction? = null,
) : Loop<GameBoardState, GameBoardModel, GameSettings, Unit, GameBoardAction>(
    model = model,
    args = args,
    firstAction = firstAction,
) {
    private val actions = GameBoardStateActions(
        onCellClick = { x, y -> emit(OnMoveMade(x to y)) },
    )

    override fun GameBoardModel.applyArgs(args: GameSettings): GameBoardModel {
        val strategyChanged =
            lightStrategy != args.lightStrategy || darkStrategy != args.darkStrategy
        return copy(
            displayOptions = args.displayOptions,
            lightStrategy = args.lightStrategy,
            darkStrategy = args.darkStrategy,
        ).run {
            if (strategyChanged) {
                resetNextTurn(defaultGameState)
            } else {
                this
            }
        }
    }

    override fun renderState(model: GameBoardModel) = with(model) {
        GameBoardState(
            actions = actions,
            board = gameState.currentBoard.toList(),
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

        return items.toList()
    }

    internal companion object Builder :
        LoopBuilder<GameBoardState, GameBoardModel, GameSettings, Unit, GameBoardAction> {
        override fun build(
            args: GameSettings?,
        ) = GameBoardLoop(
            model = GameBoardModel(),
            args = requireNotNull(args) { "Arguments must be set" },
            firstAction = OnGameStarted,
        )
    }
}
