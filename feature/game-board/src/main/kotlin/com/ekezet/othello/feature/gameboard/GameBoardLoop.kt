package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.feature.gameboard.actions.GameBoardAction
import com.ekezet.othello.feature.gameboard.actions.OnGameStarted

internal class GameBoardLoop internal constructor(
    model: GameBoardModel,
    renderer: GameBoardRenderer,
    args: GameBoardArgs?,
    dependency: GameBoardDependency? = null,
    firstAction: GameBoardAction? = null,
) : Loop<GameBoardState, GameBoardModel, GameBoardArgs, GameBoardDependency, GameBoardAction>(
    model = model,
    renderer = renderer,
    args = args,
    dependency = dependency,
    firstAction = firstAction,
) {
    override fun GameBoardModel.applyArgs(args: GameBoardArgs): GameBoardModel {
        val strategyChanged = containsDifferentStrategy(args)
        return copy(
            selectedTurn = args.selectedTurn ?: currentGameState.turn,
            boardDisplayOptions = args.boardDisplayOptions,
            lightStrategy = args.lightStrategy ?: lightStrategy,
            darkStrategy = args.darkStrategy ?: darkStrategy,
        ).run {
            if (strategyChanged) {
                resetNewGame()
            } else {
                this
            }
        }
    }

    internal companion object Builder :
        LoopBuilder<GameBoardState, GameBoardModel, GameBoardArgs, GameBoardDependency, GameBoardAction> {
        override fun build(
            args: GameBoardArgs?,
        ) = GameBoardLoop(
            model = GameBoardModel(),
            renderer = GameBoardRenderer(),
            args = requireNotNull(args) { "Arguments must be set" },
            dependency = GameBoardDependency(),
            firstAction = OnGameStarted,
        )
    }
}
