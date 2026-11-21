package com.ekezet.othello.feature.gameBoard.actions

import com.ekezet.hurok.next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.state.OthelloGameState
import com.ekezet.othello.feature.gameBoard.GameBoardModel
import com.ekezet.othello.feature.gameBoard.WaitBeforeNextTurn

typealias OnStrategyClick = (disk: Disk) -> Unit

data class OnUpdateGameState(
    private val newState: OthelloGameState,
) : GameBoardAction {
    override fun proceed(model: GameBoardModel) = with(model) {
        val effects = buildList {
            darkStrategy?.deriveNext(newState)?.let { nextMove ->
                // only wait if there's a valid next move
                add(WaitBeforeNextTurn(nextMove))
            }
        }
        next(resetNewGame(newState), effects = effects.toTypedArray())
    }
}
