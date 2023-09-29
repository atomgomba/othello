package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Effect
import kotlinx.coroutines.delay

internal sealed interface GameBoardEffect : Effect<GameBoardModel, Unit> {

    data class WaitBeforeNextAction(
        private val nextAction: GameBoardAction,
        private val delayMillis: Long = ACTION_DELAY_MILLIS,
    ) : GameBoardEffect {
        override suspend fun GameBoardScope.trigger(dependency: Unit?) {
            delay(delayMillis)
            emit(nextAction)
        }
    }
}
