package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.OthelloGameState
import com.ekezet.othello.core.game.serialize.GameStateSerializer
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn

typealias OnStrategyClick = (disk: Disk) -> Unit

data class OnUpdateGameState(
    private val newState: OthelloGameState,
) : GameBoardAction {
    override fun GameBoardModel.proceed(): Next<GameBoardModel, Unit> {
        val effects = buildList {
            darkStrategy?.deriveNext(newState)?.let { nextMove ->
                add(WaitBeforeNextTurn(nextMove))
            }
        }
        return outcome(resetNextTurn(newState), effects = effects.toTypedArray())
    }
}

data class OnSerializeGameState(
    private val callback: (data: String) -> Unit,
) : GameBoardAction {
    override fun GameBoardModel.proceed(): Next<GameBoardModel, Unit> {
        val data = GameStateSerializer.toString(gameState, this)
        callback(data)
        return skip
    }
}
