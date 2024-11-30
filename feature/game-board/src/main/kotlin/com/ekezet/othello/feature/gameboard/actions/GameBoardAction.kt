package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.NextTurn
import com.ekezet.othello.core.game.PassTurn
import com.ekezet.othello.core.game.Tie
import com.ekezet.othello.core.game.Win
import com.ekezet.othello.core.game.isValid
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.throwable.InvalidMoveException
import com.ekezet.othello.feature.gameboard.GameBoardDependency
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn
import timber.log.Timber

internal sealed interface GameBoardAction : Action<GameBoardModel, GameBoardDependency>

internal data object OnLoopStarted : GameBoardAction {
    override fun GameBoardModel.proceed() = if (ended != null || currentStrategy == HumanPlayer || !isCurrentTurn) {
        skip
    } else {
        val nextMove = currentStrategy?.deriveNext(currentGameState)
            ?: error("Strategy couldn't find a valid starting move")
        trigger(WaitBeforeNextTurn(nextMove))
    }
}

internal data class OnMoveMade(val position: Position) : GameBoardAction {
    override fun GameBoardModel.proceed() = if (currentGameState.validMoves.isValid(position)) {
        mutate(pickNextMoveAt(position))
    } else {
        skip
    }
}

internal data object ContinueGame : GameBoardAction {
    override fun GameBoardModel.proceed(): Next<GameBoardModel, GameBoardDependency> {
        val moveResult = try {
            currentGameState.proceed(nextMovePosition ?: return skip)
        } catch (e: InvalidMoveException) {
            Timber.w(e)
            return skip
        }
        return when (moveResult) {
            is NextTurn -> nextTurn(moveResult.state)
            is PassTurn -> passTurn(moveResult.state)
            is Win -> finishGame(moveResult.state, moveResult.winner)
            is Tie -> finishGame(moveResult.state, null)
        }
    }
}

internal data class OnTurnPassed(
    val nextPosition: Position?,
    val newState: CurrentGameState,
) : GameBoardAction {
    override fun GameBoardModel.proceed() =
        mutate(resetNextTurn(newState).pickNextMoveAt(nextPosition))
}

internal data class OnGameEnded(val result: GameEnd) : GameBoardAction {
    override fun GameBoardModel.proceed() = mutate(copy(ended = result))
}

internal data object OnFirstTurnClicked : GameBoardAction {
    override fun GameBoardModel.proceed() = mutate(stepToFirstTurn())
}

internal data object OnPreviousTurnClicked : GameBoardAction {
    override fun GameBoardModel.proceed() = mutate(stepToPreviousTurn())
}

internal data object OnNextTurnClicked : GameBoardAction {
    override fun GameBoardModel.proceed(): Next<GameBoardModel, GameBoardDependency> {
        val nextModel = stepToNextTurn()
        val gameState = nextModel.currentGameState
        return if (gameState is CurrentGameState && !nextModel.currentDisk.isHumanPlayer) {
            nextTurn(gameState)
        } else {
            mutate(nextModel)
        }
    }
}

internal data object OnCurrentTurnClicked : GameBoardAction {
    override fun GameBoardModel.proceed(): Next<GameBoardModel, GameBoardDependency> {
        val nextModel = stepToCurrentTurn()
        val gameState = nextModel.currentGameState
        val nextMove = currentStrategy?.deriveNext(gameState)
        val effects = buildList {
            if (currentStrategy != HumanPlayer && nextMove != null) {
                // only wait if next player is not human
                add(WaitBeforeNextTurn(nextMove))
            }
        }
        return outcome(nextModel, effects = effects.toTypedArray())
    }
}
