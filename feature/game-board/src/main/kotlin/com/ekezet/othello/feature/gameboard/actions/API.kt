package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.serialize.GameStateSerializer
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn

 data class OnUpdateGameState(
    private val newState: GameState,
) : GameBoardAction {
    override fun GameBoardModel.proceed(): Next<GameBoardModel, Unit> {
        val nextMove = darkStrategy?.deriveNext(newState)
        val effects = buildList {
            if (nextMove != null) {
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
