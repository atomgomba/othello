package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.data.defaultGameState
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.feature.gameboard.GameBoardDependency
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.ResetPastMoves
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn

typealias OnStrategyClick = (disk: Disk) -> Unit

data class OnUpdateGameState(
    private val newState: CurrentGameState,
) : GameBoardAction {
    override fun GameBoardModel.proceed(): Next<GameBoardModel, GameBoardDependency> {
        val effects = buildList {
            if (newState == defaultGameState) {
                add(ResetPastMoves)
            }
            darkStrategy?.deriveNext(newState)?.let { nextMove ->
                add(WaitBeforeNextTurn(nextMove))
            }
        }
        return outcome(resetNextTurn(newState), effects = effects.toTypedArray())
    }
}
