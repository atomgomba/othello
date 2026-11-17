package com.ekezet.othello.main

import com.ekezet.hurok.Action
import com.ekezet.hurok.next
import com.ekezet.hurok.trigger
import com.ekezet.othello.core.game.data.Start
import com.ekezet.othello.core.game.state.OthelloGameState

internal sealed interface MainAction : Action<MainModel, MainDependency>

internal data object OnNewGameClicked : MainAction {
    override fun proceed(model: MainModel) =
        trigger(UpdateGameBoardGameState(OthelloGameState.Start), ResetPastMoves)
}

internal data object OnToggleIndicatorsClicked : MainAction {
    override fun proceed(model: MainModel) = with(model) {
        val newSettings = copy(
            boardDisplayOptions = boardDisplayOptions.copy(
                showPossibleMoves = !boardDisplayOptions.showPossibleMoves,
            ),
        )
        next(newSettings, PublishGameSettings(newSettings))
    }
}

internal data object OnBackPressed : MainAction {
    override fun proceed(model: MainModel) = with(model) {
        val effects = buildSet<MainEffect> {
            if (isExitMessageVisible) {
                add(FinishActivity)
            }
        }
        next(
            model = copy(backPressCount = backPressCount + 1),
            effects = effects.toTypedArray(),
        )
    }
}

internal data object OnCancelExitClicked : MainAction {
    override fun proceed(model: MainModel) = next(model.copy(backPressCount = 0))
}
