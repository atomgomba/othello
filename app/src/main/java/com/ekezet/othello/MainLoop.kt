package com.ekezet.othello

import com.ekezet.hurok.Loop
import com.ekezet.othello.MainAction.OnNewGameClicked
import com.ekezet.othello.MainAction.OnToggleIndicatorsClicked
import com.ekezet.othello.feature.gameboard.defaultGameBoardArgs
import kotlinx.coroutines.CoroutineScope

internal class MainLoop(
    parentScope: CoroutineScope,
    initModel: MainModel,
    dependency: MainDependency,
) : Loop<MainState, MainModel, Unit, MainDependency, MainAction>(
    loopCoroutineScope = parentScope,
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
}

internal fun mainLoop(parentScope: CoroutineScope, dependency: MainDependency) =
    MainLoop(
        parentScope = parentScope,
        initModel = MainModel(),
        dependency = dependency,
    )
