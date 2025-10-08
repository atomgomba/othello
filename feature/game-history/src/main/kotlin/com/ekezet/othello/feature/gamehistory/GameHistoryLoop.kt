package com.ekezet.othello.feature.gamehistory

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder

internal class GameHistoryLoop(
    model: GameHistoryModel,
    renderer: GameHistoryRenderer,
    args: GameHistoryArgs,
    argsApplyer: GameHistoryArgsApplyer,
    dependency: GameHistoryDependency? = null,
) : Loop<GameHistoryState, GameHistoryModel, GameHistoryArgs, GameHistoryDependency, GameHistoryAction>(
    model = model,
    renderer = renderer,
    args = args,
    argsApplyer = argsApplyer,
    dependency = dependency,
) {
    internal companion object Builder :
        LoopBuilder<GameHistoryState, GameHistoryModel, GameHistoryArgs, GameHistoryDependency, GameHistoryAction> {
        private val argsApplyer: GameHistoryArgsApplyer
            get() = GameHistoryArgsApplyer { args ->
                with(args) {
                    copy(
                        moveHistory = history.history,
                        gameEnd = history.gameEnd,
                        historyImages = historyImages,
                        gameSettings = gameSettings,
                        historySettings = historySettings,
                    )
                }
            }

        override fun build(args: GameHistoryArgs?) = GameHistoryLoop(
            model = GameHistoryModel(),
            renderer = GameHistoryRenderer(),
            args = requireNotNull(args) { "Args must be set" },
            argsApplyer = argsApplyer,
            dependency = GameHistoryDependency,
        )
    }
}
