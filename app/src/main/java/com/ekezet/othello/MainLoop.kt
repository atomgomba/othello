package com.ekezet.othello

import com.ekezet.hurok.AnyLoopScope
import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.MainAction.OnNewGameClicked
import com.ekezet.othello.MainAction.OnToggleIndicatorsClicked
import com.ekezet.othello.feature.gameboard.GameBoardScope
import com.ekezet.othello.feature.gameboard.defaultGameBoardArgs

internal class MainLoop(
    initModel: MainModel,
    dependency: MainDependency,
) : Loop<MainState, MainModel, Unit, MainDependency, MainAction>(
    initModel = initModel,
    dependency = dependency,
) {
    override fun renderState(model: MainModel) = with(model) {
        MainState(
            gameSettings = model,
            gameBoardArgs = defaultGameBoardArgs.copy(
                displayOptions = displayOptions,
                opponentStrategy = opponentStrategy,
            ),
            onNewGameClick = { emit(OnNewGameClicked) },
            onToggleIndicatorsClick = { emit(OnToggleIndicatorsClicked) },
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun MainDependency.onAddChildLoop(child: AnyLoopScope) {
        gameBoardScope = child as? GameBoardScope
    }
}

internal val mainLoopBuilder: LoopBuilder<MainState, MainModel, Unit, MainDependency, MainAction> =
    { initModel, _, dependency ->
        MainLoop(
            initModel = initModel,
            dependency = requireNotNull(dependency) { "MainLoop dependency not set" },
        )
    }
