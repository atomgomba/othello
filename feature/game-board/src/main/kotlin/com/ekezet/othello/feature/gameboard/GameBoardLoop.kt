package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.state.OthelloGameState
import com.ekezet.othello.feature.gameboard.actions.GameBoardAction
import com.ekezet.othello.feature.gameboard.actions.OnGameStarted

internal class GameBoardLoop internal constructor(
    model: GameBoardModel,
    renderer: GameBoardRenderer,
    args: GameSettings? = null,
    dependency: GameBoardDependency? = null,
    firstAction: GameBoardAction? = null,
) : Loop<GameBoardState, GameBoardModel, GameSettings, GameBoardDependency, GameBoardAction>(
    model = model,
    renderer = renderer,
    args = args,
    dependency = dependency,
    firstAction = firstAction,
) {
    override fun GameBoardModel.applyArgs(args: GameSettings): GameBoardModel {
        val strategyChanged = containsDifferentStrategy(args)
        return copy(
            displayOptions = args.displayOptions,
            lightStrategy = args.lightStrategy,
            darkStrategy = args.darkStrategy,
        ).run {
            if (strategyChanged) {
                resetNextTurn(OthelloGameState.Default)
            } else {
                this
            }
        }
    }

    internal companion object Builder :
        LoopBuilder<GameBoardState, GameBoardModel, GameSettings, GameBoardDependency, GameBoardAction> {
        override fun build(
            args: GameSettings?,
        ) = GameBoardLoop(
            model = GameBoardModel(),
            renderer = GameBoardRenderer(),
            args = requireNotNull(args) { "Arguments must be set" },
            dependency = GameBoardDependency(),
            firstAction = OnGameStarted,
        )
    }
}
