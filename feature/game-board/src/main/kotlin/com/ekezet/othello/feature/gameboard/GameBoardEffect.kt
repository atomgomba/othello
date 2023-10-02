package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Effect
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.feature.gameboard.actions.OnGameEnded
import com.ekezet.othello.feature.gameboard.actions.OnMoveMade
import com.ekezet.othello.feature.gameboard.actions.OnTurnPassed
import kotlinx.coroutines.delay

internal sealed interface GameBoardEffect : Effect<GameBoardModel, Unit>

internal data class WaitBeforeNextTurn(
    private val nextMove: Position,
) : GameBoardEffect {
    override suspend fun GameBoardScope.trigger(dependency: Unit?) {
        delay(ACTION_DELAY_MILLIS)
        emit(OnMoveMade(nextMove))
    }
}

internal data class WaitBeforePass(
    private val nextMove: Position?,
    private val newState: GameState,
) : GameBoardEffect {
    override suspend fun GameBoardScope.trigger(dependency: Unit?) {
        delay(ACTION_DELAY_MILLIS)
        emit(OnTurnPassed(nextMove, newState))
    }
}

internal data class WaitBeforeGameEnd(
    private val result: GameEnd,
) : GameBoardEffect {
    override suspend fun GameBoardScope.trigger(dependency: Unit?) {
        delay(ACTION_DELAY_MILLIS)
        emit(OnGameEnded(result))
    }
}
