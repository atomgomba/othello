package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.state.OthelloGameState
import com.ekezet.othello.feature.gameboard.GameBoardDependency
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn

typealias OnStrategyClick = (disk: Disk) -> Unit

data class OnUpdateGameState(
    private val newState: OthelloGameState,
) : GameBoardAction {
    override fun GameBoardModel.proceed(): Next<GameBoardModel, GameBoardDependency> {
        val effects = buildList {
            darkStrategy?.deriveNext(newState)?.let { nextMove ->
                // only wait if there's a valid next move
                add(WaitBeforeNextTurn(nextMove))
            }
        }
        return outcome(resetNextTurn(newState), effects = effects.toTypedArray())
    }
}
