package com.ekezet.othello.feature.gamehistory

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder

internal class GameHistoryLoop(
    model: GameHistoryModel,
    renderer: GameHistoryRenderer,
    firstAction: GameHistoryAction? = null,
    dependency: GameHistoryDependency? = null,
) : Loop<GameHistoryState, GameHistoryModel, Unit, GameHistoryDependency, GameHistoryAction>(
    model = model,
    renderer = renderer,
    firstAction = firstAction,
    dependency = dependency,
) {
    internal companion object Builder : LoopBuilder<GameHistoryState, GameHistoryModel, Unit, GameHistoryDependency, GameHistoryAction> {
        override fun build(args: Unit?) = GameHistoryLoop(
            model = GameHistoryModel(),
            renderer = GameHistoryRenderer(),
            dependency = GameHistoryDependency(),
        )
    }
}
