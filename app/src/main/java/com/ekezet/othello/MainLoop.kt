package com.ekezet.othello

import com.ekezet.hurok.Loop
import com.ekezet.othello.MainAction.OnNewGameClicked
import com.ekezet.othello.MainAction.OnToggleIndicatorsClicked
import com.ekezet.othello.feature.gameboard.GameBoardArgs
import kotlinx.coroutines.CoroutineScope

class MainLoop(parentScope: CoroutineScope, initModel: MainModel) :
    Loop<MainState, MainModel, Unit, Unit, MainAction>(
        coroutineScope = parentScope,
        initModel = initModel,
    ) {

    override fun renderState(model: MainModel) = with(model) {
        MainState(
            gameSettings = model,
            gameBoardArgs = GameBoardArgs(
                gameState = gameState,
                displayOptions = displayOptions,
                opponentStrategy = opponentStrategy,
            ),
            onNewGameClick = { emit(OnNewGameClicked) },
            onToggleIndicatorsClick = { emit(OnToggleIndicatorsClicked) },
        )
    }
}

fun mainLoop(parentScope: CoroutineScope) =
    MainLoop(parentScope = parentScope, initModel = MainModel())
