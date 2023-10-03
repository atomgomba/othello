package com.ekezet.othello

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.game.GameState
import timber.log.Timber

internal sealed interface MainAction : Action<MainModel, MainDependency>

internal data object OnNewGameClicked : MainAction {
    override fun MainModel.proceed() =
        trigger(UpdateGameBoardGameState(GameState.new()))
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

internal data object OnShareGameClicked : MainAction {
    override fun MainModel.proceed() = trigger(SerializeBoard)
}

internal data class OnGameStateSerialized(
    private val data: String,
) : MainAction {
    override fun MainModel.proceed(): Next<MainModel, MainDependency> {
        Timber.d("Board:\n$data")
        return trigger(StartShareIntent(data))
    }
}
