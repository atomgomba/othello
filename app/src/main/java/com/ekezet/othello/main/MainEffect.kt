package com.ekezet.othello.main

import com.ekezet.hurok.Effect
import com.ekezet.othello.core.game.OthelloGameState
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.effect.PublishGameSettings
import com.ekezet.othello.feature.gameboard.actions.OnUpdateGameState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal sealed interface MainEffect : Effect<MainModel, MainDependency>

internal data object CollectData : MainEffect {
    override suspend fun MainActionEmitter.trigger(dependency: MainDependency?) = dependency?.run {
        collectJob?.cancel()
        collectJob = parentScope.launch {
            launch {
                moveHistoryStore.history.collectLatest {
                    emit(OnMoveHistoryChanged(isNotEmpty = it.isNotEmpty()))
                }
            }
        }
    }
}

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
