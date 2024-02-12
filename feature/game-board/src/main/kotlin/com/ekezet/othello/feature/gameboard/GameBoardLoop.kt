package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.defaultGameState
import com.ekezet.othello.feature.gameboard.actions.GameBoardAction
import com.ekezet.othello.feature.gameboard.actions.OnGameStarted

internal class GameBoardLoop internal constructor(
    model: GameBoardModel,
    renderer: GameBoardRenderer,
    args: GameSettings? = null,
    firstAction: GameBoardAction? = null,
) : Loop<GameBoardState, GameBoardModel, GameSettings, Unit, GameBoardAction>(
    model = model,
    renderer = renderer,
    args = args,
    firstAction = firstAction,
) {
    override fun GameBoardModel.applyArgs(args: GameSettings): GameBoardModel {
        val strategyChanged =
            lightStrategy != args.lightStrategy || darkStrategy != args.darkStrategy
        return copy(
            displayOptions = args.displayOptions,
            lightStrategy = args.lightStrategy,
            darkStrategy = args.darkStrategy,
        ).run {
            if (strategyChanged) {
                resetNextTurn(defaultGameState)
            } else {
                this
            }
        }
    }

    internal companion object Builder :
        LoopBuilder<GameBoardState, GameBoardModel, GameSettings, Unit, GameBoardAction> {
        override fun build(
            args: GameSettings?,
        ) = GameBoardLoop(
            model = GameBoardModel(),
            renderer = GameBoardRenderer(),
            args = requireNotNull(args) { "Arguments must be set" },
            firstAction = OnGameStarted,
        )
    }
}
