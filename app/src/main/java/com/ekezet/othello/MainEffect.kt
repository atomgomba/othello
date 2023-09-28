package com.ekezet.othello

import com.ekezet.hurok.Effect
import com.ekezet.hurok.LoopScope
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.feature.gameboard.OnUpdateGameState

internal sealed interface MainEffect : Effect<MainModel, MainDependency> {
    data class UpdateGameBoardGameState(
        private val newState: GameState,
    ) : MainEffect {
        override suspend fun LoopScope<MainModel, MainDependency>.trigger(
            dependency: MainDependency?,
        ) = dependency?.run {
            gameBoardScope.emit(OnUpdateGameState(newState))
        }
    }
}
