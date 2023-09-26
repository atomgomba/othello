package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.NextMove
import com.ekezet.othello.core.game.error.InvalidMoveException
import com.ekezet.othello.core.game.isValid
import com.ekezet.othello.feature.gameboard.GameBoardEffect.DeriveOpponentMove

internal sealed interface GameBoardAction : Action<GameBoardModel, GameBoardDependency> {

    data object OnResetGameClicked : GameBoardAction {
        override fun GameBoardModel.proceed() = change(
            copy(
                gameState = defaultGameState,
                nextMovePosition = null,
            ),
        )
    }

    data object OnToggleIndicatorsClicked : GameBoardAction {
        override fun GameBoardModel.proceed() = change(
            copy(
                displayOptions = displayOptions.copy(
                    showPossibleMoves = !displayOptions.showPossibleMoves,
                ),
            ),
        )
    }

    data class OnCellClicked(val at: Position) : GameBoardAction {
        override fun GameBoardModel.proceed() = if (gameState.validMoves.isValid(at)) {
            change(copy(nextMovePosition = at))
        } else {
            skip
        }
    }

    data object ContinueGame : GameBoardAction {
        override fun GameBoardModel.proceed(): Next<GameBoardModel, GameBoardDependency> {
            nextMovePosition ?: return skip
            val newState = try {
                gameState.proceed(NextMove(nextMovePosition, currentDisk))
            } catch (e: InvalidMoveException) {
                return skip
            }
            val effects = buildList<GameBoardEffect> {
                if (opponentStrategy != null && newState.currentDisk == Disk.Light) {
                    add(DeriveOpponentMove(newState, opponentStrategy))
                }
            }
            return outcome(
                model = copy(
                    gameState = newState,
                    nextMovePosition = null,
                ),
                effects = effects.toTypedArray(),
            )
        }
    }
}
