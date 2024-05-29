package com.ekezet.othello.feature.gamehistory

import com.ekezet.hurok.Renderer
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.core.game.toPastGameState
import com.ekezet.othello.core.ui.viewModels.toImmutableList
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem
import kotlinx.collections.immutable.toImmutableList

internal class GameHistoryRenderer : Renderer<GameHistoryModel, GameHistoryDependency, GameHistoryState> {
    override fun renderState(model: GameHistoryModel) = GameHistoryState(
        historyItems = model.moveHistory.render(),
        gameEnd = model.gameEnd,
        lastState = model.moveHistory.toPastGameState(),
    )

    private fun MoveHistory.render() = map {
        HistoryItem(
            turn = it.turn,
            move = it.moveAt,
            disk = it.disk,
            board = it.board.toImmutableList(),
        )
    }.toImmutableList()
}
