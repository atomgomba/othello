package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.NextMove
import com.ekezet.othello.core.game.isValid
import com.ekezet.othello.core.game.throwable.InvalidMoveException
import com.ekezet.othello.feature.gameboard.GameBoardEffect.DeriveOpponentMove

internal sealed interface GameBoardAction : Action<GameBoardModel, Unit> {

    data class OnCellClicked(val at: Position) : GameBoardAction {
        override fun GameBoardModel.proceed() =
            if (gameState.validMoves.isValid(at)) {
                change(copy(nextMovePosition = at))
            } else {
                skip
            }
    }

    data object ContinueGame : GameBoardAction {
        override fun GameBoardModel.proceed(): Next<GameBoardModel, Unit> {
            nextMovePosition ?: return skip
            val newState = try {
                gameState.proceed(NextMove(nextMovePosition, currentDisk))
            } catch (e: InvalidMoveException) {
                return skip
            }
            return nextTurn(newState)
        }

        private fun GameBoardModel.nextTurn(
            newState: GameState,
        ) = outcome(
                model = copy(
                    gameState = newState,
                    nextMovePosition = null,
                ),
                effects = buildList<GameBoardEffect> {
                    if (opponentStrategy != null && newState.currentDisk == Disk.Light) {
                        add(DeriveOpponentMove(newState, opponentStrategy))
                    }
                }.toTypedArray(),
            )
    }
}
