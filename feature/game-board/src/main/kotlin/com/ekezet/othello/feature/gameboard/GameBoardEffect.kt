package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Effect
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.GameBoardAction.OnCellClicked
import kotlinx.coroutines.delay

internal sealed interface GameBoardEffect : Effect<GameBoardModel, Unit> {

    data class DeriveOpponentMove(
        private val state: GameState,
        private val strategy: Strategy,
    ) : GameBoardEffect {
        override suspend fun GameBoardScope.trigger(dependency: Unit?) {
            val next = strategy.deriveNext(state)
            if (next != null) {
                delay(MOVE_DELAY_MILLIS)
                emit(OnCellClicked(next))
            } else {
                // TODO: Opponent must pass or has lost
            }
        }
    }
}
