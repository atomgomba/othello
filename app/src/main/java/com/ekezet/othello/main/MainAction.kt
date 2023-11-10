package com.ekezet.othello.main

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.game.OthelloGameState

internal sealed interface MainAction : Action<MainModel, MainDependency>

internal data object OnNewGameClicked : MainAction {
    override fun MainModel.proceed() =
        trigger(UpdateGameBoardGameState(OthelloGameState.new()))
}

internal data object OnToggleIndicatorsClicked : MainAction {
    override fun MainModel.proceed(): Next<MainModel, MainDependency> {
        val newSettings = copy(
            displayOptions = displayOptions.copy(
                showPossibleMoves = !displayOptions.showPossibleMoves,
            ),
        )
        return outcome(newSettings, PublishGameSettings(newSettings))
    }
}
