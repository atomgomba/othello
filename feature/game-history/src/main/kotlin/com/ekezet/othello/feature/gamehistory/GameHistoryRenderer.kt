package com.ekezet.othello.feature.gamehistory

import androidx.compose.ui.graphics.asImageBitmap
import com.ekezet.hurok.Renderer
import com.ekezet.othello.core.game.toPastGameState
import com.ekezet.othello.core.ui.viewModels.toImmutableList
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem
import kotlinx.collections.immutable.toImmutableList

internal class GameHistoryRenderer : Renderer<GameHistoryModel, GameHistoryDependency, GameHistoryState> {
    override fun renderState(model: GameHistoryModel) = GameHistoryState(
        historyItems = model.render(),
        gameEnd = model.gameEnd,
        lastState = model.moveHistory.toPastGameState(),
    )

    private fun GameHistoryModel.render() = moveHistory.map {
        HistoryItem(
            turn = it.turn,
            move = it.moveAt,
            disk = it.disk,
            board = it.board.toImmutableList(),
            image = historyImages[it.renderId]?.asImageBitmap(),
        )
    }.toImmutableList()
}
