package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.serialize.asString
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.NextTurn
import com.ekezet.othello.core.game.PassTurn
import com.ekezet.othello.core.game.Tie
import com.ekezet.othello.core.game.Win
import com.ekezet.othello.core.game.isValid
import com.ekezet.othello.core.game.throwable.InvalidMoveException
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.GameEnd
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn
import timber.log.Timber

internal sealed interface GameBoardAction : Action<GameBoardModel, Unit>

internal data object OnGameStarted : GameBoardAction {
    override fun GameBoardModel.proceed() = if (ended != null || darkStrategy == null) {
        skip
    } else {
        val nextMove = darkStrategy.deriveNext(gameState)
            ?: error("Strategy couldn't find a valid starting move")
        trigger(WaitBeforeNextTurn(nextMove))
    }
}

internal data class OnMoveMade(val position: Position) : GameBoardAction {
    override fun GameBoardModel.proceed() = if (gameState.validMoves.isValid(position)) {
        change(pickNextMoveAt(position))
    } else {
        skip
    }
}

internal data object ContinueGame : GameBoardAction {
    override fun GameBoardModel.proceed(): Next<GameBoardModel, Unit> {
        val moveResult = try {
            gameState.proceed(nextMovePosition, currentDisk)
        } catch (e: InvalidMoveException) {
            Timber.w("Invalid move at ${nextMovePosition?.asString()}")
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
    val newState: GameState,
) : GameBoardAction {
    override fun GameBoardModel.proceed() =
        change(resetNextTurn(newState, passed = true).pickNextMoveAt(nextPosition))
}

internal data class OnGameEnded(val result: GameEnd) : GameBoardAction {
    override fun GameBoardModel.proceed() = change(copy(ended = result))
}
