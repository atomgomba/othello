package com.ekezet.othello.feature.gamehistory

import com.ekezet.hurok.Renderer

internal class GameHistoryRenderer : Renderer<GameHistoryModel, GameHistoryDependency, GameHistoryState> {
    override fun renderState(model: GameHistoryModel) = GameHistoryState
}
