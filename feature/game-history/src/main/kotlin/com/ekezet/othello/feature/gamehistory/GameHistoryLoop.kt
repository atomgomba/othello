package com.ekezet.othello.feature.gamehistory

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.core.game.MoveHistory

internal class GameHistoryLoop(
    model: GameHistoryModel,
    renderer: GameHistoryRenderer,
    args: MoveHistory? = null,
    dependency: GameHistoryDependency? = null,
) : Loop<GameHistoryState, GameHistoryModel, MoveHistory, GameHistoryDependency, GameHistoryAction>(
    model = model,
    renderer = renderer,
    args = args,
    dependency = dependency,
) {
    override fun GameHistoryModel.applyArgs(args: MoveHistory) = copy(
        moveHistory = args,
    )

    internal companion object Builder : LoopBuilder<GameHistoryState, GameHistoryModel, MoveHistory, GameHistoryDependency, GameHistoryAction> {
        override fun build(args: MoveHistory?) = GameHistoryLoop(
            model = GameHistoryModel(),
            renderer = GameHistoryRenderer(),
            args = requireNotNull(args) { "Args must be set" },
            dependency = GameHistoryDependency,
        )
    }
}
