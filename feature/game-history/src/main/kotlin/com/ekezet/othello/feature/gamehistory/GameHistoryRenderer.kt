package com.ekezet.othello.feature.gamehistory

import com.ekezet.hurok.Renderer
import com.ekezet.othello.core.data.models.diskCount
import com.ekezet.othello.core.game.toPastGameState
import com.ekezet.othello.core.ui.viewModels.toImmutableList
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem
import kotlinx.collections.immutable.toImmutableList

internal class GameHistoryRenderer : Renderer<GameHistoryModel, GameHistoryDependency, GameHistoryState> {
    override fun renderState(model: GameHistoryModel) = with(model) {
        GameHistoryState(
            historyItems = renderHistoryItems(),
            gameEnd = gameEnd,
            lastState = moveHistory.toPastGameState(),
            isGrayscaleMode = gameSettings.boardDisplayOptions.isGrayscaleMode,
        )
    }

    private fun GameHistoryModel.renderHistoryItems() = moveHistory.map {
        val (darkCount, lightCount) = it.board.diskCount
        HistoryItem(
            turn = it.turn,
            move = it.moveAt,
            disk = it.disk,
            board = it.board.toImmutableList(),
            image = historyImages[it.uuid],
            darkCount = darkCount,
            lightCount = lightCount,
        )
    }.toImmutableList()
}
