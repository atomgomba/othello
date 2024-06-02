package com.ekezet.othello.main

import com.ekezet.hurok.Effect
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.effect.PublishGameSettings
import com.ekezet.othello.core.game.state.OthelloGameState
import com.ekezet.othello.feature.gameboard.actions.OnUpdateGameState

internal sealed interface MainEffect : Effect<MainModel, MainDependency>

internal data class PublishGameSettings(
    override val settings: IGameSettings,
) : MainEffect, PublishGameSettings<MainModel, MainDependency>()

internal data class UpdateGameBoardGameState(
    private val newState: OthelloGameState,
) : MainEffect {
    override suspend fun MainActionEmitter.trigger(
        dependency: MainDependency?,
    ) = dependency?.run {
        gameBoardEmitter?.emit(OnUpdateGameState(newState))
    }
}

internal data object ResetPastMoves : MainEffect {
    override suspend fun MainActionEmitter.trigger(dependency: MainDependency?) =
        dependency?.run {
            gameHistoryStore.reset()
        }
}
