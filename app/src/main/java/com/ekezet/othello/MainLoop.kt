package com.ekezet.othello

import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopScope
import com.ekezet.othello.MainAction.OnNewGameClicked
import com.ekezet.othello.MainAction.OnToggleIndicatorsClicked
import com.ekezet.othello.feature.gameboard.GameBoardArgs
import kotlinx.coroutines.CoroutineScope

class MainLoop(scope: CoroutineScope) :
    Loop<MainState, MainModel, Unit, MainDependency, MainAction>(
        coroutineScope = scope,
        args = null,
        dependency = MainDependency,
    ) {
    override fun initModel(args: Unit?) =
        MainModel()

    override fun renderState(model: MainModel) = with(model) {
        MainState(
            gameSettings = model,
            gameBoardArgs = GameBoardArgs(
                initialGameState = initialGameState,
                displayOptions = displayOptions,
                opponentStrategy = opponentStrategy,
            ),
            onNewGameClick = { emit(OnNewGameClicked) },
            onToggleIndicatorsClick = { emit(OnToggleIndicatorsClicked) },
        )
    }
}

typealias MainScope = LoopScope<MainModel, MainDependency>

fun mainLoop(scope: CoroutineScope) =
    MainLoop(scope)
