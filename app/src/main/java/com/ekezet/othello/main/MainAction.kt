package com.ekezet.othello.main

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.game.data.defaultGameState

internal sealed interface MainAction : Action<MainModel, MainDependency>

internal data object FirstAction : MainAction {
    override fun MainModel.proceed() = trigger(CollectData)
}

internal data object OnNewGameClicked : MainAction {
    override fun MainModel.proceed() =
        trigger(UpdateGameBoardGameState(defaultGameState))
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

internal data class OnMoveHistoryChanged(
    private val isNotEmpty: Boolean,
) : MainAction {
    override fun MainModel.proceed() = mutate(copy(hasGameHistory = isNotEmpty))
}
