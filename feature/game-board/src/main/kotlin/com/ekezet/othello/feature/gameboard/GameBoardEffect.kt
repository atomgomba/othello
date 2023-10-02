package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Effect
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.feature.gameboard.GameBoardAction.OnCellClicked
import com.ekezet.othello.feature.gameboard.GameBoardAction.OnGameEnded
import com.ekezet.othello.feature.gameboard.GameBoardAction.OnTurnPassed
import kotlinx.coroutines.delay

internal sealed interface GameBoardEffect : Effect<GameBoardModel, Unit> {

    data class WaitBeforeNextTurn(
        private val nextMove: Position,
    ) : GameBoardEffect {
        override suspend fun GameBoardScope.trigger(dependency: Unit?) {
            delay(ACTION_DELAY_MILLIS)
            emit(OnCellClicked(nextMove))
        }
    }

    data class WaitBeforePass(
        private val nextMove: Position?,
        private val newState: GameState,
    ) : GameBoardEffect {
        override suspend fun GameBoardScope.trigger(dependency: Unit?) {
            delay(ACTION_DELAY_MILLIS)
            emit(OnTurnPassed(nextMove, newState))
        }
    }

    data class WaitBeforeGameEnd(
        private val result: GameEnd,
    ) : GameBoardEffect {
        override suspend fun GameBoardScope.trigger(dependency: Unit?) {
            delay(ACTION_DELAY_MILLIS)
            emit(OnGameEnded(result))
        }
    }
}
