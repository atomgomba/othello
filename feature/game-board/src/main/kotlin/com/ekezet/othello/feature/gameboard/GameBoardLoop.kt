package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.feature.gameboard.actions.GameBoardAction
import com.ekezet.othello.feature.gameboard.actions.OnLoopStarted

internal class GameBoardLoop internal constructor(
    model: GameBoardModel,
    renderer: GameBoardRenderer,
    args: GameBoardArgs?,
    argsApplyer: GameBoardArgsApplyer?,
    onStart: GameBoardEmitter.() -> Unit = {},
    dependency: GameBoardDependency? = null,
) : Loop<GameBoardState, GameBoardModel, GameBoardArgs, GameBoardDependency, GameBoardAction>(
    model = model,
    renderer = renderer,
    args = args,
    argsApplyer = argsApplyer,
    onStart = onStart,
    dependency = dependency,
) {
    internal companion object Builder :
        LoopBuilder<GameBoardState, GameBoardModel, GameBoardArgs, GameBoardDependency, GameBoardAction> {
        private val argsApplyer: GameBoardArgsApplyer
            get() = GameBoardArgsApplyer { args ->
                val strategyChanged = containsDifferentStrategy(args)
                copy(
                    selectedTurn = args.selectedTurn ?: selectedTurn,
                    boardDisplayOptions = args.boardDisplayOptions,
                    lightStrategy = args.lightStrategy,
                    darkStrategy = args.darkStrategy,
                ).run {
                    if (strategyChanged) {
                        resetNewGame()
                    } else {
                        this
                    }
                }
            }

        override fun build(args: GameBoardArgs?) = GameBoardLoop(
            model = GameBoardModel(),
            renderer = GameBoardRenderer(),
            args = requireNotNull(args) { "Arguments must be set" },
            argsApplyer = argsApplyer,
            dependency = GameBoardDependency(),
            onStart = { emit(OnLoopStarted) },
        )
    }
}
