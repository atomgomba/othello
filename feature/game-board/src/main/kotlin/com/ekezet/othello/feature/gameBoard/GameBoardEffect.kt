package com.ekezet.othello.feature.gameBoard

import com.ekezet.hurok.Effect
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameBoard.actions.OnGameEnded
import com.ekezet.othello.feature.gameBoard.actions.OnMoveMade
import com.ekezet.othello.feature.gameBoard.actions.OnTurnPassed
import com.ekezet.othello.feature.settings.feature.strategyPicker.showStrategyPickerFor
import kotlinx.coroutines.delay

internal sealed interface GameBoardEffect : Effect<GameBoardModel, GameBoardDependency>

internal data class WaitBeforeNextTurn(
    private val nextMove: Position,
) : GameBoardEffect {
    override suspend fun GameBoardEmitter.trigger(dependency: GameBoardDependency?) {
        delay(ACTION_DELAY_MILLIS)
        emit(OnMoveMade(nextMove))
    }
}

internal data class WaitBeforePassTurn(
    private val nextMove: Position?,
    private val newState: CurrentGameState,
) : GameBoardEffect {
    override suspend fun GameBoardEmitter.trigger(dependency: GameBoardDependency?) {
        delay(ACTION_DELAY_MILLIS * 2)
        emit(OnTurnPassed(nextMove, newState))
    }
}

internal data class WaitBeforeGameEnd(
    private val result: GameEnd,
) : GameBoardEffect {
    override suspend fun GameBoardEmitter.trigger(dependency: GameBoardDependency?) {
        delay(ACTION_DELAY_MILLIS)
        emit(OnGameEnded(result))
    }
}

internal data class PublishPastMoves(
    private val newState: CurrentGameState,
    private val gameEnd: GameEnd? = null,
) : GameBoardEffect {
    override suspend fun GameBoardEmitter.trigger(dependency: GameBoardDependency?) = dependency?.run {
        gameHistoryStore.reset(newState.pastMoves, gameEnd)
        renderJob?.cancel()
        renderJob = movesRenderer.updateJobs(newState.pastMoves)
    }
}

internal data class ShowStrategyPicker(val player: Disk, val current: Strategy?) : GameBoardEffect {
    override suspend fun GameBoardEmitter.trigger(dependency: GameBoardDependency?) = dependency?.run {
        strategyPickerLoop.emit(showStrategyPickerFor(player = player, default = current))
    }
}
