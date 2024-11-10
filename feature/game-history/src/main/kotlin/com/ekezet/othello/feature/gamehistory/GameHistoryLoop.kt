package com.ekezet.othello.feature.gamehistory

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder

internal class GameHistoryLoop(
    model: GameHistoryModel,
    renderer: GameHistoryRenderer,
    args: GameHistoryArgs? = null,
    firstAction: GameHistoryAction? = null,
    dependency: GameHistoryDependency? = null,
) : Loop<GameHistoryState, GameHistoryModel, GameHistoryArgs, GameHistoryDependency, GameHistoryAction>(
    model = model,
    renderer = renderer,
    args = args,
    firstAction = firstAction,
    dependency = dependency,
) {
    override fun GameHistoryModel.applyArgs(args: GameHistoryArgs) = with(args) {
        copy(
            moveHistory = history.history,
            gameEnd = history.gameEnd,
            historyImages = historyImages,
            gameSettings = gameSettings,
            historySettings = historySettings,
        )
    }

    internal companion object Builder : LoopBuilder<GameHistoryState, GameHistoryModel, GameHistoryArgs, GameHistoryDependency, GameHistoryAction> {
        override fun build(args: GameHistoryArgs?) = GameHistoryLoop(
            model = GameHistoryModel(),
            renderer = GameHistoryRenderer(),
            args = requireNotNull(args) { "Args must be set" },
            dependency = GameHistoryDependency,
        )
    }
}
