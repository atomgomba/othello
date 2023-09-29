package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.NextMove
import com.ekezet.othello.core.game.NextTurn
import com.ekezet.othello.core.game.PassTurn
import com.ekezet.othello.core.game.Tie
import com.ekezet.othello.core.game.Win
import com.ekezet.othello.core.game.isValid
import com.ekezet.othello.core.game.throwable.InvalidMoveException
import com.ekezet.othello.feature.gameboard.GameBoardEffect.WaitBeforeNextAction
import com.ekezet.othello.feature.gameboard.GameEnd.EndedTie
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin

internal sealed interface GameBoardAction : Action<GameBoardModel, Unit> {

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
            val moveResult = try {
                gameState.proceed(NextMove(nextMovePosition, currentDisk))
            } catch (e: InvalidMoveException) {
                return skip
            }
            return when (moveResult) {
                is NextTurn -> nextTurn(moveResult.state)
                is PassTurn -> passTurn(moveResult.state)
                is Win -> finishGame(moveResult.state, moveResult.winner)
                is Tie -> finishGame(moveResult.state, null)
            }
        }

        private fun GameBoardModel.nextTurn(newState: GameState): Next<GameBoardModel, Unit> {
            val effects = mutableListOf<GameBoardEffect>()
            if (opponentStrategy != null && newState.currentDisk == Disk.Light) {
                val nextMove = opponentStrategy.deriveNext(newState)
                if (nextMove != null) {
                    effects.add(WaitBeforeNextAction(OnCellClicked(nextMove)))
                }
            }
            return outcome(
                model = resetGameState(newState),
                effects = effects.toTypedArray(),
            )
        }

        private fun GameBoardModel.passTurn(newState: GameState) =
            change(
                model = resetGameState(newState),
            )

        private fun GameBoardModel.finishGame(newState: GameState, winner: Disk?) =
            outcome(
                model = resetGameState(newState),
                WaitBeforeNextAction(EndGame(winner?.let { EndedWin(it) } ?: EndedTie))
            )
    }

    data class EndGame(val result: GameEnd) : GameBoardAction {
        override fun GameBoardModel.proceed() = change(copy(ended = result))
    }
}

data class OnUpdateGameState(
    private val newState: GameState,
) : GameBoardAction {
    override fun GameBoardModel.proceed() = change(resetGameState(newState))
}
