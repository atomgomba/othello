package com.ekezet.othello.main

import com.ekezet.hurok.AnyActionEmitter
import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.feature.gameboard.GameBoardEmitter

internal class MainLoop internal constructor(
    model: MainModel,
    renderer: MainRenderer,
    args: MainArgs? = null,
    dependency: MainDependency? = null,
) : Loop<MainState, MainModel, MainArgs, MainDependency, MainAction>(
    model = model,
    renderer = renderer,
    args = args,
    dependency = dependency,
) {
    override fun MainModel.applyArgs(args: MainArgs) = copy(
        displayOptions = args.gameSettings.displayOptions,
        lightStrategy = args.gameSettings.lightStrategy,
        darkStrategy = args.gameSettings.darkStrategy,
        hasGameHistory = args.moveHistory.isNotEmpty(),
    )

    @Suppress("UNCHECKED_CAST")
    override fun MainDependency.onAddChildEmitter(child: AnyActionEmitter) {
        gameBoardEmitter = (child as? GameBoardEmitter) ?: gameBoardEmitter
    }

    internal companion object Builder :
        LoopBuilder<MainState, MainModel, MainArgs, MainDependency, MainAction> {
        override fun build(
            args: MainArgs?,
        ) = MainLoop(
            model = MainModel(),
            renderer = MainRenderer(),
            args = requireNotNull(args) { "Args must be set" },
            dependency = MainDependency(),
        )
    }
}
