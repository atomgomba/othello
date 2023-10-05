package com.ekezet.othello.main

import com.ekezet.hurok.AnyParentLoop
import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.feature.gameboard.GameBoardScope

internal class MainLoop private constructor(
    args: GameSettings,
    dependency: MainDependency,
) :
    Loop<MainState, MainModel, GameSettings, MainDependency, MainAction>(
        args = args,
        dependency = dependency,
    ) {

    override fun initModel() = MainModel()

    override fun renderState(model: MainModel) =
        MainState(
            onNewGameClick = { emit(OnNewGameClicked) },
            onToggleIndicatorsClick = { emit(OnToggleIndicatorsClicked) },
            onShareGameClick = { emit(OnShareGameClicked) },
        )

    override fun MainModel.applyArgs(args: GameSettings) = copy(
        displayOptions = args.displayOptions,
        lightStrategy = args.lightStrategy,
        darkStrategy = args.darkStrategy,
    )

    @Suppress("UNCHECKED_CAST")
    override fun MainDependency.onAddChildLoop(child: AnyParentLoop) {
        gameBoardScope = child as? GameBoardScope
    }

    internal companion object Builder :
        LoopBuilder<MainState, MainModel, GameSettings, MainDependency, MainAction> {
        override fun invoke(
            args: GameSettings?,
            dependency: MainDependency?,
        ) = MainLoop(
            args = requireNotNull(args) { "Args must be set" },
            dependency = requireNotNull(dependency) { "Dependency must be set" },
        )
    }
}
