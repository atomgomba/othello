package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.serialize.asString
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.NextMove
import com.ekezet.othello.core.game.NextTurn
import com.ekezet.othello.core.game.PassTurn
import com.ekezet.othello.core.game.Tie
import com.ekezet.othello.core.game.Win
import com.ekezet.othello.core.game.isValid
import com.ekezet.othello.core.game.serialize.GameStateSerializer
import com.ekezet.othello.core.game.throwable.InvalidMoveException
import com.ekezet.othello.feature.gameboard.GameBoardEffect.WaitBeforeGameEnd
import com.ekezet.othello.feature.gameboard.GameBoardEffect.WaitBeforeNextTurn
import com.ekezet.othello.feature.gameboard.GameBoardEffect.WaitBeforePass
import com.ekezet.othello.feature.gameboard.GameEnd.EndedTie
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin
import timber.log.Timber

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
                Timber.w("Invalid move at ${nextMovePosition.asString()}")
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
            val strategy = if (newState.currentDisk == Disk.Light) lightStrategy else darkStrategy
            if (strategy != null) {
                val nextMove = strategy.deriveNext(newState)
                if (nextMove != null) {
                    effects.add(WaitBeforeNextTurn(nextMove))
                }
            }
            return outcome(
                model = resetNextTurn(newState),
                effects = effects.toTypedArray(),
            )
        }

        private fun GameBoardModel.passTurn(newState: GameState): Next<GameBoardModel, Unit> {
            val strategy = if (newState.currentDisk == Disk.Light) lightStrategy else darkStrategy
            val nextMove = strategy?.deriveNext(newState)
            return trigger(WaitBeforePass(nextMove, newState))
        }

        private fun GameBoardModel.finishGame(newState: GameState, winner: Disk?) = outcome(
            model = resetNextTurn(newState),
            WaitBeforeGameEnd(winner?.let { EndedWin(it) } ?: EndedTie),
        )
    }

    data object OnGameStarted : GameBoardAction {
        override fun GameBoardModel.proceed() =
            if (darkStrategy == null) {
                // dark is a human
                skip
            } else {
                val nextMove = darkStrategy.deriveNext(gameState) ?: error("Starting the game is impossible")
                trigger(WaitBeforeNextTurn(nextMove))
            }
    }

    data class OnTurnPassed(val nextMove: Position?, val newState: GameState) : GameBoardAction {
        override fun GameBoardModel.proceed() =
            change(resetNextTurn(newState).copy(nextMovePosition = nextMove))
    }

    data class OnGameEnded(val result: GameEnd) : GameBoardAction {
        override fun GameBoardModel.proceed() = change(copy(ended = result))
    }
}

data class OnUpdateGameState(
    private val newState: GameState,
) : GameBoardAction {
    override fun GameBoardModel.proceed(): Next<GameBoardModel, Unit> {
        val nextMove = darkStrategy?.deriveNext(newState)
        val effects = buildList {
            if (nextMove != null) {
                add(WaitBeforeNextTurn(nextMove))
            }
        }
        return outcome(resetNextTurn(newState), effects = effects.toTypedArray())
    }
}

data class OnSerializeBoard(
    private val callback: (data: String) -> Unit,
) : GameBoardAction {
    override fun GameBoardModel.proceed(): Next<GameBoardModel, Unit> {
        val data = GameStateSerializer.toString(gameState, this)
        callback(data)
        return skip
    }
}
