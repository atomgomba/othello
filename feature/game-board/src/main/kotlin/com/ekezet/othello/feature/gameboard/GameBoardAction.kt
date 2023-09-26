package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.NextMove
import com.ekezet.othello.core.game.error.InvalidMoveException
import com.ekezet.othello.core.game.isValid

internal sealed interface GameBoardAction : Action<GameBoardModel, Unit> {

    data object OnResetGameClicked : GameBoardAction {
        override fun GameBoardModel.proceed() = change(
            copy(
                gameState = defaultGameState,
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
        override fun GameBoardModel.proceed(): Next<GameBoardModel, Unit> {
            nextMovePosition ?: return skip
            val nextMove = NextMove(nextMovePosition, currentDisk)
            return try {
                change(
                    copy(
                        gameState = gameState.proceed(nextMove),
                        nextMovePosition = null,
                    ),
                )
            } catch (e: InvalidMoveException) {
                skip
            }
        }
    }
}
