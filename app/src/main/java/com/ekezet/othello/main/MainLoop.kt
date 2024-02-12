package com.ekezet.othello.main

import com.ekezet.hurok.AnyActionEmitter
import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.feature.gameboard.GameBoardEmitter

internal class MainLoop internal constructor(
    model: MainModel,
    renderer: MainRenderer,
    args: GameSettings? = null,
    dependency: MainDependency? = null,
) : Loop<MainState, MainModel, GameSettings, MainDependency, MainAction>(
    model = model,
    renderer = renderer,
    args = args,
    dependency = dependency,
) {
    override fun MainModel.applyArgs(args: GameSettings) = copy(
        displayOptions = args.displayOptions,
        lightStrategy = args.lightStrategy,
        darkStrategy = args.darkStrategy,
    )

    @Suppress("UNCHECKED_CAST")
    override fun MainDependency.onAddChildEmitter(child: AnyActionEmitter) {
        gameBoardEmitter = (child as? GameBoardEmitter) ?: gameBoardEmitter
    }

    internal companion object Builder :
        LoopBuilder<MainState, MainModel, GameSettings, MainDependency, MainAction> {
        override fun build(
            args: GameSettings?,
        ) = MainLoop(
            model = MainModel(),
            renderer = MainRenderer(),
            args = requireNotNull(args) { "Args must be set" },
            dependency = MainDependency(),
        )
    }
}
